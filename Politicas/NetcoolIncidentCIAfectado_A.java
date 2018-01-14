log("NetcoolIncidentCIAfectado_A");//Se asignan los valores del servicio de A variables
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
log("NetcoolIncidentesCIAfectado");
log("Iniciar política 'NetcoolIncidentesCIAfectado'...");
//Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
WSSetDefaultPKGName('NetcoolIncidentesCIAfectado');

//Especificar parámetros
RetrieveNetcoolIncidentesCIAfectadoRequestDocument=WSNewObject("com.hp.schemas.sm._7.RetrieveNetcoolIncidentesCIAfectadoRequestDocument");
_RetrieveNetcoolIncidentesCIAfectadoRequest=WSNewSubObject(RetrieveNetcoolIncidentesCIAfectadoRequestDocument,"RetrieveNetcoolIncidentesCIAfectadoRequest");


_Model = WSNewSubObject(_RetrieveNetcoolIncidentesCIAfectadoRequest,"Model");

_Keys = WSNewSubObject(_Model,"Keys");
_Keys['Query'] = 'tmx.im.logical.name="'+@CMDB_Logical_Name+'" and (tmx.im.flag=NULL or tmx.im.flag= true)';


_TmxImId = WSNewSubObject(_Keys,"TmxImId");

_Instance = WSNewSubObject(_Model,"Instance");


WSParams = {RetrieveNetcoolIncidentesCIAfectadoRequestDocument};


//Especifique un nombre de servicio web, un punto final y un método
WSService = 'NetcoolIncidentesCIAfectado';
WSEndPoint = 'http://qcalswpr:13081/SM/7/ws';
WSMethod = 'RetrieveNetcoolIncidentesCIAfectado';

//Habilitar seguridad de servicio web
callProps = NewObject();
callProps.Username="netcooldt";
callProps.Password="netcooldt";
callProps.ReuseHttpClient = false;
callProps.KeepAlive = false;
callProps.CloseConnection=true;

log("Se va a invocar la llamada de servicio web RetrieveNetcoolIncidentesCIAfectado ......");

WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
log("Resultado devuelto de la llamada de servicio web RetrieveNetcoolIncidentesCIAfectado: " +WSInvokeDLResult);

//Se extrae el valor de null ya que este indica que el ciid no tiene ningún incidente asociado entonces se promueve a 28
//respuesta = RExtract(WSInvokeDLResult,'.*tmx.im.id=([A-Z]+)".*');
returnCode = RExtract(WSInvokeDLResult,'.*returnCode="([0-9]).*');

if(returnCode = 9 || impactFlag = 200){
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
    
}elseif (returnCode = 0 || ImpactFlag = 2001){
   //EJECUTAR UN BATCHUPDATE PARA AGREGAR EL NUMERO DE TICKET Y PROMOVER A 29
    Filter1="Serial="+serial; //Este es el serial del evento
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="SMS_TicketNumber='"+ticket+"', TMX_Promote = 29";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

   //ACTUALIZACION EN BITACORA
    msj= fch +"|" + "Evento: " + serial + "|" + "Inicia proceso incidente en Service Manager" + "|" + "El CIID tiene incidentes asociados";
    MyKey = serial +":"+ usr +":"+ fch; 
    MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
    log ("El Query del Insert en alerts.journal es: "+MySQL);
    DirectSQL(Source,MySQL,false);

    log("Fin de la politica NetcoolIncidentCIAfectado_A");  
}