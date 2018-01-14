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
ticket = @SMS_TicketNumber;
usr=0;
fch=getdate();
Source="defaultobjectserver";
serial = Ticket;
log( "SERIAL: " + serial);

log("El estado es: " + Estado + "\nEl numero de ticket es: " + Ticket + "\nEl nombre de quien reporta es: " + Reporte);

/*
Validaciones para los estados de resuelto y cancelado
1.- Flujo basico el estado glbal cambia a RESUELTO y se elimina el evento *
2.-Flujo alterno 1  no se registra la solucion apartir del evento, el id del incidente se registra en el diario y se debe eliminar del id de incidente para 
volver a ser candidato a incidente y realizar de nuevo la peticion a sm, el operador al detectar que el evento termino satisfactoriamente pero continua 
el analista vuelve a solicitar el proceso a partir del mismo evento, la politica registra la solicitud manual y el usuario que solicito el proceso.

3.- si el evento es cancelado se registra el id del incidente, se elimina el id del incidente y vuelve a ser candidato.
*/

if(Ticket = "" || Estado = "" || Reporte = "" || Estado != "Resuelto" || Estado != "Cancelado"){
   WSListenerResult = NewObject();
   WSListenerResult.Estado="El estado esta vacio";
   WSListenerResult.Ticket = "El Ticket esta Vacio";
   WSListenerResult.Reporte = "El reporte esta Vacio";
}
if(Estado = "Resuelto" && Ticket >= "" && Reporte >= ""){
    WSListenerResult = NewObject();
    WSListenerResult.Estado="Estado recibido";
    WSListenerResult.Ticket = "Numero de ticket recibido";
    WSListenerResult.Reporte = "Nombre de quien reporta recibido";
    log("ESTADOS: " + Estado + Ticket + Reporte);


    //Seactualiza el campo del evento en CMDB_Istatus a Resuelto 
    Filter1="SMS_TicketNumber='"+Ticket+"'";
    log("SERIAL: " + Filter1);
    UpdateExpression="CMDB_Istatus = '"+Estado+"'";
    log("UPDATEExpression: " + UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression);
    log("FIN DE LA POLITICA NetcoolCreateIncident_A");

} elseif (Estado = "Cancelado"){
    WSListenerResult = NewObject();
    WSListenerResult.Estado="Estado recibido: " + Estado;
 
    //Seactualiza el campo del evento en CMDB_Istatus a Resuelto 
    Filter1="SMS_TicketNumber='"+Ticket+"'";
    log("SERIAL: " + Filter1);
    UpdateExpression="CMDB_Istatus = '"+Estado+"'";
    log("UPDATEExpression: " + UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression);
    log("FIN DE LA POLITICA NetcoolCreateIncident_A");

    //ACTUALIZACION EN BITACORA
   /*msj = fch + "|" + "Estado del incidente: " + Estado + "|" + "El estado fue cancelado" + "|" + "El id de incidente es: " + Ticket;
   MyKey = usr +":"+ fch; 
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);*/
}


//WSListenerResult.estado="Cancelado";