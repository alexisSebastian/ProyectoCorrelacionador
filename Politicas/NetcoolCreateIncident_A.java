log("NetcoolCreateIncident_");


//CAMPOS PARA SACAR LOS DATOS PARA LA CONSULTA
ciid = @CMDB_Logical_Name;
serial = @Serial;
identificador = @Identifier;
clli = @TMX_NodeName;
promote = @TMX_Promote;
tally = @Tally;
alertKey = @AlertKey;
fisrtOcurrence = @FirstOccurrence;
lastOcurrence = @LastOccurrence;
estatus = @CMDB_Istatus;
alertGroup = @AlertGroup;
referenciaSISA = @TMX_Reference;
estatus = @CMDB_Istatus;
subred = @CMDB_Subred;
tipoRed = @CMDB_Tipo_Red;
elemetoRed = @CMDB_Topologia;
usr=0;
fch=getdate();
usuario = @ONUID;
Source="defaultobjectserver";


log("\nCIID ES: "+@CMDB_Logical_Name+"\nEL SERIAL ES: "+@Serial+"\nEL CLLI ES: "+@TMX_NodeName+"\nEL PROMOTE ES: " +@TMX_Promote+"\nEL AlertKey ES: "+@AlertKey+"\nTALLY: "+@Tally+"\nLastOcurrence ES: "+@LastOccurrence+"\nEl ESTATUS ES: "+@CMDB_Istatus+"\nEl AlertGroup ES: "+@AlertGroup+"\nLa REFERENCIA SISA ES: "+@TMX_Reference+"\nLA SUBRED ES:"+@CMDB_Subred+"\nEL TIPO DE RED ES:"+@CMDB_Tipo_Red+"\nEL ELEMENTO DE RED ES: "+@CMDB_Topologia+"\Usuario :"+@ONUID);
log ("\n NOMENCLATURA DE TICKET: " + dato);
//Esta política está generada por el asistente de Impact.

//Esta política se basa en el archivo WSDL en /opt/IBM/tivoli/impact/NetcoolIncident.wsdl
log("Incidente"); 
  

log("Iniciar política 'NetcoolCreateIncident_'...");
//Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
WSSetDefaultPKGName('NetcoolCrearIncidente');

//Especificar parámetros
CreateNetcoolIncidentRequestDocument=WSNewObject("com.hp.schemas.sm._7.CreateNetcoolIncidentRequestDocument");
_CreateNetcoolIncidentRequest=WSNewSubObject(CreateNetcoolIncidentRequestDocument,"CreateNetcoolIncidentRequest");


_Model = WSNewSubObject(_CreateNetcoolIncidentRequest,"Model");

_Keys = WSNewSubObject(_Model,"Keys");
_Keys['Query'] = "";

_Number = WSNewSubObject(_Keys,"Number");

_Instance = WSNewSubObject(_Model,"Instance");

_FechaRegistro = WSNewSubObject(_Instance,"FechaRegistro");

//Manejar tipo de calendario especial...
date = WSNewObject("java.util.GregorianCalendar");
log("FECHARegistro: " + date);
_FechaRegistro['CalendarValue'] = date;

_Prioridad = WSNewSubObject(_Instance,"Prioridad");
_Prioridad['StringValue'] = "Menor";

_CiAfectado = WSNewSubObject(_Instance,"CiAfectado");
_CiAfectado['StringValue'] = @CMDB_Logical_Name;

_SintomasReportados = WSNewSubObject(_Instance,"SintomasReportados");

_SintomasReportados_0_ = WSNewSubObject(_SintomasReportados,"SintomasReportados");
_SintomasReportados_0_['StringValue'] = @TMX_NodeName + "|" + @AlertKey + "|" + @AlertGroup + "|" + @TMX_Reference;

_FechaInicioDelIncidente = WSNewSubObject(_Instance,"FechaInicioDelIncidente");

//Manejar tipo de calendario especial...
date = WSNewObject("java.util.GregorianCalendar");
//log("FECHARINICIODEINCIDENTE: " + date);
JavaCall(null, date, "setTimeInMillis", { @FirstOccurrence * 1000 });
_FechaInicioDelIncidente['CalendarValue'] = date;

_IdEvento = WSNewSubObject(_Instance,"IdEvento");
_IdEvento['StringValue'] = @TMX_NodeName;

_Estado = WSNewSubObject(_Instance,"Estado");
_Estado['StringValue'] = "Priorizado";

_Fase = WSNewSubObject(_Instance,"Fase");
_Fase['StringValue'] = "Registrar";

_AfectacionAlServicio = WSNewSubObject(_Instance,"AfectacionAlServicio");
_AfectacionAlServicio['StringValue'] = "0";

