#!/bin/sh

export LABRADHOST=localhost
export LABRADPASSWORD=testpass
export LABRADPORT=7777

# start labrad manager
labrad 1>.labrad.log 2>.labrad.err.log &
sleep 20

# run the tests
pytest -v .
STATUS=$?

echo "=== .labrad.log ===" && cat .labrad.log && echo

exit $STATUS
