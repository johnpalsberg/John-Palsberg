"""
### BEGIN NODE INFO
[info]
name = CS Piezo Server No Simulation
version = 1.0.0
description = Communicates with the AMO3 box for control of piezo voltages. Doesn't allow for simulated mode.
instancename = CSPiezoServerNoSim

[startup]
cmdline = %PYTHON% %FILE%
timeout = 20

[shutdown]
message = 987654321
timeout = 20
### END NODE INFO
"""

from labrad.units import WithUnit
from labrad.server import setting, Signal, inlineCallbacks

from twisted.internet.defer import returnValue
from EGGS_labrad.servers import SerialDeviceServer

TERMINATOR = '\r\n'


class CSPiezoServerSim(SerialDeviceServer):
    """
    Communicates with the AMO3 box for control of piezo voltages.
    """

    name = 'CS Piezo Server with No Simulation'
    regKey = 'CSPiezoServerNoSim'
    serNode = 'penny'
    port = 'COM3'

    timeout = WithUnit(3.0, 's')
    baudrate = 38400


    # SIGNALS
    voltage_update = Signal(999999, 'signal: voltage update', '(iv)')

    
    def voltage_helper(channel, voltage=None):
        if channel not in (0, 1, 2, 3):
            raise Exception("Error: channel must be one of (0, 1, 2, 3).")
        if voltage is not None:
            if (voltage < 0) or (voltage > 150):
                raise Exception("Error: voltage must be in [0, 150].")
            yield self.ser.acquire()
           
            yield self.ser.write('vout.w {:d} {:3f}\r\n'.format(channel, voltage))
            yield self.ser.read_line('\n')
            self.ser.release()
        # getter
        yield self.ser.acquire()
        
        yield self.ser.write('vout.r {:d}\r\n'.format(channel))
        resp = yield self.ser.read_line('\n')
        self.ser.release()
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
            yield self.ser.write('remote.w {:d}\r\n'.format(state))
            yield self.ser.read_line('\n')
            self.ser.release()
        # getter
        yield self.ser.acquire()
       
        yield self.ser.write('remote.r\r\n')
        resp = yield self.ser.read_line('\n')
        self.ser.release()
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
            yield self.ser.write('out.w {:d} {:d}\r\n'.format(channel, state))
            yield self.ser.read_line('\n')
            self.ser.release()
        # getter
        yield self.ser.acquire()
        
        yield self.ser.write('out.r {:d}\r\n'.format(channel))
        resp = yield self.ser.read_line('\n')
        self.ser.release()
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
    util.runServer(CSPiezoServerNoSim())
