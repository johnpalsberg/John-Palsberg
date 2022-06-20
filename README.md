# John-Palsberg
QsimMaster Folder:
  Here is the code for Thomas's Qsim project. It contains both his code and also the general common code from the Campell Group. So far, I have made some Python 2 to Python 3 upgrades and also some import statement fixes. 

# Env Setup

I am using MACOS 10.15.7, conda version 4.7.12.

For purpose of upgrading into python3, we use a conda environment:

* conda create -n labrad python=3.10 twisted numpy pyparsing requests cython
* conda activate labrad

Hand install treedict from https://github.com/hoytak/treedict, using following command:
* python setup.py install --cython


