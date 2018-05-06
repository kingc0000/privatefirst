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
package com.serotonin.modbus4j.ip.xa;

import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.base.BaseRequestHandler;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.HandRequest;
import com.serotonin.modbus4j.msg.HandResponse;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.sero.messaging.IncomingRequestMessage;
import com.serotonin.modbus4j.sero.messaging.OutgoingResponseMessage;

public class HandRequestHandler extends BaseRequestHandler {
    public HandRequestHandler(ModbusSlaveSet slave) {
        super(slave);
    }

    public OutgoingResponseMessage handleRequest(IncomingRequestMessage req) throws Exception {
        HandMessageRequest handRequest = (HandMessageRequest) req;
        ModbusRequest request = handRequest.getModbusRequest();
        ModbusResponse response = handleRequestImpl(request);
        if (response == null)
            return null;
        return new HandMessageResponse(response, handRequest.transactionId);
    }
    @Override
    public ModbusResponse handleRequestImpl(ModbusRequest request) throws ModbusTransportException {
    	HandRequest req = (HandRequest) request;
        // Find the process image to which to send.
//        
//        ByteQueue xaQueue = new ByteQueue();
//        ModbusUtils.pushShort(xaQueue, );
//        ModbusUtils.pushShort(xaQueue, ModbusUtils.HAND_PROTOCOL_ID);
//        ModbusUtils.pushShort(xaQueue, 1);
//        ModbusUtils.pushShort(xaQueue, 128);
    	return new HandResponse(req.getSerialNo());
    }
}
