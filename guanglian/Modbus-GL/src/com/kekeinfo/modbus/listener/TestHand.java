/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.kekeinfo.modbus.listener;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpHandMaster;
import com.serotonin.modbus4j.msg.HandRequest;
import com.serotonin.modbus4j.sero.util.DigitalTrans;

/**
 * @author Matthew Lohbihler
 */
public class TestHand {
    public static void main(String[] args) throws Exception {
        IpParameters params = new IpParameters();
        params.setHost("54.223.80.221");
//        params.setHost("localhost");
//        params.setPort(502);
        params.setPort(5000);
        ModbusMaster master = new ModbusFactory().createTcpHandMaster(params, true);
        master.init();
        
        TcpHandMaster tcp = (TcpHandMaster) master;
        tcp.send(new HandRequest(DigitalTrans.StringToAsciiString("1100201704241060")));
        tcp.destroy();
    }
}
