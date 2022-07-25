"""
### BEGIN NODE INFO
[info]
name = CS Piezo Server with Simulation
version = 1.0.0
description = Communicates with the AMO3 box for control of piezo voltages. Allows for simulated mode.
instancename = CSPiezoServerSim

[startup]
cmdline = %PYTHON% %FILE%
timeout = 20

[shutdown]
message = 987654321
timeout = 20
### END NODE INFO
"""
import os,sys

from labrad.units import WithUnit
from labrad.server import setting, Signal, inlineCallbacks


from twisted.internet.defer import returnValue
sys.path.append('/Users/landonmiller/Desktop/GitHub/John-Palsberg/QsimMaster/scripts/experiments/qsim_example_experiment/EGGS_labrad/servers')
from serial.cs import csserialdeviceserverwithsimulation

TERMINATOR = '\r\n'


class CSPiezoServerSim(CSSerialDeviceServerSim):
    """
    Communicates with the AMO3 box for control of piezo voltages.
    """
    simulated=True
    name = 'CS Piezo Server with Simulation'
    regKey = 'CSPiezoServerSim'
    serNode = 'penny'
    port = 'COM3'

    timeout = WithUnit(3.0, 's')
    baudrate = 38400


    # SIGNALS
    voltage_update = Signal(999999, 'signal: voltage update', '(iv)')

    simulator_voltages=[0,0,0,0]
    simulated_remote_accessible=1
    simulated_channel_power=[1,1,1,1]
    
    def voltage_helper(channel, voltage=None):
        if channel not in (0, 1, 2, 3):
            raise Exception("Error: channel must be one of (0, 1, 2, 3).")
        if voltage is not None:
            if (voltage < 0) or (voltage > 150):
                raise Exception("Error: voltage must be in [0, 150].")
            yield self.ser.acquire()
            if self.simulated:
                self.simulator_voltages[channel]=voltage
            else:
                yield self.ser.write('vout.w {:d} {:3f}\r\n'.format(channel, voltage))
                yield self.ser.read_line('\n')
            self.ser.release()
        # getter
        yield self.ser.acquire()
        if not self.simulated:
            yield self.ser.write('vout.r {:d}\r\n'.format(channel))
            resp = yield self.ser.read_line('\n')
        self.ser.release()
        if self.simulated:
            return self.simulator_voltages[channel]
        else:
            return float(resp)
            
    # GENERAL
    @setting(100, 'device_info')
    def device_info(self, c):
        output = 'id?'
        yield self.ser.acquire()
        yield self.ser.write(output)
        device_type = yield self.ser.read_line('\n')
        device_id = yield self.ser.read_line('\n')
        hardware_id = yield self.ser.read_line('\n')
        firmware = yield self.ser.read_line('\n')
        self.ser.release()
        returnValue([device_type, device_id, hardware_id, firmware])
        
    @setting(12, 'Remote', state = 'i')
    def remote_mode(self, c, state=2):
        """
        Set remote mode of device.
        Arguments:
            remote_status   (bool)  : whether the device accepts serial commands
        Returns:
                            (bool)  : whether the device accepts serial commands
        """
        if state!=2:
            yield self.ser.acquire()
            if self.simulated:
                self.simulated_remote_accessible=state
            else:
                yield self.ser.write('remote.w {:d}\r\n'.format(state))
                yield self.ser.read_line('\n')
            self.ser.release()
        # getter
        yield self.ser.acquire()
        if not self.simulated:
            yield self.ser.write('remote.r\r\n')
            resp = yield self.ser.read_line('\n')
        self.ser.release()
        if self.simulated:
            returnValue(str(self.simulated_remote_accessible))
        else:
            if resp.strip() == 'enabled':
                returnValue("1")
            else:
                returnValue("0")
    
    
        


    # ON/OFF
    @setting(111, 'Toggle', channel='i', state='i', returns='i')
    def toggle(self, c, channel, state=2):
        """
        Set a channel to be on or off.
        Args:
            channel (int)   : the channel to read/write.
            power   (bool)  : whether channel is to be on or off
        Returns:
                    (bool)  : result
        """
        if channel not in (0, 1, 2, 3):
            raise Exception("Error: channel must be one of (0, 1, 2, 3).")
        if state != 2:
            yield self.ser.acquire()
            if self.simulated:
                self.simulated_channel_power[channel]=state
            else:
                yield self.ser.write('out.w {:d} {:d}\r\n'.format(channel, state))
                yield self.ser.read_line('\n')
            self.ser.release()
        # getter
        yield self.ser.acquire()
        if not self.simulated:
            yield self.ser.write('out.r {:d}\r\n'.format(channel))
            resp = yield self.ser.read_line('\n')
        self.ser.release()
        if self.simulated:
            returnValue(str(self.simulated_channel_power[channel]))
        else:
            if resp.strip() == 'enabled':
                returnValue("1")
            else:
                returnValue("0")


    
    
    
        
    @setting(211, 'Voltage', channel='i', voltage='v', returns='v')
    def voltage(self, c, channel, voltage=None):
        '''
        Sets/get the channel voltage.
        Arguments:
            channel (int)   : the channel to read or write a voltage to
            voltage (float) : the channel voltage to set
        Returns:
                    (float) : the channel voltage
        '''
        returnValue(str(self.voltage_helper(channel,voltage)))
        
        
    @setting(311, 'GetVoltage', channel='i', returns='v')
    def get_voltage(self,c,channel):
        '''
        Gets the channel voltage.
        Arguments:
            channel (int)   : the channel to read
        Returns:
                    (float) : the channel voltage
        '''
        returnValue(str(self.voltage_helper(channel,None)))
        
    @setting(411, 'SetVoltage', channel='i', voltage='v', returns='v')
    def set_voltage(self,c,channel,voltage):
        '''
        Sets the channel voltage.
        Arguments:
            channel (int)   : the channel to read/write
            voltage (float) : the channel voltage to set
        Returns:
                    (float) : the channel voltage
        '''
        self.voltage_helper(channel,voltage)
              
              
    
        
        
    


if __name__ == '__main__':
    from labrad import util
    util.runServer(CSPiezoServerSim())
