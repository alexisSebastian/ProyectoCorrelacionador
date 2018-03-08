log("NetcoolDTCMDB_");

clli = @TMX_NodeName;
serial = @Serial;
//serial = 90331497;
identificador = @Identifier;
usr=0;
fch=getdate();
repisa = "0" + @TMX_ShelfNumber; 
puerto = @PhysicalPort;
//slot = "0" +  @PhysicalSlot;
//subSlot = "0" + @TMX_SubInterface;

if (@PhysicalSlot > 9) {
    slot = @PhysicalSlot;
}else{
    slot = "0" +  @PhysicalSlot;
}

if (@TMX_SubInterface > 9) {
    subSlot = @TMX_SubInterface;
}else{
    subSlot = "0" + @TMX_SubInterface;
}

tarjeta = @PhysicalCard;
Source="defaultobjectserver";
SoureceB = "CNSBADP";
SourceD = "CNSDESA";


log("\n"+"CLLI ES: "+ @TMX_NodeName+"\n" + "El SERIAL ES:"+@Serial+"\nLa repisa es: "+@TMX_ShelfNumber
+"\nEl puerto es: "+ puerto + "\nEl slot es: "+ @PhysicalSlot +"\nEl SubSlot es :"+@TMX_SubInterface
+"\nLa Tarjeta es:"+@PhysicalCard +"\nEl servidor es: "+ @ServerName);


