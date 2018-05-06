package com.serotonin.modbus4j.ip.xa;

import com.serotonin.modbus4j.msg.ModbusMessage;
import com.serotonin.modbus4j.sero.messaging.IncomingResponseMessage;
import com.serotonin.modbus4j.sero.messaging.OutgoingRequestMessage;
import com.serotonin.modbus4j.sero.messaging.WaitingRoomKey;
import com.serotonin.modbus4j.sero.messaging.WaitingRoomKeyFactory;

public class HandWaitingRoomKeyFactory implements WaitingRoomKeyFactory {
    @Override
    public WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request) {
        return createWaitingRoomKey((XaMessage) request);
    }

    @Override
    public WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response) {
        return createWaitingRoomKey((XaMessage) response);
    }

    public WaitingRoomKey createWaitingRoomKey(XaMessage msg) {
        return new XaWaitingRoomKey(msg.getTransactionId(), msg.getModbusMessage());
    }

    class XaWaitingRoomKey implements WaitingRoomKey {
        private final int transactionId;
        private final String serialNo;
        private final byte functionCode;

        public XaWaitingRoomKey(int transactionId, ModbusMessage msg) {
            this.transactionId = transactionId;
            this.serialNo = msg.getSerialNo();
            this.functionCode = msg.getFunctionCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            XaWaitingRoomKey other = (XaWaitingRoomKey) obj;
            if (functionCode != other.functionCode)
                return false;
            if (!serialNo.equals(other.serialNo))
                return false;
            if (transactionId != other.transactionId)
                return false;
            return true;
        }
    }
}
