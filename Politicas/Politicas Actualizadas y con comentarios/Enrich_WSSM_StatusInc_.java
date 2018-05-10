log("Enrich_WSSM_StatusInc");

/******************************************************* {COPYRIGHT-TOP-RM} ***
 * Licensed Materials - Property of IBM
 * "Restricted Materials of IBM"
 * 5724-S43
 *
 * (C) Copyright IBM Corporation 2006, 2011. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication, or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 ******************************************************* {COPYRIGHT-END-RM} **/
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
sqlA = DirectSQL(Source, "SELECT ServerName FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", false );
serverA = sqlA[0].ServerName;
log("Server A: " + serverA);

sqlB = DirectSQL(SourceB, "SELECT ServerName FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", false);
serverB = sqlB[0].ServerName;
log("ServerB: " + serverB);
 

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
if (Estado != "Resuelto" || Estado != "Cancelado") {
    WSListenerResult = NewObject();
    WSListenerResult.Reporte = "El estado es diferente del esperado";
}
if(Estado == "Resuelto" && Ticket != "" && Reporte != "" && JavaCall(null, Ticket, "matches", {"^[A-Z]{2}[0-9]{4}_[0-9]{6}$"})){
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

   msj = fch + "|" + "Estado del incidente: " + Estado + "|" + "El estado fue Resuelto" + "|" + "El id de incidente es: " + Ticket;
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

    /*query = "select Serial from alerts.status where SMS_TicketNumber = '"+Ticket+"' ";
    CountOnly = False;
    log("El serial traido por el query es: " + query);
    serialResult = DirectSQL(Source,query,CountOnly);
    log("El serial traido por el query es: " + serialResult);*/
    
   

    SQL = DirectSQL(Source, "SELECT Serial FROM alerts.status WHERE SMS_TicketNumber = '"+Ticket+"'", False);
    log("SQL: " + SQL);    
    serial = SQL[0].Serial;
    log("SERIAL: " + serial);
    

    //ACTUALIZACION EN BITACORA se necesita modificar la politica
   /*msj = fch + "|" + "Estado del incidente: " + Estado + "|" + "El estado fue cancelado" + "|" + "El id de incidente es: " + Ticket;
   log("El mensaje es :" + msj);
   MyKey = serial +":"+ usr +":"+ fch;
   log("La key es: " + MyKey); 
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);*/

    //Seactualiza el campo del evento en CMDB_Istatus a Cancelado 
    /*Filter1="SMS_TicketNumber='"+Ticket+"'";
    log("SERIAL: " + Filter1);
    UpdateExpression="CMDB_Istatus = '"+Estado+"', SMS_TicketNumber = '', TMX_Promote = 0";
    log("UPDATEExpression: " + UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression);*/
 
   log("FIN DE LA POLITICA, EL ESTADO SE CANCELO");
}
