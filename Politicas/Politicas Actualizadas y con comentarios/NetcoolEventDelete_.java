log("NetcoolEventDelete");

serial = @Serial;
Source="defaultobjectserver";
//serial = 90924759;

/**
 * TODO:
 * Validar la incercion de mensajes en bitacora antes de eliminar
 * evento en consola
 * 
 */


//Se actualiza en la tabla alerts.jorunal
msj= fch +"|" + "Evento: " + serial + "|" + "Eliminacion de evento" + "|" + "Se realizo la eliminación del evento";
MyKey = serial +":"+ usr +":"+ fch;
MySQL = "insert into alerts.journal (KeyField,Serial,UID,Chrono,Text1) values('"+ MyKey +"',"+ serial +","+ usr +","+ fch + ",'"+msj+"')";
log("Query a ejecutar en el journal: " + MySQL);
DirectSQL(Source,MySQL,false);


log("Inicio de la politica NetcoolEventDelete");

  Filter = "Serial="+serial;
  BatchDelete('data', Filter, null);

log(BatchDelete); 
log("Fin de la politica NetcoolEventDelete");  
  