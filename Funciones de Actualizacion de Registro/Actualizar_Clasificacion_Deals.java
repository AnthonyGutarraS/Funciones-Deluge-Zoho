void automation.Actualizar_Clasificacion_Deals(Int oportunidadId)
{
info "Función llamada correctamente";
oportunidad = zoho.crm.getRecordById("Deals",oportunidadId.toString());
nuevaClasificacion = oportunidad.get("CLASIFICACI_N");
info "Nueva clasificación obtenida: " + nuevaClasificacion;
cuotasRelacionadas = zoho.crm.getRelatedRecords("Cuotas","Deals",oportunidadId.toString());
info "Cuotas relacionadas obtenidas: " + cuotasRelacionadas;
if(cuotasRelacionadas.size() > 0)
{
	for each  cuota in cuotasRelacionadas
	{
		cuotaId = cuota.get("id");
		estadoCuota = cuota.get("Estado");
		// Solo actualizar si el estado no es "Pagado"
		if(estadoCuota != "Pagado")
		{
			cuotaMap = Map();
			cuotaMap.put("Clasificaci_n",nuevaClasificacion);
			updateResponse = zoho.crm.updateRecord("Cuotas",cuotaId,cuotaMap);
			if(updateResponse.get("status") == "success")
			{
				info "Cuota actualizada exitosamente: " + cuotaId;
			}
			else
			{
				info "Error al actualizar la cuota: " + cuotaId;
				info updateResponse.get("message");
			}
		}
		else
		{
			info "Cuota con ID " + cuotaId + " no actualizada porque está en estado 'Pagado'";
		}
	}
}
else
{
	info "No se encontraron cuotas relacionadas con la oportunidad: " + oportunidadId;
}
}
