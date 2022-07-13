trap "kill 0" EXIT
export PYTHONPATH=../QsimMaster/scripts/experiments/qsimexperiment.py:$PYTHONPATH
python -i quickstart_client.py #run python script, then keep open python shell

