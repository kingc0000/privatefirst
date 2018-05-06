/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.Modbus;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class HandRequest extends ModbusRequest {
    

    public HandRequest(String serialNo) {
    	this.serialNo = serialNo;
    }
    
    @Override
    public void validate(Modbus modbus) throws ModbusTransportException {
        
    }

    @Override
    public String toString() {
        return "HandRequest [serialNo=" + serialNo + "]";
    }

	@Override
	ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void readRequest(ByteQueue queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void writeRequest(ByteQueue queue) {
		//queue = new ByteQueue();
		queue.push(serialNo);
	}
	
	public void writeSerialNo(ByteQueue queue) {
		//queue = new ByteQueue();
		queue.push(serialNo);
	}

	@Override
	public byte getFunctionCode() {
		// TODO Auto-generated method stub
		return FunctionCode.HAND_CONNECTION;
	}
	
}
