update alerts.status set TMX_Promote = 25, TMX_TestStatus ='%uid' where Serial in ($selected_rows.Serial) and Severity>0 and TMX_Promote != 25 and NmosCauseType=1 and Agent like 'ADVA' and AlertGroup = 'LNK-DOWN'



//HERRAMIETAS
update alerts.status set TMX_Promote = 25, TMX_TestStatus ='%uid' where Serial in ($selected_rows.Serial) and Severity>0 and TMX_Promote != 25 and NmosCauseType=1 and Agent like 'ADVA' and AlertGroup = 'LNK-DOWN'



ACTULIZACION: 

(update alerts.status set TMX_Promote = 25, TMX_TestStatus ='%uid' where Serial in ($selected_rows.Serial) and Severity>0 and TMX_Promote != 25 and NmosCauseType=1) and ((Agent like 'ADVA' and AlertGroup in ('AIS', 'FLT', 'PSU', 'ALLSYNCREF', 
'CFMFA', 'LAGMF', 'LNKDNCABLEFAULT', 'LNKDNCABLEREMOVED', 'LNK-DOWN', 'LOS', 'PMOVL', 'SFP-TX', 'SFPTXFAULT', 'TRAFFICFAIL', 'EventBufferOverflow', 'Flood',
'SFP-M', 'XFPREMOVED', 'REMOVED', 'SFPMEA', 'SFPREMOVED', 'SFP-RMVD', 'BEXNES', 'CLOCKFAIL', 'DCN', 'IPADDRCONFLICT', 'LNKDNAUTONEGFAILED', 'LNKDNUNISOLATED',
'RXJABBER', 'RX-JABBER')) or (Agent like 'Cisco CPN' and AlertGroup in ('BFD connectivity down', 'BFD neighbour loss', 'BGP link down', 'BGP neighbour loss', 'efp down' ,'lag down',
'layer 2 tunnel down', 'LDP neighbor loss', 'cisco flash device changed', 'all ip interfaces down', 'bgp-process-down', 'link utilization', 'interface status',
'dropped packets', 'tx utilization', 'mpls te tunnel down trap', 'mpls te tunnel down', 'pseudo wire tunnel traps', 'rx utilization', 'cpu utilization',
'mpls ldp session down', 'port down' , 'link down', 'cisco flash device removed', 'cefc power status changed', 'MPLS interface removed', 'members changed',
'chassis alarm trap', 'discard packets', 'env sp 4 fan tray fail', 'mpls te tunnel reoptimized trap', 'pm sp stdby 4 err disable', 'snmp authentication failure',
'Spanning-Tree-Topology-Change-Trap', 'vsi down', 'vtp notification prefix trap')) or (Agent like 'ADSL' and AlertGroup in ('The Memory Usage Is Too High', 
'The Disk Usage Is Too High (Major)', 'The Disk Usage Is Too High (Critical)', 'The User in the SMManagers Group Changes a Users Password', 'The default password for user root is not changed', 
'The ADSL port activation rate fails to reach the rate threshold.', 'EventKeepAlive')) or (Agent like 'Alcatel 5620 SAM' and AlertGroup in ('ReachabilityProblem', 
'BfdSessionDown', 'AccessInterfaceDown', 'InterfaceDown', 'LinkDown', 'InterfaceNeighborDown', 'LspDown', 'LspPathDown', 'NeighborDown', 'OspfInterfaceDown', 'ServiceSiteDown', 
'SdpBindingDown', 'TunnelDown', 'TunnelAdministrativelyDown')) or (Agent like 'Alcatel 5620 SAM CC' and AlertGroup in ('GenericInterfaceLinkDown', 'link down', 'LinkDown', 
'netw.PhysicalLink', 'PollerProblem', 'NodeRebooted', 'HandShaking', 'ReachabilityProblem')) or  (Agent like 'RAD' and AlertGroup in ('Alarm indication signal (ais)', 'link down', 'Loss of frame (lof)', 'Loss of signal (los)', 'Loss of signal (los) at far end (los-fe)', 
'Remote failure indication (rfi)', 'Power supply failure of - 5 Volt', 'Power supply failure of + 5 Volt', 'Power supply failure of 12 Volt', 'Link Up', 'Node Connected', 'Node Disconnected', 'Card not supported or misconfigured', 'Module was inserted', 
'Module was removed')) or  (Agent like '^BM' and AlertGroup in ('Traffic Failure', 'Transport Link Failure', 'Transport Segment Failure', 'Ingress Failure', 'Resource Isolation',
'Traffic Degradation', 'Transport Link Degradation')))











