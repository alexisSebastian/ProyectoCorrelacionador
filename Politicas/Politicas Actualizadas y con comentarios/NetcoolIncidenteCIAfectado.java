log("NetcoolIncidenteCIAfectado_");

ciid = @CMDB_Logical_Name;
serial = @Serial;
identificador = @Identifier;
serverName = @ServerName;
serverSerial = @ServerSerial;
stateChange = @StateChange;
tmxNodeName = @TMX_NodeName;
tmxPromote = @TMX_Promote;
impactFlag = @ImpactFlag;
tally = @Tally;
usr=0;
fch=getdate();
Source="defaultobjectserver";

log("\n\n\nCMDB_Logical_Name: " + @CMDB_Logical_Name + "\nserial: " + @Serial + "\nServerName: "+ @ServerName + "\nServerSerial:" +@ServerSerial+"\nStateChange: " +@StateChange+"\nTMX_NodeName: "+@TMX_NodeName+"\nTMX_Promote: "+@TMX_Promote+"\nTALLY: "+@Tally);

//Esta política está generada por el asistente de Impact. 

//Esta política se basa en el archivo WSDL en /opt/IBM/tivoli/impact/NetcoolIncidentesCIAfectado.wsdl

log("Iniciar política 'NetcoolIncidenteCIAfectado_'...");
//Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
WSSetDefaultPKGName('NetcoolIncidentesCIAfectado');

//Especificar parámetros
RetrieveNetcoolIncidentesCIAfectadoListRequestDocument=WSNewObject("com.hp.schemas.sm._7.RetrieveNetcoolIncidentesCIAfectadoListRequestDocument");
_RetrieveNetcoolIncidentesCIAfectadoListRequest=WSNewSubObject(RetrieveNetcoolIncidentesCIAfectadoListRequestDocument,"RetrieveNetcoolIncidentesCIAfectadoListRequest");


_Keys_0_ = WSNewSubObject(_RetrieveNetcoolIncidentesCIAfectadoListRequest,"Keys");
_Keys_0_['Query'] = 'tmx.im.logical.name = "'+@CMDB_Logical_Name+'" and (tmx.im.flag=NULL or tmx.im.flag= true)';

_TmxImId = WSNewSubObject(_Keys_0_,"TmxImId");
//_TmxImId = "";


WSParams = {RetrieveNetcoolIncidentesCIAfectadoListRequestDocument};


//Especifique un nombre de servicio web, un punto final y un método
WSService = 'NetcoolIncidentesCIAfectado';
WSEndPoint = 'http://qcalswpr:13083/SM/7/ws';
WSMethod = 'RetrieveNetcoolIncidentesCIAfectadoList';

//Habilitar seguridad de servicio web
callProps = NewObject();
callProps.Username="netcooldt";
callProps.Password="netcooldt";
callProps.ReuseHttpClient = false;
callProps.KeepAlive = false;
callProps.CloseConnection=true;


log("Se va a invocar la llamada de servicio web RetrieveNetcoolIncidentesCIAfectadoList ......");

WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
log("Resultado devuelto de la llamada de servicio web RetrieveNetcoolIncidentesCIAfectadoList: " +WSInvokeDLResult);

//Se extrae el valor de null ya que este indica que el ciid no tiene ningún incidente asociado entonces se promueve a 28
//respuesta = RExtract(WSInvokeDLResult,'.*tmx.im.id=([A-Z]+)".*');
returnCode = RExtract(WSInvokeDLResult,'.*returnCode="([0-9]).*');
idIncidente = RExtract(WSInvokeDLResult,'.*<IdIncidente type=.*>(.*)</IdIncidente>.*');

if(returnCode = 9 || impactFlag = 300){
    Filter1="Serial="+serial; //Este es el serial del evento
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="TMX_Promote = 28";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

    //ACTUALIZACION EN BITACORA
    msj= fch +"|" + "Evento: " + serial + "|" + "Inicia proceso incidente en Service Manager" + "|" + "El CIID no tiene Incidentes asociados";
    MyKey = serial +":"+ usr +":"+ fch; 
    MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
    log ("El Query del Insert en alerts.journal es: "+MySQL);
    DirectSQL(Source,MySQL,false);
    
}elseif (returnCode = 0 || ImpactFlag = 301){
   //EJECUTAR UN BATCHUPDATE PARA AGREGAR EL NUMERO DE TICKET Y PROMOVER A 29
    Filter1="Serial="+serial; //Este es el serial del evento
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="SMS_TicketNumber='"+idIncidente+"', TMX_Promote = 29";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

   //ACTUALIZACION EN BITACORA
    msj= fch +"|" + "Evento: " + serial + "|" + "Inicia proceso incidente en Service Manager" + "|" + "El CIID tiene incidentes asociados" + "|" + "ID de Incidente: " + idIncidente;
    MyKey = serial +":"+ usr +":"+ fch; 
    MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
    log ("El Query del Insert en alerts.journal es: "+MySQL);
    DirectSQL(Source,MySQL,false);

    log("Fin de la politica NetcoolIncidentCIAfectado_");  
}