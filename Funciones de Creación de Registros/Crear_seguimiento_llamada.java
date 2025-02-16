//Esta funcion crea un registro del Modulo Seguimiento de actividades cuando se crea una llamada nueva con todos los campos de la llamda
// en ese registro

void automation.registrar_llamada_seguimiento(int id_llamada)
{
// Obtener detalles de la llamada
callDetails = zoho.crm.getRecordById("Calls",id_llamada);
info callDetails;
if(callDetails != null)
{
	// Mapear campos al módulo Seguimiento de Asesores
	activityMap = Map();
	activityMap.put("Name",callDetails.get("Subject"));// Asunto
	activityMap.put("Tipo_de_Actividad","Llamada");// Tipo
	activityMap.put("Fecha_de_Actividad",callDetails.get("Call_Start_Time"));// Fecha
	activityMap.put("Estado",callDetails.get("Call_Status"));// Estado
	activityMap.put("Duraci_n_min",callDetails.get("Call_Duration"));// Duración
	activityMap.put("Owner",callDetails.get("Owner").get("id"));// Propietario
	activityMap.put("Descripci_n",callDetails.get("Description"));// Descripción
	activityMap.put("Proposito_de_la_llamada",callDetails.get("Call_Purpose"));// Propósito
	activityMap.put("Resultado_de_la_llamada",callDetails.get("Call_Result"));// Resultado
	activityMap.put("Tipo_de_llamada",callDetails.get("Call_Type"));// Tipo
	activityMap.put("ID_Actividad",id_llamada.toString());// ID de actividad
	// Asociar con Oportunidades
	if(callDetails.get("$se_module") == "Deals" && callDetails.get("What_Id") != null)
	{
		activityMap.put("Relacionado_con_Oportunidad",callDetails.get("What_Id").get("id"));
	}
	// Asociar con Posibles Clientes
	if(callDetails.get("$se_module") == "Leads" && callDetails.get("What_Id") != null)
	{
		activityMap.put("Relacionado_con_Posibles_Clientes",callDetails.get("What_Id").get("id"));
	}
	// Asociar con Cuentas
	if(callDetails.get("$se_module") == "Accounts" && callDetails.get("What_Id") != null)
	{
		activityMap.put("Relacionado_con_Cuenta",callDetails.get("What_Id").get("id"));
	}
	// Asociar con Contactos
	if(callDetails.get("Who_Id") != null)
	{
		activityMap.put("Relacionado_con_Contactos",callDetails.get("Who_Id").get("id"));
	}
	// Crear registro en Seguimiento de Asesores
	createResponse = zoho.crm.createRecord("Seguimiento_de_Asesores",activityMap);
	info createResponse;
}
else
{
	info "No se encontraron detalles de la llamada con el ID: " + id_llamada;
}
}