log("Iniciar política 'NetcoolDTCMDB_'...");
    //Especifique el nombre de paquete tal como se ha definido al compilar WSDL en Impact
    WSSetDefaultPKGName('NetcoolDTCMDB');

    //Especificar parámetros
    RetrieveNetcoolDTCMDBRequestDocument=WSNewObject("com.hp.schemas.sm._7.RetrieveNetcoolDTCMDBRequestDocument");
    _RetrieveNetcoolDTCMDBRequest=WSNewSubObject(RetrieveNetcoolDTCMDBRequestDocument,"RetrieveNetcoolDTCMDBRequest");


    _Model = WSNewSubObject(_RetrieveNetcoolDTCMDBRequest,"Model");

    _Keys = WSNewSubObject(_Model,"Keys"); 
    if (Length(clli) == 11) {
        _Keys['Query'] = 'display.name="'+clli+'"'; //EQUIPO
        log("El Equipo es: " + _Keys);

    }elseif(Length(clli) > 11 && tarjeta > '0' && tarjeta != ''){ 
        _Keys['Query'] = 'display.name= "'+clli+''+'/'+''+repisa+''+'/'+slot+''+'/'+subSlot+'"'; //TARJETA
        log("La Tarjeta es: " + _Keys);
    }elseif(Length(clli) > 11 && puerto > 0){
        _Keys['Query'] = 'display.name= "'+clli+''+'/'+''+repisa+''+'/'+slot+''+'/'+subSlot+''+'/'+puerto+'"'; //PUERTO
        log("El Puerto es: " + _Keys);
    }
    _CIID = WSNewSubObject(_Keys,"CIID");

    _Instance = WSNewSubObject(_Model,"Instance");

    _FileDevice = WSNewSubObject(_Instance,"FileDevice");

    _CIID = WSNewSubObject(_FileDevice,"CIID");


    WSParams = {RetrieveNetcoolDTCMDBRequestDocument};
    log("DocumentoRequest: " + WSParams);
 
    //Especifique un nombre de servicio web, un punto final y un método
    WSService = 'NetcoolDTCMDB';
    WSEndPoint = 'http://qcalswpr:13083/SM/7/ws';
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
    log("TERMINA LA LLAMADA DEL WS");

    log("Fin de la politica NetcoolDTCMDB_A");

    //Se extrae del resultado del ws en WSInvokeDLResult los valores CIID, estatus, subred, tipoRed
    CIId= RExtract(WSInvokeDLResult,".*<CIID type=.*>(.*)</CIID>.*"); //de esta forma se obtiene el campo de la respuesta del webservuce
    estatus = RExtract(WSInvokeDLResult,".*<estatus type=.*>(.*)</estatus>.*");
    subRed = RExtract(WSInvokeDLResult,".*<subred type=.*>(.*)</subred>.*");
    tipoRed = RExtract(WSInvokeDLResult,".*<tipoRed type=.*>(.*)</tipoRed>.*");
    modelo = RExtract(WSInvokeDLResult,".*<modelo type=.*>(.*)</modelo>.*");
    elemetRed = RExtract(WSInvokeDLResult,".*<elementoRed type=.*>(.*)</elementoRed>.*");
    returnCode = RExtract(WSInvokeDLResult,'.*returnCode="([0-9]).*'); 
    log("CIID: "+CIId+"\n"+"ESTATUS: "+estatus+"\n"+"SubRed: "+subRed+"\n"+"TIPO RED: "+tipoRed+"\n"+"EL MODELO ES: "+ modelo+"\n"+"EL ELEMENTO DE RED ES: "+elemetRed);

    //Validaciones
    if(CIId = NULL){CIId = "";}
    if(estatus = NULL){estatus = "";}
    if(subRed = NULL){subRed = "";}
    if(tipoRed = NULL){tipoRed = "";}
    if(modelo = NULL){modelo = "";}
    if(elemetRed = NULL){elemetRed = "";}

    if ((CIId != "" && elemetRed != "" && estatus != "")|| (elementRed = "Equipo" && estatus = "GESTIONADO") || (elementRed = "Enlaces" && estatus = "GESTIONADO") || (elementRed = "Servicio" && estatus = "LIBERADO") || (elementRed = "Servicio Cliente Final" && estatus = "En operacion") || (elementRed = "Puerto" && estatus = "OCUPADO") || (elementRed = "Tarjeta" && estatus = "GESTIONADO")) 
    {
        Filter1="Serial="+serial; //Este es el serial del evento
        log ("El serial del evento es: " +Filter1);
        UpdateExpression="CMDB_Logical_Name='"+CIId+"',CMDB_Istatus='"+estatus+"',CMDB_Subred='"+subRed+"',CMDB_Tipo_Red='"+tipoRed+"',CMDB_Model='"+modelo+"',CMDB_Topologia='"+elemetRed+"',TMX_Promote = 26, ImpactFlag=200";
        log("VALOR DE TMX_PROMOTE ANTES DE ACTUALIZAR: " + @TMX_Promote);
        log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
        BatchUpdate('data', Filter1, UpdateExpression); //BatchUpdate sirve para actualizar el campo en el evento

        //ACTUALIZACION EN BITACORA
        msj= fch +"|" + "Evento: " + serial + "|" + "Enriquece evento" + "|" + "El CLLI se encontro en Service Manager";
        MyKey = serial +":"+ usr +":"+ fch;
        MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
        log ("El Query del Insert en alerts.journal es: "+MySQL);
        DirectSQL(Source,MySQL,false);
    } elseif ((CIId = "" && elemetRed = "" && estatus = "")|| (elementRed = "Equipo" && estatus != "GESTIONADO") || (elementRed = "Enlaces" && estatus != "GESTIONADO") || (elementRed = "Servicio" && estatus != "LIBERADO") || (elementRed = "Servicio Cliente Final" && estatus != "En operacion") || (elementRed = "Puerto" && estatus != "OCUPADO") || (elementRed = "Tarjeta" && estatus != "GESTIONADO"))
    {
        if (returnCode == 9) {
            //Se actualiza en la tabla alerts.status
            Filter1="Serial="+serial;
            log ("El serial del evento es: " +Filter1);
            UpdateExpression="ImpactFlag=201, TMX_Promote = 0";
            log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
            BatchUpdate('data', Filter1, UpdateExpression);

            //Se actualiza en la tabla alerts.jorunal
            msj= fch +"|" + "Evento: " + serial + "|" + "No se encontro informacion" + "|" + "Sin informacion en la fuente de enriquecimiento";
            MyKey = serial +":"+ usr +":"+ fch;
            MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
            DirectSQL(Source,MySQL,false);
            log ("El Query del Insert en alerts.journal es: "+MySQL);
        }elseif(returnCode == 0){
            Filter1="Serial="+serial;
            log ("El serial del evento es: " +Filter1);
            UpdateExpression="ImpactFlag=201, TMX_Promote = 0,CMDB_Logical_Name='"+CIId+"',CMDB_Istatus='"+estatus+"',CMDB_Subred='"+subRed+"',CMDB_Tipo_Red='"+tipoRed+"',CMDB_Model='"+modelo+"',CMDB_Topologia='"+elemetRed+"'";
            log ("El UPDATE EXPRESSION ES: "+UpdateExpression);
            BatchUpdate('data', Filter1, UpdateExpression);


            msj= fch +"|" + "Evento: " + serial + "|" + "Enriquece evento" + "|" + "Informacion incompleta en la fuente de enriquecimiento";
            MyKey = serial +":"+ usr +":"+ fch;
            MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
            log ("El Query del Insert en alerts.journal es: "+MySQL);
            DirectSQL(Source,MySQL,false);
        }
    }