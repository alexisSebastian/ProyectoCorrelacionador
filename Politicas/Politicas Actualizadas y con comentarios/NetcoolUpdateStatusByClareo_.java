log("NetcoolUpdateStatusByClareo");
/**
 * TODO:
 * 
 * Realizar validacion para que la politica espere 5 minutos antes de mandar la notificacín para clarear evente
 */
fchClareo = @FirstOccurrence;
log("Fecha clareo: " + fchClareo);
tiempo = 300;
fechaActual = GetDate();
log("Fecha Actual: " + fechaActual);
ticket = @SMS_TicketNumber;
nodo = @TMX_NodeName;
serial = @Serial;
diferencia = fechaActual - fchClareo;

log("DIFERENCIA con formato: " + LocalTime(diferencia,"HH:mm:ss") + "\nFecha del clareo con formato: "+ LocalTime(fchClareo,"HH:mm:ss") + 
"\nFechaActual con formato: " + LocalTime(fechaActual,"HH:mm:ss") + "\nSERIAL" + serial + "\nNODO: " + nodo + "\nTICKET: " + ticket);
log("Tiempo definido en umbral es:" + LocalTime(tiempo,"HH:mm:ss"));
log("\nEl tiempo transcurrido del umbral con formato es: " + LocalTime(diferencia, "HH:mm:ss"));

  //VARIABLES PARA PRUEBAS
  
  /*fhClareo = 1519840638;
  tiempo = 300;
  ticket = "IN-1802-000118";
  serial = 91510055;
  diferencia = GetDate()-fchClareo;*/

if (JavaCall(null, ticket, "matches", {"^[A-Z]{2}-[0-9]{4}-[0-9]{6}$"})) {
        log("El ticket tiene la nomenclatura correcta");
    if(diferencia > tiempo){
        log("El tiempo que se definio si es de 5 minutos.");
        //Esta política está generada por el asistente de Impact. 
    
        //Esta política se basa en el archivo WSDL en /opt/IBM/tivoli/impact/NetcoolIncidentOficial.wsdl
    
        log("Iniciar política 'UpdateStatus'...");
        //Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
        WSSetDefaultPKGName('NetcoolCrearIncidente');
    
    
        //Especificar parámetros
        UpdateNetcoolIncidentRequestDocument=WSNewObject("com.hp.schemas.sm._7.UpdateNetcoolIncidentRequestDocument");
        _UpdateNetcoolIncidentRequest=WSNewSubObject(UpdateNetcoolIncidentRequestDocument,"UpdateNetcoolIncidentRequest");
    
    
        _Model = WSNewSubObject(_UpdateNetcoolIncidentRequest,"Model");
    
        _Keys = WSNewSubObject(_Model,"Keys");
    
        _Number = WSNewSubObject(_Keys,"Number");
        //_Number['StringValue'] = "@SMS_TicketNumber";
        _Number['StringValue'] = ticket; 
    
        _Instance = WSNewSubObject(_Model,"Instance");
    
        //_FechaRegistro = WSNewSubObject(_Instance,"FechaRegistro");
    
        _EstadoGlobal = WSNewSubObject(_Instance,"EstadoGlobal");
        _EstadoGlobal['StringValue'] = "Restaurado";
    
        _BanderaCierre = WSNewSubObject(_Instance,"BanderaCierre");
        _BanderaCierre['StringValue'] = "0";
    
    
        WSParams = {UpdateNetcoolIncidentRequestDocument};
    
    
        //Especifique un nombre de servicio web, un punto final y un método
        WSService = 'NetcoolIncident';
        WSEndPoint = 'http://qcalswpr:13083/SM/7/ws';
        WSMethod = 'UpdateNetcoolIncident';
    
        //Habilitar seguridad de servicio web
        callProps = NewObject();
        callProps.Username="netcooldt";
        callProps.Password="netcooldt";
        callProps.ReuseHttpClient = false;
        callProps.KeepAlive = false;
        callProps.CloseConnection=true;
        callProps.Timeout=1200000;
    
        log("Se va a invocar la llamada de servicio web UpdateNetcoolIncident ......");
    
        WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
        log("Resultado devuelto de la llamada de servicio web UpdateNetcoolIncident: " +WSInvokeDLResult);
    
        /*Filter1="Serial="+serial; Este es el serial del evento
        log ("El serial del evento es: " +Filter1);
        UpdateExpression="TMX_Promote = 30";
        log("VALOR DE TMX_PROMOTE ANTES DE ACTUALIZAR: " + @TMX_Promote);
        log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
        BatchUpdate('data', Filter1, UpdateExpression);*/ //BatchUpdate sirve para actualizar el campo en el evento
    
        //ACTUALIZACION EN BITACORA
        /*msj= fch +"|" + "Evento: " + serial + "|" + "Solucion de evento" + "|" + "El evento fue clareado desde el gestor";
        MyKey = serial +":"+ usr +":"+ fch;
        MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
        log ("El Query del Insert en alerts.journal es: "+MySQL);
        DirectSQL(Source,MySQL,false);*/
         
    }else{
        log("algo esta fallando");
    }
    
}else{
    log("el ticket no coincide");
}