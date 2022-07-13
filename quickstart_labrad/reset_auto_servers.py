import labrad

cxn=labrad.connection()
reg=cxn.registry
reg.cd('Nodes')
reg.cd('landons-macbook-pro.local') #change the name of your client, as can be observed either in gui or by checking contents of Nodes directory
reg.set('directories',['/Users/landonmiller/Desktop/GitHub/John-Palsberg/QsimMaster/scripts/experiments/qsim_example_experiment/common/lib/servers','/Users/landonmiller/Desktop/GitHub/John-Palsberg/QsimMaster/scripts/experiments/qsim_example_experiment/EGGS_labrad/servers/data_vault_complete']) #change to your path to John-Paslberg
reg.set('autostart',['ParameterVault','ScriptScanner']) #change to whichever you want to start every time (not as important when using gui, as any server found when scanning directories can easily be started with one click)



#stuff below here not really needed, this is for new nodes that don't have a directory in Nodes but your computer already has one.
reg.cd('..')
reg.cd('__default__') #change the name of your client, as can be observed either in gui or by checking contents of Nodes directory
reg.set('directories',['/Users/landonmiller/Desktop/GitHub/John-Palsberg/QsimMaster/scripts/experiments/qsim_example_experiment/common/lib/servers','/Users/landonmiller/Desktop/GitHub/John-Palsberg/QsimMaster/scripts/experiments/qsim_example_experiment/EGGS_labrad/servers/data_vault_complete']) #anything other than data vault from EGGS?
reg.set('autostart',['ParameterVault','ScriptScanner']) #change to whichever you want to start every time




