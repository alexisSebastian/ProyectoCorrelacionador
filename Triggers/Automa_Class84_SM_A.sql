begin
--condiciones para poder ser candidatos a etiqueta con class 84
	for each row incidente in alerts.status where  incidente.Type = 1 and incidente.TMX_EventCategory = 'Excepcion' and incidente.NmosCauseType = 1 and incidente.Class != 84 and incidente.TMX_Promote = 0
	begin
		update alerts.status via incidente.Identifier set Class = 84;

		if(incidente.Class = 84 and incidente.TMX_Promote = 29 or (incidente.Class = 84 and incidente.TMX_Promote = 30) or (incidente.Class = 84 and incidente.TMX_Promote != 0 and incidente.ImpactFlag = 301))
			then
			update alert.status via incidente.Indentifier set Class = 0;
		end if;
	end;
end