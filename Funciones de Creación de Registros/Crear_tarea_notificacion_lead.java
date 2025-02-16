void automation.crear_tarea_recordatorio(Int lead_id)
{
// Obtener información del lead
leadInfo = zoho.crm.getRecordById("Leads",lead_id);
leadName = leadInfo.get("Full_Name");
supervisorId = 5567137000013041001;
// ID del supervisor
// Obtener la hora actual en UTC
nowUTC = zoho.currenttime;
// Configurar zona horaria manualmente (UTC -5)
timezone = "-05:00";
// Ajustar 1 minuto para el recordatorio
reminderTime = nowUTC.addMinutes(1);
// Forzar formato con zona horaria exacta
formattedReminder = reminderTime.toString("yyyy-MM-dd'T'HH:mm:ss") + timezone;
// Crear la tarea
taskMap = Map();
taskMap.put("Subject","Lead No Atendido - " + leadName);
taskMap.put("What_Id",lead_id.toString());
// Asociar la tarea al lead
taskMap.put("$se_module","Leads");
// Especificar el módulo relacionado
taskMap.put("Due_Date",nowUTC.toDate());
// Fecha de vencimiento (hoy)
taskMap.put("Priority","Alta");
taskMap.put("Status","No Iniciado");
taskMap.put("Owner",supervisorId.toString());
// Asignar al supervisor
// Configuración del recordatorio emergente (Formato manual)
reminderMap = Map();
reminderMap.put("ALARM","FREQ=NONE;ACTION=POPUP;TRIGGER=DATE-TIME:" + formattedReminder);
taskMap.put("Remind_At",reminderMap);
// Asignar el recordatorio a la tarea
// Crear la tarea en CRM
response = zoho.crm.createRecord("Tasks",taskMap);
info response;
// Verificar el resultado
}