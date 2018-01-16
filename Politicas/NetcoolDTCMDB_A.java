log("NetcoolDTCMDB_A");
//Se asignan los valores del servicio de A a variables
serial = @Serial;
identificador = @Identifier;
usr=0;
fch=getdate();
Source="defaultobjectserver";
log("\n"+"CLLI ES: "+ @TMX_NodeName+"\n" + "El SERIAL ES:"+@Serial+"\n");

nodo = SUBSTRING(@TMX_NodeName, 0, 11);

log("Primer indice del Nodo: " + nodo);

//Esta pol�tica est� generada por el asistente de Impact. 

//Esta pol�tica se basa en el archivo WSDL en /opt/IBM/tivoli/impact/NetcoolDTCMDB_1.wsdl

//Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
WSSetDefaultPKGName('SM_DTCMDB');

//Especificar par�metros
RetrieveNetcoolDTCMDBRequestDocument=WSNewObject("com.hp.schemas.sm._7.RetrieveNetcoolDTCMDBRequestDocument");
_RetrieveNetcoolDTCMDBRequest=WSNewSubObject(RetrieveNetcoolDTCMDBRequestDocument,"RetrieveNetcoolDTCMDBRequest");


_Model = WSNewSubObject(_RetrieveNetcoolDTCMDBRequest,"Model");


_Keys = WSNewSubObject(_Model,"Keys");
_Keys['Query'] = 'display.name="'+nodo+'"'; //Agregar la variable del nodo
_Instance = WSNewSubObject(_Model,"Instance");
 
WSParams = {RetrieveNetcoolDTCMDBRequestDocument};

//Especifique un nombre de servicio web, un punto final y un m�todo
WSService = 'NetcoolDTCMDB';
WSEndPoint = 'http://qcalswpr:13086/SM/7/ws';
WSMethod = 'RetrieveNetcoolDTCMDB';

//Habilitar seguridad de servicio web
callProps = NewObject();
callProps.Username="netcooldt";
callProps.Password="netcooldt";
callProps.ReuseHttpClient = false;
callProps.KeepAlive = false;
callProps.CloseConnection=true;

log("Se va a invocar la llamada de servicio web RetrieveNetcoolDTCMDB ......");

WSInvokeDLResult = WSInvokeDL(WSService, WSEndPoint, WSMethod, WSParams, callProps);
log("Resultado devuelto de la llamada de servicio web RetrieveNetcoolDTCMDB: " +WSInvokeDLResult);

//Se extrae del resultado del ws en WSInvokeDLResult los valores CIID, estatus, subred, tipoRed
CIId= RExtract(WSInvokeDLResult,".*<CIID type=.*>(.*)</CIID>.*"); //de esta forma se obtiene el campo de la respuesta del webservuce
estatus = RExtract(WSInvokeDLResult,".*<estatus type=.*>(.*)</estatus>.*");
subRed = RExtract(WSInvokeDLResult,".*<subred type=.*>(.*)</subred>.*");
tipoRed = RExtract(WSInvokeDLResult,".*<tipoRed type=.*>(.*)</tipoRed>.*");
modelo = RExtract(WSInvokeDLResult,".*<modelo type=.*>(.*)</modelo>.*");
elemetRed = RExtract(WSInvokeDLResult,".*<elementoRed type=.*>(.*)</elementoRed>.*");

log("CIID: "+CIId+"\n"+"ESTATUS: "+estatus+"\n"+"SubRed: "+subRed+"\n"+"TIPO RED: "+tipoRed+"\n"+"EL MODELO ES: "+ modelo+"\n"+"EL ELEMENTO DE RED ES: "+elemeRed);

//Validaciones
if(CIId = NULL){CIId = "";}
if(estatus = NULL){estatus = "";}
if(subRed = NULL){subRed = "";}
if(tipoRed = NULL){tipoRed = "";}
if(modelo = NULL){modelo = "";}
if(elemetRed = NULL){elemetRed = "";}

if((CIId != "" && elemetRed != "" && estatus != "")|| (elementRed = "Equipo" && estatus = "GESTIONADO") || (elementRed = "Enlaces" && estatus = "GESTIONADO") || (elementRed = "Servicio" && estatus = "LIBERADO") || (elementRed = "Servicio Cliente Final" && estatus = "En operacion") || (elementRed = "Puerto" && estatus = "OCUPADO") || (elementRed = "Tarjeta" && estatus = "GESTIONADO"))
{
   Filter1="Serial="+serial; //Este es el serial del evento
   log ("El serial del evento es: " +Filter1);
   UpdateExpression="CMDB_Logical_Name='"+CIId+"',CMDB_Istatus='"+estatus+"',CMDB_Subred='"+subRed+"',CMDB_Tipo_Red='"+tipoRed+"',CMDB_Model='"+modelo+"',CMDB_Topologia='"+elemetRed+"',TMX_Promote = 26";
   log("VALOR DE TMX_PROMOTE ANTES DE ACTUALIZAR: " + @TMX_Promote);
   log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
   BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

   //ACTUALIZACION EN BITACORA
   msj= fch +"|" + "Evento: " + serial + "|" + "Enriquece evento" + "|" + "El CLLI se encontro en Service Manager";
   MyKey = serial +":"+ usr +":"+ fch; 
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);

}
elseif ((CIId = "" && elemetRed = "" && estatus = "")|| (elementRed = "Equipo" && estatus != "GESTIONADO") || (elementRed = "Enlaces" && estatus != "GESTIONADO") || (elementRed = "Servicio" && estatus != "LIBERADO") || (elementRed = "Servicio Cliente Final" && estatus != "En operacion") || (elementRed = "Puerto" && estatus != "OCUPADO") || (elementRed = "Tarjeta" && estatus != "GESTIONADO")) 
{

  //ACTUALIZACION EN BITACORA
   msj= fch +"|" + "Evento: " + serial + "|" + "Enriquece evento" + "|" + "Informacion incompleta en la fuente de enriquecimiento";
   MyKey = serial +":"+ usr +":"+ fch; 
   MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
   log ("El Query del Insert en alerts.journal es: "+MySQL);
   DirectSQL(Source,MySQL,false);
}


log("Fin de la politica NetcoolDTCMDB_A");