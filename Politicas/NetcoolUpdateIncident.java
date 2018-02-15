log("UpdateStatus");
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
_Number['StringValue'] = "IN-1802-000099";

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

log("Se va a invocar la llamada de servicio web UpdateNetcoolIncident ......");

WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
log("Resultado devuelto de la llamada de servicio web UpdateNetcoolIncident: " +WSInvokeDLResult);