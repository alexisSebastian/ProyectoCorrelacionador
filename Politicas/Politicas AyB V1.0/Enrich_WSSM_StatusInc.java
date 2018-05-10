/**Propósito: Realizar la cancelacion o resolucion del evento reportado a Service Manager
 * Fuentes:     OMNIbus         DataSource: defaultobjectserver         DataType: data
 *              Servicios       DataSource:  CNSBADP                    DataType: data2
 * 
 * Gestores involucrados:
 * Fecha de Creación:
 * Fecha de Actualización: 
 */ 

log("Enrich_WSSM_StatusInc");

log("****Enrich_WSSM_StatusInc*****");
usr=0;
fch=getdate();
//serial = @Serial;
Source="defaultobjectserver";
SourceB = "CNSBADP";
result = "Resuelto";
cancel = "Cancelado";

log("El estado es: " + Estado + "\nEl numero de ticket es: " + Ticket + "\nEl nombre de quien reporta es: " + Reporte);

//query para saber de que servidor proviene el evento reportado
sqlA = DirectSQL(Source, "SELECT ServerName FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", false);
serverA = sqlA[0].ServerName;
log("Server: " + serverA);


sqlB = DirectSQL(SourceB, "SELECT ServerName FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", false);
serverB = sqlB[0].ServerName;
log("Server: " + serverB);



if(Ticket == ""){
    WSListenerResult = NewObject();
    WSListenerResult.Ticket = "El Ticket esta Vacio";
}
if (Estado == "" ) {
    WSListenerResult = NewObject();
    WSListenerResult.Estado="El estado esta vacio";
}
if (Reporte == "") {
    WSListenerResult = NewObject();
    WSListenerResult.Reporte = "El reporte esta Vacio";
}
if (Estado != "Resuelto de Service Manager" || Estado != "Cancelado") {
    WSListenerResult = NewObject();
    WSListenerResult.Reporte = "El estado es diferente del esperado";
}

