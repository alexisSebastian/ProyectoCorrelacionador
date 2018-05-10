begin
--condiciones para poder ser candidatos a etiqueta con class 84
	for each row incidente in alerts.status where  incidente.Type = 1 and incidente.TMX_EventCategory = 'Excepcion' and incidente.NmosCauseType = 1 and incidente.Class != 84
	begin
		update alerts.status via incidente.Identifier set Class = 84;
	end;
end