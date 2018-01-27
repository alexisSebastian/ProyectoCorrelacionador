/**POLITICA PARA ENRIQUECIMIENTO EN LA CMDB**/

/*Propósito: Proceso de enriquecimiento en la CMDB 
  Fuentes: OMNIbus   DataSource: defaultobjectserver  DataType=data

  Gestores involucrados:   
  Fecha de Creación: 25/01/2018
  Fecha de Actualización: 25/01/2018 */

log("NetcoolDTCMDB");

clli = @TMX_NodeName;
serial = @Serial;
identificador = @Identifier;
usr=0;
fch=getdate();
repisa = @TMX_ShelfNumber;
puerto = @PhysicalPort;
slot = @PhysicalSlot;
server = @ServerName;
Source="defaultobjectserver";

nodo = SUBSTRING(@TMX_NodeName, 0, 11);

log("\n"+"CLLI ES: "+ @TMX_NodeName+"\n" + "El SERIAL ES:"+@Serial+"\n" + "\nLa repisa es: " +repisa +"\El puerto es: " + puerto + "\nEl slot es: " + slot +"\El primer indice del nodo es: " + nodo);


//Validacion para los servidores
if (server == "CNSAOTD"){

}

    //Empieza ejecucion del ws para obtener el enriquecimiento de la CMDB
log("EMPIEZA LA EJECUCION DEL WS, capurando los datos");
WSSetDefaultPKGName('SM_DTCMDB');

RetrieveNetcoolDTCMDBRequestDocument=WSNewObject("com.hp.schemas.sm._7.RetrieveNetcoolDTCMDBRequestDocument");
_RetrieveNetcoolDTCMDBRequest=WSNewSubObject(RetrieveNetcoolDTCMDBRequestDocument,"RetrieveNetcoolDTCMDBRequest");

_Model = WSNewSubObject(_RetrieveNetcoolDTCMDBRequest,"Model");

_Keys = WSNewSubObject(_Model,"Keys");
    //_Keys['Query'] = 'display.name="'+nodo+'" + || + "'+repisa+'" + || + "'+puerto+'" + || + "'+slot'"'; //Agregar la variable del nodo
_Keys['Query'] = 'display.name="'+nodo+'"+"/"+"'+repisa+'"+"/"+"'+puerto+'"+"/"+"'+slot+'"';
_Instance = WSNewSubObject(_Model,"Instance");

WSParams = {RetrieveNetcoolDTCMDBRequestDocument};

  //Especifique un nombre de servicio web, un punto final y un método
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
ciid = RExtract(WSInvokeDLResult,".*<CIID type=.*>(.*)</CIID>.*"); //de esta forma se obtiene el campo de la respuesta del webservuce
estatus = RExtract(WSInvokeDLResult,".*<estatus type=.*>(.*)</estatus>.*");
subRed = RExtract(WSInvokeDLResult,".*<subred type=.*>(.*)</subred>.*");
tipoRed = RExtract(WSInvokeDLResult,".*<tipoRed type=.*>(.*)</tipoRed>.*");
modelo = RExtract(WSInvokeDLResult,".*<modelo type=.*>(.*)</modelo>.*");
elemetRed = RExtract(WSInvokeDLResult,".*<elementoRed type=.*>(.*)</elementoRed>.*");
returnCode = RExtract(WSInvokeDLResult,'.*returnCode="([0-9]).*'); 

log("CIID: "+ciid+"\n"+"ESTATUS: "+estatus+"\n"+"SubRed: "+subRed+"\n"+"TIPO RED: "+tipoRed+"\n"+"EL MODELO ES: "+ modelo+"\n"+"EL ELEMENTO DE RED ES: "+elemetRed);

/***Se realizan las validaciones***/
/**Se valida que los elemntos de res sean los esperados al igual que su estatus */
if ((elementRed = "Equipo" && estatus = "GESTIONADO") || (elementRed = "Enlaces" && estatus = "GESTIONADO") || (elementRed = "Servicio" && estatus = "LIBERADO") || (elementRed = "Servicio Cliente Final" && estatus = "En operacion") || (elementRed = "Puerto" && estatus = "OCUPADO") || (elementRed = "Tarjeta" && estatus = "GESTIONADO")){
    /**Para los elementos de red tipo equipo y que sean gestionados  */
    if (elementoRed "Equipo" && estatus = "GESTIONADO"){
        if (ciid != "" && modelo != "" && elementRed != "" && estatus != "" && subRed != "" && tipoRed != ""){
            Filter1="Serial="+serial; //Este es el serial del evento
            log ("El serial del evento es: " +Filter1);
            UpdateExpression="CMDB_Logical_Name='"+ciid+"',CMDB_Istatus='"+estatus+"',CMDB_Subred='"+subRed+"',CMDB_Tipo_Red='"+tipoRed+"',CMDB_Model='"+modelo+"',CMDB_Topologia='"+elemetRed+"',TMX_Promote = 26, ImpactFlag = 200";
            log("VALOR DE TMX_PROMOTE ANTES DE ACTUALIZAR: " + @TMX_Promote);
            log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
            BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

            //ACTUALIZACION EN BITACORA
            msj= fch +"|" + "Evento: " + serial + "|" + "Enriquece evento" + "|" + "El CLLI se encontro en Service Manager";
            MyKey = serial +":"+ usr +":"+ fch; 
            MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
            log ("El Query del Insert en alerts.journal es: "+MySQL);
            DirectSQL(Source,MySQL,false);
        }else{
            Filter1="Serial="+serial; //Este es el serial del evento
            log ("El serial del evento es: " +Filter1);
            UpdateExpression="CMDB_Logical_Name='"+ciid+"',CMDB_Istatus='"+estatus+"',CMDB_Subred='"+subRed+"',CMDB_Tipo_Red='"+tipoRed+"',CMDB_Model='"+modelo+"',CMDB_Topologia='"+elemetRed+"',TMX_Promote = 0, ImpactFlag = 201";
            log("VALOR DE TMX_PROMOTE ANTES DE ACTUALIZAR: " + @TMX_Promote);
            log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
            BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

            //ACTUALIZACION EN BITACORA
            msj= fch +"|" + "Evento: " + serial + "|" + "Enriquece evento" + "|" + "Informacion incompleta en la fuente de enriquecimiento";
            MyKey = serial +":"+ usr +":"+ fch; 
            MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
            log ("El Query del Insert en alerts.journal es: "+MySQL);
            DirectSQL(Source,MySQL,false);
        }
    }
    

}