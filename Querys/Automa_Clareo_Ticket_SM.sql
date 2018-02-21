begin
--Cambia a severidad = 0 , los eventos que tengan la leyenda clareo automático y no hayan sido actualizados durante los últimos 5 minutos
	for each row clareo in alerts.status where  
                        clareo.SMS_TicketNumber !='' and clareo.StateChange < (getdate() - 300) and clareo.Type 1 and clareo.Severity>0
	begin
		update alerts.status via clareo.Identifier set Severity = 0 ;
 	end;

end