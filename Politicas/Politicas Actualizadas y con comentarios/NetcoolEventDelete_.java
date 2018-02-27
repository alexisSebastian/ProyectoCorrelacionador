log("NetcoolEventDelete");

serial = @Serial;
//serial = 90924759;



log("Inicio de la politica NetcoolEventDelete");

  Filter = "Serial="+serial;
  BatchDelete('data', Filter, null);

log(BatchDelete); 
log("Fin de la politica NetcoolEventDelete");  
  