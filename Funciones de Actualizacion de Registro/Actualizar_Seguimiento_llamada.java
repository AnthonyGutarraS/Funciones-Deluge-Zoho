//Esta funcion actualiza un registro del modulo Seguimiento cuando se actualiza una Llamada y en caso de encontrar el registro se crea uno nuevo

void automation.actualizar_llamada_seguimiento(Int id_llamada)
{
callDetails = zoho.crm.getRecordById("Calls",id_llamada);
if(callDetails != null)
{
	criteria = "ID_Actividad:equals:" + id_llamada;
	existingRecords = zoho.crm.searchRecords("Seguimiento_de_Asesores",criteria);
	activityMap = Map();
	activityMap.put("Name",callDetails.get("Subject"));
	activityMap.put("Tipo_de_Actividad","Llamada");
	activityMap.put("Fecha_de_Actividad",callDetails.get("Call_Start_Time"));
	activityMap.put("Estado",callDetails.get("Call_Status"));
	activityMap.put("Duraci_n_min",callDetails.get("Call_Duration"));
	activityMap.put("Resultado_de_la_llamada",callDetails.get("Call_Result"));
	activityMap.put("ID_Actividad",id_llamada.toString());
	// Asociar con módulos relacionados
	if(callDetails.get("$se_module") == "Deals" && callDetails.get("What_Id") != null)
	{
		activityMap.put("Relacionado_con_Oportunidad",callDetails.get("What_Id").get("id"));
	}
	if(callDetails.get("$se_module") == "Leads" && callDetails.get("What_Id") != null)
	{
		activityMap.put("Relacionado_con_Posibles_Clientes",callDetails.get("What_Id").get("id"));
	}
	if(callDetails.get("$se_module") == "Accounts" && callDetails.get("What_Id") != null)
	{
		activityMap.put("Relacionado_con_Cuenta",callDetails.get("What_Id").get("id"));
	}
	if(callDetails.get("Who_Id") != null)
	{
		activityMap.put("Relacionado_con_Contactos",callDetails.get("Who_Id").get("id"));
	}
	if(existingRecords.size() > 0)
	{
		// Actualizar el registro existente
		seguimientoId = existingRecords.get(0).get("id");
		updateResponse = zoho.crm.updateRecord("Seguimiento_de_Asesores",seguimientoId.toLong(),activityMap);
		info "Registro actualizado: " + updateResponse;
	}
	else
	{
		// Crear un nuevo registro si no existe
		createResponse = zoho.crm.createRecord("Seguimiento_de_Asesores",activityMap);
		info "Nuevo registro creado: " + createResponse;
	}
}
}