if (serverA == 'CNSAOTD') {
    if(Estado == "Resuelto desde Service Manager" && Ticket != "" && Reporte != "" && JavaCall(null, Ticket, "matches", {"^[A-Z]{2}[0-9]{4}_[0-9]{6}$"})){
        WSListenerResult = NewObject();
        WSListenerResult.Estado="Estado recibido";
        WSListenerResult.Ticket = "Numero de ticket recibido";
        WSListenerResult.Reporte = "Nombre de quien reporta recibido";
        log("ESTADOS: " + Estado + Ticket + Reporte);
    
    
        SQL = DirectSQL(Source, "SELECT Serial FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", False);
        serial = SQL[0].Serial;
        log("SERIAL: " + serial);
    
        Filter1="SMS_TicketNumber='"+Ticket+"'";
        log("SERIAL: " + Filter1);
        UpdateExpression="CMDB_Istatus = '"+Estado+"', TMX_Promote = 30"; 
        log("UPDATEExpression: " + UpdateExpression);
        BatchUpdate('data', Filter1, UpdateExpression);
    
       msj = "Estado del incidente: " + Estado + "|" + "El id de incidente es: " + Ticket; //fch + "|" + + "El estado fue Resuelto" + "|"
       log("El mensaje es :" + msj);
       MyKey = serial +":"+ usr +":"+ fch;
       log("La key es: " + MyKey); 
       MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
       log ("El Query del Insert en alerts.journal es: "+MySQL);
       DirectSQL(Source,MySQL,false);
     
       log("FIN DE LA POLITICA EL ESTADO SE RESOLVIO");
    
    } elseif (Estado == "Cancelado"){
        WSListenerResult = NewObject();
        WSListenerResult.Estado="Estado recibido: " + Estado;
       
        SQL = DirectSQL(Source, "SELECT Serial FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", False);
        log("SQL: " + SQL);    
        serial = SQL[0].Serial;
        log("SERIAL: " + serial);
        
    
        //ACTUALIZACION EN BITACORA se necesita modificar la politica
       msj = "Estado del incidente: " + Estado + "|" + "El estado fue cancelado" + "|" + "El id de incidente es: " + Ticket; //fch + "|" + 
       log("El mensaje es :" + msj);
       MyKey = serial +":"+ usr +":"+ fch;
       log("La key es: " + MyKey); 
       MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
       log ("El Query del Insert en alerts.journal es: "+MySQL);
       DirectSQL(Source,MySQL,false);
    
        //Seactualiza el campo del evento en CMDB_Istatus a Cancelado 
        Filter1="SMS_TicketNumber='"+Ticket+"'";
        log("SERIAL: " + Filter1);
        UpdateExpression="CMDB_Istatus = '"+Estado+"', SMS_TicketNumber = '', TMX_Promote = 0";
        log("UPDATEExpression: " + UpdateExpression);
        BatchUpdate('data', Filter1, UpdateExpression);
     
       log("FIN DE LA POLITICA, EL ESTADO SE CANCELO");
    }
    log("Fin de la politica por medio del servidor A");

} elseif(serverB == 'CNSBOTD'){
    if(Estado == "Resuelto desde Service Manager" && Ticket != "" && Reporte != "" && JavaCall(null, Ticket, "matches", {"^[A-Z]{2}[0-9]{4}_[0-9]{6}$"})){
        WSListenerResult = NewObject();
        WSListenerResult.Estado="Estado recibido";
        WSListenerResult.Ticket = "Numero de ticket recibido";
        WSListenerResult.Reporte = "Nombre de quien reporta recibido";
        log("ESTADOS: " + Estado + Ticket + Reporte);
    
    
        SQL = DirectSQL(SourceB, "SELECT Serial FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", False);
        serial = SQL[0].Serial;
        log("SERIAL: " + serial);
    
        Filter1="SMS_TicketNumber='"+Ticket+"'";
        log("SERIAL: " + Filter1);
        UpdateExpression="CMDB_Istatus = '"+Estado+"', TMX_Promote = 30"; //
        log("UPDATEExpression: " + UpdateExpression);
        BatchUpdate('data2', Filter1, UpdateExpression);
    
       msj = "Estado del incidente: " + Estado + "|"  + "El id de incidente es: " + Ticket; //fch + "|" + + "El estado fue Resuelto" + "|"
       log("El mensaje es :" + msj);
       MyKey = serial +":"+ usr +":"+ fch;
       log("La key es: " + MyKey); 
       MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
       log ("El Query del Insert en alerts.journal es: "+MySQL);
       DirectSQL(SourceB,MySQL,false);
     
       log("FIN DE LA POLITICA EL ESTADO SE RESOLVIO");
    
    } elseif (Estado == "Cancelado"){
        WSListenerResult = NewObject();
        WSListenerResult.Estado="Estado recibido: " + Estado;
   
       
        SQL = DirectSQL(SourceB, "SELECT Serial FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", False);
        log("SQL: " + SQL);    
        serial = SQL[0].Serial;
        log("SERIAL: " + serial);
        
    
        //ACTUALIZACION EN BITACORA se necesita modificar la politica
       msj = "Estado del incidente: " + Estado + "|" + "El estado fue cancelado" + "|" + "El id de incidente es: " + Ticket; //fch + "|" + 
       log("El mensaje es :" + msj);
       MyKey = serial +":"+ usr +":"+ fch;
       log("La key es: " + MyKey); 
       MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
       log ("El Query del Insert en alerts.journal es: "+MySQL);
       DirectSQL(SourceB,MySQL,false);
    
        //Seactualiza el campo del evento en CMDB_Istatus a Cancelado 
        Filter1="SMS_TicketNumber='"+Ticket+"'";
        log("SERIAL: " + Filter1);
        UpdateExpression="CMDB_Istatus = '"+Estado+"', SMS_TicketNumber = '', TMX_Promote = 0";
        log("UPDATEExpression: " + UpdateExpression);
        BatchUpdate('data2', Filter1, UpdateExpression);
     
       log("FIN DE LA POLITICA, EL ESTADO SE CANCELO");
    }

    log("Fin de la politica por medio del servidor B");
}