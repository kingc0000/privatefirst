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

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.HandRequest;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.sero.io.StreamUtils;
import com.serotonin.modbus4j.sero.messaging.IncomingRequestMessage;
import com.serotonin.modbus4j.sero.messaging.OutgoingRequestMessage;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;
/**
 * 握手请求
 * @author sam
 *
 */
public class HandMessageRequest extends XaMessage implements OutgoingRequestMessage, IncomingRequestMessage {
	private final static Log LOG = LogFactory.getLog(HandMessageRequest.class);
	
    static HandMessageRequest createXaMessageRequest(ByteQueue queue) throws ModbusTransportException {
        // Remove the XA header
        int transactionId = ModbusUtils.popShort(queue);
        int protocolId = ModbusUtils.popShort(queue);
        if (protocolId != ModbusUtils.HAND_PROTOCOL_ID)
            throw new ModbusTransportException("Unsupported Hand protocol id: " + protocolId);

        int length = ModbusUtils.popShort(queue); // Length, which we don't care about.
//        StreamUtils.dumpHex(b)
		String serialNo = queue.popString(length, Charset.forName("utf-8"));
		LOG.info("received gatway which serialno is "+serialNo);
		// Create the modbus response.
		HandRequest request = new HandRequest(serialNo);
        return new HandMessageRequest(request, transactionId);
    }

    public HandMessageRequest(ModbusRequest modbusRequest, int transactionId) {
        super(modbusRequest, transactionId);
    }
    @Override
    public byte[] getMessageData() {
        ByteQueue msgQueue = new ByteQueue();

        // Write the particular message.
        ((HandRequest)modbusMessage).writeSerialNo(msgQueue);
        
        // Create the XA message
        ByteQueue xaQueue = new ByteQueue();
        ModbusUtils.pushShort(xaQueue, transactionId);
        ModbusUtils.pushShort(xaQueue, ModbusUtils.HAND_PROTOCOL_ID);
        ModbusUtils.pushShort(xaQueue, msgQueue.size());
        xaQueue.push(msgQueue);

        // Return the data.
        return xaQueue.popAll();
    }

    @Override
    public boolean expectsResponse() {
        return  StringUtils.isNotBlank(((HandRequest)modbusMessage).getSerialNo());
    }

    public ModbusRequest getModbusRequest() {
        return (ModbusRequest) modbusMessage;
    }
}