update alerts.status set TMX_Promote = 25, TMX_TestStatus ='%uid' where Serial in ($selected_rows.Serial) and Severity>0 and TMX_Promote != 25 and NmosCauseType=1 and ((Agent like 'ADVA' and AlertGroup in ('AIS', 'FLT', 'PSU', 'ALLSYNCREF', 
'CFMFA', 'LAGMF', 'LNKDNCABLEFAULT', 'LNKDNCABLEREMOVED', 'LNK-DOWN', 'LOS', 'PMOVL', 'SFP-TX', 'SFPTXFAULT', 'TRAFFICFAIL', 'EventBufferOverflow', 'Flood',
'SFP-M', 'XFPREMOVED', 'REMOVED', 'SFPMEA', 'SFPREMOVED', 'SFP-RMVD', 'BEXNES', 'CLOCKFAIL', 'DCN', 'IPADDRCONFLICT', 'LNKDNAUTONEGFAILED', 'LNKDNUNISOLATED',
'RXJABBER', 'RX-JABBER')) or (Agent like 'Cisco CPN' and AlertGroup in ('BFD connectivity down', 'BFD neighbour loss', 'BGP link down', 'BGP neighbour loss', 'efp down' ,'lag down',
'layer 2 tunnel down', 'LDP neighbor loss', 'cisco flash device changed', 'all ip interfaces down', 'bgp-process-down', 'link utilization', 'interface status',
'dropped packets', 'tx utilization', 'mpls te tunnel down trap', 'mpls te tunnel down', 'pseudo wire tunnel traps', 'rx utilization', 'cpu utilization',
'mpls ldp session down', 'port down' , 'link down', 'cisco flash device removed', 'cefc power status changed', 'MPLS interface removed', 'members changed',
'chassis alarm trap', 'discard packets', 'env sp 4 fan tray fail', 'mpls te tunnel reoptimized trap', 'pm sp stdby 4 err disable', 'snmp authentication failure',
'Spanning-Tree-Topology-Change-Trap', 'vsi down', 'vtp notification prefix trap')) or (Agent like 'ADSL' and AlertGroup in ('The Memory Usage Is Too High', 
'The Disk Usage Is Too High (Major)', 'The Disk Usage Is Too High (Critical)', 'The User in the SMManagers Group Changes a Users Password', 'The default password for user root is not changed', 
'The ADSL port activation rate fails to reach the rate threshold.', 'EventKeepAlive')) or (Agent like 'Alcatel 5620 SAM' and AlertGroup in ('ReachabilityProblem', 
'BfdSessionDown', 'AccessInterfaceDown', 'InterfaceDown', 'LinkDown', 'InterfaceNeighborDown', 'LspDown', 'LspPathDown', 'NeighborDown', 'OspfInterfaceDown', 'ServiceSiteDown', 
'SdpBindingDown', 'TunnelDown', 'TunnelAdministrativelyDown')) or (Agent like 'Alcatel 5620 SAM CC' and AlertGroup in ('GenericInterfaceLinkDown', 'link down', 'LinkDown', 
'netw.PhysicalLink', 'PollerProblem', 'NodeRebooted', 'HandShaking', 'ReachabilityProblem')) or  (Agent like 'RAD' and AlertGroup in ('Alarm indication signal (ais)', 'link down', 'Loss of frame (lof)', 'Loss of signal (los)', 'Loss of signal (los) at far end (los-fe)', 
'Remote failure indication (rfi)', 'Power supply failure of - 5 Volt', 'Power supply failure of + 5 Volt', 'Power supply failure of 12 Volt', 'Link Up', 'Node Connected', 'Node Disconnected', 'Card not supported or misconfigured', 'Module was inserted', 
'Module was removed')) or  (Agent like '^BM' and AlertGroup in ('Traffic Failure', 'Transport Link Failure', 'Transport Segment Failure', 'Ingress Failure', 'Resource Isolation',
'Traffic Degradation', 'Transport Link Degradation')))
