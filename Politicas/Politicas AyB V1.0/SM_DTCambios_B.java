/**Propósito: Realizar consulta del CIID para saber si el mismo tiene cambios programados
 * Fuentes:     OMNIbus         DataSource: CNSBADP         DataType: data2
 *              Servicios       DataSource:                 DataType:
 * 
 * Gestores involucrados:  ADSL, RAD y BM
 * Fecha de Creación:
 * Fecha de Actualización: 
 */ 

log("SM_DTCambios_B");
ciid = @CMDB_Logical_Name;
serial = @Serial;
identificador = @Identifier;
serverName = @ServerName;
serverSerial = @ServerSerial;
stateChange = @StateChange;
tmxNodeName = @TMX_NodeName;
tmxPromote = @TMX_Promote;
tally = @Tally;
usr=0;
fch=getdate();
Source="CNSBADP";

log("\n\n\nCMDB_Logical_Name: " + @CMDB_Logical_Name + "\nserial: " + @Serial + "\nServerName: "+ @ServerName + "\nServerSerial:" +@ServerSerial+"\nStateChange: " +@StateChange+"\nTMX_NodeName: "+@TMX_NodeName+"\nTMX_Promote: "+@TMX_Promote+"\nTALLY: "+@Tally);

//Esta política está generada por el asistente de Impact. 

//Esta política se basa en el archivo WSDL en /opt/IBM/tivoli/impact/NDTCambios.wsdl

log("Iniciar política 'NetcoolDTCambios_'...");
//Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
WSSetDefaultPKGName('NetcoolDTCambios');

//Especificar parámetros
RetrieveNetcoolDTCambiosListRequestDocument=WSNewObject("com.hp.schemas.sm._7.RetrieveNetcoolDTCambiosListRequestDocument");
_RetrieveNetcoolDTCambiosListRequest=WSNewSubObject(RetrieveNetcoolDTCambiosListRequestDocument,"RetrieveNetcoolDTCambiosListRequest");


_Keys_0_ = WSNewSubObject(_RetrieveNetcoolDTCambiosListRequest,"Keys");
_Keys_0_['Query'] = 'number#"CA" and (assets="'+@CMDB_Logical_Name+'" or affected.item="'+@CMDB_Logical_Name+'") and open=true';

_IDCambio = WSNewSubObject(_Keys_0_,"IDCambio");


WSParams = {RetrieveNetcoolDTCambiosListRequestDocument};


//Especifique un nombre de servicio web, un punto final y un método
WSService = 'NetcoolDTCambios';
WSEndPoint = 'http://qcalswpr:13081/SM/7/ws';
WSMethod = 'RetrieveNetcoolDTCambiosList';

//Habilitar seguridad de servicio web
callProps = NewObject();
callProps.Username="netcooldt";
callProps.Password="netcooldt";
callProps.ReuseHttpClient = false;
callProps.KeepAlive = false;
callProps.CloseConnection=true;

log("Se va a invocar la llamada de servicio web RetrieveNetcoolDTCambiosList ......");

WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
log("Resultado devuelto de la llamada de servicio web RetrieveNetcoolDTCambiosList: " +WSInvokeDLResult);

returnCode = RExtract(WSInvokeDLResult,'.*returnCode="([0-9]).*');
 
if(returnCode = 9){
    Filter1="Serial="+serial; 
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="TMX_Promote = 27, ImpactFlag=300";
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data2', Filter1, UpdateExpression);

       
    msj= "Enriquece evento" + "|" + "El CIID no tiene cambios asociados"; //fch +"|" + "Evento: " + serial + "|" + 
    MyKey = serial +":"+ usr +":"+ fch; 
    MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
    log ("El Query del Insert en alerts.journal es: "+MySQL);
    DirectSQL(Source,MySQL,false);
}


elseif(returnCode = 0){

  
    Filter1="Serial="+serial; 
    log ("El serial del evento es: " +Filter1);
    UpdateExpression="ImpactFlag=301"; 
    log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
    BatchUpdate('data2', Filter1, UpdateExpression);

  
    msj= "Enriquece evento" + "|" + "El CIID tiene cambios asociados"; //fch +"|" + "Evento: " + serial + "|" + 
    MyKey = serial +":"+ usr +":"+ fch; 
    MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
    log ("El Query del Insert en alerts.journal es: "+MySQL);
    DirectSQL(Source,MySQL,false); 
}
 