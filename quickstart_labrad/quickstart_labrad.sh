#!/bin/bash
trap "kill 0" EXIT

export LABRADHOST=localhost
export LABRADPASSWORD=''
export LABRADHOST=localhost
export LABRADHOST=localhost
export LABRADPORT=7682
export LABRAD_TLS=off
export LABRAD_TLS_PORT=7643
bash ../../../capstone2/scalabrad-0.8.3/bin/labrad > /dev/null & #change to your specific relative/absolute path
bash ../../../capstone2/scalabrad-web-server-2.0.6/bin/labrad-web > /dev/null & #change to your specific relative/absolute path
sleep 5 #give labrad manager time to start before starting node
python -m labrad.node
