void automation.genera_boleta_factura_transicion(String id)
{
cuota_record = zoho.crm.getRecordById("Cuotas",id);
idSeguimiento = cuota_record.get("Cronograma_de_Cuota").get("id");
montoPago = cuota_record.get("Monto_Pago");
metodoPago = cuota_record.get("M_todo_de_Pago");
fechaDeposito = cuota_record.get("Fecha_de_Deposito");
bancoDestino = cuota_record.get("Banco_de_destino");
numeroOperacion = cuota_record.get("Numero_de_Operaci_n");
cuentaDestino = cuota_record.get("Cuenta_de_destino");
cronograma = zoho.crm.getRecordById("Cronogramas_de_Cuotas",idSeguimiento);
modalidadPago = cronograma.get("Modalidad_de_Pago");
titular = zoho.crm.getRecordById("Contacts",cronograma.get("Titular").get("id"));
ventaId = cronograma.get("Oportunidad_de_Venta").get("id");
dealRecord = zoho.crm.getRecordById("Deals",ventaId);
lote = dealRecord.get("Lote_s_Interesado_s");
proyecto = dealRecord.get("Proyecto_Interesado").get("name");
saldoAnterior = ifNull(cronograma.get("Monto_pagado"),0);
actualizarCronograma = zoho.crm.updateRecord("Cronogramas_de_Cuotas",idSeguimiento,{"Monto_pagado":saldoAnterior + montoPago},{"trigger":{"workflow","blueprint","approval"}});
correlativo_Factura = "";
facturaMap = Map();
facturaMap.put("Cronograma_de_Cuota",idSeguimiento);
facturaMap.put("Cuota",id);
// Asociación explícita del registro de Cuota
facturaMap.put("Monto_de_pago",montoPago);
facturaMap.put("Metodo_de_pago",metodoPago);
facturaMap.put("Fecha_de_Dep_sito",fechaDeposito);
facturaMap.put("Banco",bancoDestino);
facturaMap.put("N_de_Operaci_n",numeroOperacion);
facturaMap.put("N_Cuenta",cuentaDestino);
facturaMap.put("Contacto",cronograma.get("Titular").get("id"));
facturaMap.put("Tipo_de_Documento",titular.get("Tipo_de_Documento"));
facturaMap.put("N_mero_de_documento",titular.get("N_mero_de_documento"));
facturaMap.put("Direcci_n",titular.get("Direcci_n"));
facturaMap.put("Email",titular.get("Email"));
facturaMap.put("Tipo_de_Factura_Boleta","Contado");
facturaMap.put("Estado_de_Comprobante","Generada");
if(metodoPago == "FACTURA DE CREDITO")
{
	facturaMap.put("Tipo_de_Factura_Boleta","Crédito");
}
if(titular.get("Tipo_de_Documento") == "RUC")
{
	correlativo_Factura = zoho.crm.getOrgVariable("Correlativo_CRM_Facturas");
	info correlativo_Factura;
	facturaMap.put("Tipo_de_Comprobante","Factura");
	facturaMap.put("Name",correlativo_Factura);
	// Actualiza el valor de la variable global sumando 1
	nuevo_valor = correlativo_Factura.toNumber() + 1;
	valueMap = Map();
	valueMap.put("apiname","Correlativo_CRM_Facturas");
	valueMap.put("value",nuevo_valor);
	resp = zoho.crm.invokeConnector("crm.set",valueMap);
	info "A";
}
if(titular.get("Tipo_de_Documento") == "DNI")
{
	correlativo_boleta = zoho.crm.getOrgVariable("Correlativo_CRM_Boletas");
	info correlativo_boleta;
	facturaMap.put("Tipo_de_Comprobante","Boleta");
	facturaMap.put("Name",correlativo_boleta);
	// Actualiza el valor de la variable global sumando 1
	nuevo_valor = correlativo_boleta.toNumber() + 1;
	info nuevo_valor;
	valueMap = Map();
	valueMap.put("apiname","Correlativo_CRM_Boletas");
	valueMap.put("value",nuevo_valor);
	resp = zoho.crm.invokeConnector("crm.set",valueMap);
	info resp;
}
facturaMap.put("Fecha_de_Emisi_n",today);
cuotaList = list();
descripcion = "";
info "modalidadPago" + modalidadPago;
if(modalidadPago == "Contado")
{
	descripcion = "CANCELACION POR TRANSFERENCIA DE POSESION, LOTE " + lote + " PROYECTO VILLA ECO-SOSTENIBLE , SECTOR EL PARAISO PLAYA CHICA DISTRITO DE SANTA MARIA, HUAURA - LIMA.";
}
if(modalidadPago == "Crédito Directo")
{
	descripcion = "ADELANTO POR TRANSFERENCIA DE POSESION  CUOTA " + cuota_record.get("Name") + ", LOTE  " + lote + " PROYECTO VILLA ECO-SOSTENIBLE , SECTOR EL PARAISO PLAYA CHICA DISTRITO DE SANTA MARIA, HUAURA - LIMA.";
}
conceptoCuota = cuota_record.get("Concepto");
if(conceptoCuota == "Cuotas iniciales")
{
	descripcion = "ADELANTO POR TRANSFERENCIA DE POSESION,  CUOTA INICIAL, LOTE " + lote + " PROYECTO VILLA ECO-SOSTENIBLE , SECTOR EL PARAISO PLAYA CHICA DISTRITO DE SANTA MARIA, HUAURA - LIMA.";
}
montoNuevo = montoPago.toDecimal() + ifNull(cuota_record.get("Monto_pagado"),0).toDecimal();
importePagadox = montoPago.toDecimal() / 1.18;
cuotaMap = Map();
cuotaMap.put("Letra",id);
cuotaMap.put("Descripci_n",descripcion);
cuotaMap.put("Cantidad",1);
cuotaMap.put("Importe_Pagado",importePagadox.round(2));
cuotaMap.put("Tipo_de_IGV","Gravado");
cuotaList.add(cuotaMap);
if(metodoPago != "FACTURA DE CREDITO")
{
	updateCuota = zoho.crm.updateRecord("Cuotas",id,{"Monto_pagado":montoNuevo,"Fecha_de_Cancelaci_n":fechaDeposito},{"trigger":{"workflow","blueprint","approval"}});
}
facturaMap.put("Letra_Facturada",cuotaList);
crearFactura = zoho.crm.createRecord("Facturas_Cobranza",facturaMap);
info crearFactura;
// URL construida dinámicamente
module_name = "CustomModule10";
// Nombre del módulo
record_id = "6023069000051749330";
// ID del registro
org_id = "836317560";
// ID de la organización
// Construir la URL
// Construir la URL con un parámetro adicional
url = "crm.zoho.com/crm/org" + org_id + "/tab/" + module_name + "/" + id + "?refresh=true";
// Redirigir al registro específico
openUrl(url,"parent window");
}