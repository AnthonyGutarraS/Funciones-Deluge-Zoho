string button.GenerarCuotasCavali(String idMain)
{
jsonMain = zoho.crm.getRecordById("Cronogramas_de_Cuotas",idMain);
//info jsonMain;
if(ifnull(jsonMain.get("id"),"").toString() != "")
{
	//iniciar proceso.
	apiTokenAccessJson = zoho.crm.getOrgVariable("tokenCavaliGeneracionLetras","crminternalkey");
	cavaliTokenAccess = "";
	if(ifnull(apiTokenAccessJson,"").toString().contains("token"))
	{
		cavaliTokenAccess = apiTokenAccessJson.toMap().get("token");
	}
	header = Map();
	header.put("Content-Type","application/json");
	header.put("Authorization","Bearer " + cavaliTokenAccess);
	body = Map();
	salesCode = jsonMain.get("N_Proforma");
	//"123456";
	operationCode = "RELE2501031521123456";
	documentTypeCodeSignee = "DNI";
	documentNumberSignee = "70573799";
	documentNumberDrawer = "20611749792";
	curlParam = "salesCode=" + salesCode + "&operationCode=" + operationCode + "&documentTypeCodeSignee=" + documentTypeCodeSignee + "&documentNumberSignee=" + documentNumberSignee + "&documentNumberDrawer=" + documentNumberDrawer;
	body.put("salesCode",salesCode);
	body.put("operationCode","RELE2501031521123456");
	body.put("documentTypeCodeSignee","DNI");
	body.put("documentNumberSignee","70573799");
	body.put("documentNumberDrawer","20611749792");
	parserUrlEncoder = encodeUrl(curlParam);
	//info parserUrlEncoder;
	getLetrasRelacionadas = invokeurl
	[
		url :"https://letras-desa.canvia-aws.es:8861/letrest/digitall-rest/letras-api/search-operations-letter?" + "salesCode=" + salesCode + "&operationCode=" + operationCode + "&documentTypeCodeSignee=" + documentTypeCodeSignee + "&documentNumberSignee=" + documentNumberSignee + "&documentNumberDrawer=" + documentNumberDrawer
		type :GET
		parameters:body.toString()
		headers:header
	];
	if(ifnull(getLetrasRelacionadas,"").toString().contains("message"))
	{
		jsonObjectReturnApi = getLetrasRelacionadas.tomap().get("salesDetailList").toList().get(0).toMap();
		if(ifnull(getLetrasRelacionadas.tomap().get("message"),"").equals("Conforme"))
		{
			//documentTypeDrawer
			//nameDrawer
			//documentNumberDrawer
			//specialClauses
			//operationCode
			//signeeList[]
			//acceptantList[]
			//guaranteeList[]
			//billExchangeList[]
			documentTypeDrawer = jsonObjectReturnApi.tomap().get("documentTypeDrawer") + "";
			nameDrawer = jsonObjectReturnApi.tomap().get("nameDrawer") + "";
			documentNumberDrawer = jsonObjectReturnApi.tomap().get("documentNumberDrawer") + "";
			specialClauses = jsonObjectReturnApi.tomap().get("specialClauses") + "";
			operationCode = jsonObjectReturnApi.tomap().get("operationCode") + "";
			signeeList = jsonObjectReturnApi.tomap().get("signeeList").tolist();
			acceptantList = jsonObjectReturnApi.tomap().get("acceptantList").tolist();
			guaranteeList = jsonObjectReturnApi.tomap().get("guaranteeList").tolist();
			billExchangeList_Cuotas = jsonObjectReturnApi.tomap().get("billExchangeList").tolist();
			for each  recCuota in billExchangeList_Cuotas
			{
				try 
				{
					nroDeCuota = recCuota.get("number");
					fechaDeVencimiento = recCuota.get("dueAt");
					moneda = ifnull(recCuota.get("currencyCode"),"").toString();
					montoAPagar = ifnull(recCuota.get("amount"),0.0).todecimal();
					newCuota = Map();
					newCuota.put("Name",nroDeCuota);
					newCuota.put("Fecha_de_Vencimiento",fechaDeVencimiento.toString().toList("T").get(0));
					newCuota.put("Currency",if(moneda.contains("DOLA"),"USD","PEN"));
					newCuota.put("Monto_a_pagar",montoAPagar);
					newCuota.put("Cronograma_de_Cuota",jsonMain.get("id"));
					newCuota.put("Aprobaci_n_de_cuota","Aprobado");
					newCuota.put("Estado","Programado");
					newCuota.put("Concepto",null);
					arregloDeals = zoho.crm.getRelatedRecords("Deals","Contacts",jsonMain.get("Titular").toMap().get("id")).tolist().get(0).toMap();
					newCuota.put("Titular",jsonMain.get("Titular").toMap().get("Name"));
					newCuota.put("Oportunidad_Titular",arregloDeals.get("id"));
					newCuota.put("Contacto_Titular",jsonMain.get("Titular").toMap().get("id"));
					info zoho.crm.createRecord("Cuotas",newCuota);
					//return "Las cuotas se han Cargado exitosamente, revisar Seccion debajo";
				}
				catch (Error)
				{
					info Error;
				}
			}
			//return "Exito, Refrescar Ventana para ver los cambios.";
			return "Las cuotas se han Cargado exitosamente, revisar Seccion debajo";
		}
		else
		{
			return "No fue posible recuperar informacion del Cronograma";
		}
	}
	return "";
}
try 
{
}
catch (Error)
{
}
return "";
}