_IdSistema = WSNewSubObject(_Instance,"IdSistema");
_IdSistema['StringValue'] = "Netcool";

_FuenteDeReporte = WSNewSubObject(_Instance,"FuenteDeReporte");
_FuenteDeReporte['StringValue'] = "Automatico";

_EstadoGlobal = WSNewSubObject(_Instance,"EstadoGlobal");
_EstadoGlobal['StringValue'] = "Registrado";

_TipoRed = WSNewSubObject(_Instance,"TipoRed");
_TipoRed['StringValue'] = @CMDB_Tipo_Red;

_SubRed = WSNewSubObject(_Instance,"SubRed");
_SubRed['StringValue'] = @CMDB_Subred;

_ElementoRed = WSNewSubObject(_Instance,"ElementoRed");
_ElementoRed['StringValue'] = @CMDB_Topologia;

_IdUsuario = WSNewSubObject(_Instance,"IdUsuario");
_IdUsuario['StringValue'] = "netcooldt";

_CantidadServiciosL2L = WSNewSubObject(_Instance,"CantidadServiciosL2L");
_CantidadServiciosL2L['StringValue'] = "0";

_CantidadServiciosIDE = WSNewSubObject(_Instance,"CantidadServiciosIDE");
_CantidadServiciosIDE['StringValue'] = "0";

_CantidadServiciosRPV = WSNewSubObject(_Instance,"CantidadServiciosRPV");
_CantidadServiciosRPV['StringValue'] = "0";

_CantidadQuejas = WSNewSubObject(_Instance,"CantidadQuejas");
_CantidadQuejas['StringValue'] = "0";

_CantidadVideoCamAfectadas = WSNewSubObject(_Instance,"CantidadVideoCamAfectadas");
_CantidadVideoCamAfectadas['StringValue'] = "0";

_BanderaCierre = WSNewSubObject(_Instance,"BanderaCierre");
_BanderaCierre['StringValue'] = "";

_QuienReporta = WSNewSubObject(_Instance,"QuienReporta");
_QuienReporta['StringValue'] = @OwnerUID;


WSParams = {CreateNetcoolIncidentRequestDocument};


log(WSParams);

//Especifique un nombre de servicio web, un punto final y un método
WSService = 'NetcoolIncident';
WSEndPoint = 'http://qcalswpr:13083/SM/7/ws';
WSMethod = 'CreateNetcoolIncident';

//Habilitar seguridad de servicio web
callProps = NewObject();
callProps.Username="netcooldt";
callProps.Password="netcooldt";
callProps.ReuseHttpClient = false;
callProps.KeepAlive = false;
callProps.CloseConnection=true;

log("Se va a invocar la llamada de servicio web CreateNetcoolIncident ......");

WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
log("Resultado devuelto de la llamada de servicio web CreateNetcoolIncident: " +WSInvokeDLResult);

//Se extrae el valor del numero de ticket
ticket = RExtract(WSInvokeDLResult,".*<number type=.*>(.*)</number>.*");
returnCode = RExtract(WSInvokeDLResult,'.*returnCode="([0-9]).*');
 
log("El ticket es : " + ticket);
if (ticket == null && returnCode == 9){
    Filter1="Serial="+serial; //Este es el serial del evento
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="ImpactFlag = 201";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento
    log("FIN DE LA POLITICA NetcoolCreateIncident_A");

   //ACTUALIZACION EN BITACORA
   msj= fch +"|" + "Evento: " + serial + "|" + "Inicia proceso incidente en Service Manager" + "|" + "Error de comunicacion";
   MyKey = serial +":"+ usr +":"+ fch;
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);

}

if (JavaCall(null, ticket, "matches", {"^[A-Z]{2}-[0-9]{4}-[0-9]{6}$"})){

    Filter1="Serial="+serial; //Este es el serial del evento
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="SMS_TicketNumber='"+ticket+"', TMX_Promote = 29";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento
    log("FIN DE LA POLITICA NetcoolCreateIncident_A");

   //ACTUALIZACION EN BITACORA
   msj= fch +"|" + "Evento: " + serial + "|" + "Inicia proceso incidente en Service Manager" + "|" + "Se genero exitosamente el insidente en SM" + "|" + "ID del incidente: " + ticket;
   MyKey = serial +":"+ usr +":"+ fch;
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);
}else{
    Filter1="Serial="+serial; //Este es el serial del evento
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="ImpactFlag = 201";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento
    log("FIN DE LA POLITICA NetcoolCreateIncident_A");

   //ACTUALIZACION EN BITACORA
   msj= fch +"|" + "Evento: " + serial + "|" + "Inicia proceso incidente en Service Manager" + "|" + "Error de informacion enviada";
   MyKey = serial +":"+ usr +":"+ fch;
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);
} 