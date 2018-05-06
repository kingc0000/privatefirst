package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class HandResponse extends ModbusResponse {

    public HandResponse(String serialNo) {
    	this.serialNo = serialNo;
    }
    @Override
    public byte getFunctionCode() {
        return FunctionCode.HAND_CONNECTION;
    }
    
    @Override
    public String toString() {
    	return "HandResponse [serialNo=" + serialNo + "]";
    }
	@Override
	protected void writeResponse(ByteQueue queue) {
		// TODO Auto-generated method stub
		queue.push(serialNo);
	}
	@Override
	protected void readResponse(ByteQueue queue) {
		// TODO Auto-generated method stub
		
	}
}
