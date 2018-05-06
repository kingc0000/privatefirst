package com.kekeinfo.modbus.listener;

import java.io.InputStream;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.base.DataModal;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpHandSlave;

public class ListenerTest {
    static Random random = new Random();
    static float ir1Value = -100;
    private static final Log LOG = LogFactory.getLog(ListenerTest.class);
//    private static final Logger LOG = LoggerFactory.getLogger(ListenerTest.class);

    public static void main(String[] args) throws Exception {
        // SerialParameters params = new SerialParameters();
        // params.setCommPortId("COM1");
        // params.setPortOwnerName("dufus");
        // params.setBaudRate(9600);
    	InputStream inputStream=ListenerTest.class.getResourceAsStream("/log4j.properties");
        PropertyConfigurator.configure(inputStream);
        
        IpParameters params = new IpParameters();
        params.setHost("localhost");
        params.setPort(5000);

        ModbusFactory modbusFactory = new ModbusFactory();
        // ModbusListener listener = modbusFactory.createRtuListener(processImage, 31, params, false);
        // ModbusListener listener = modbusFactory.createAsciiListener(processImage, 31, params);
//        final ModbusMaster listener = modbusFactory.createTcpListener(params);
        final TcpHandSlave listener = (TcpHandSlave) modbusFactory.createTcpHandSlave(5000, false);
        // ModbusSlave listener = modbusFactory.createUdpSlave(processImage, 31);

        // When the "listener" is started it will use the current thread to run. So, if an exception is not thrown
        // (and we hope it won't be), the method call will not return. Therefore, we start the listener in a separate
        // thread so that we can use this thread to modify the values.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                	LOG.debug("waiting for connection");
                	listener.start();
                }
                catch (ModbusInitException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        Thread.sleep(30000);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
            	while (true) {
        			listener.getDatas();
        			try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
        		}
            }
        }).start();*/
        new Thread(new Runnable() {
            @Override
            public void run() {
            	while (true) {
            		DataModal data = new DataModal();
    				data.setBusinessId(1L);
    				data.setBusinessName("ceshi");
    				data.setSerialNo("1100201704241060");
    				data.setNode1(2);
    				data.setNode2(2);
    				data.setChannel1(0);
    				data.setChannel2(1);
    				BatchResults<String> result = listener.getDatas(data);
    				if (result!=null) {
//						Integer d1 = result.getIntValue("node1");
//						Integer d2 = result.getIntValue("node2");
//						System.out.println("value1="+d1);
//						System.out.println("value2="+d2);
						System.out.println("node1.value="+result.getValue("node1"));
						System.out.println("node2.value="+result.getValue("node2"));
    				}
        			try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
        		}
            }
        }).start();
    }
}
