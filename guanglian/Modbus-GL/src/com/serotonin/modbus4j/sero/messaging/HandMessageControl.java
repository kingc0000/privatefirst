package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.ip.xa.HandMessageRequest;
import com.serotonin.modbus4j.sero.io.StreamUtils;
import com.serotonin.modbus4j.sero.log.BaseIOLog;
import com.serotonin.modbus4j.sero.timer.SystemTimeSource;
import com.serotonin.modbus4j.sero.timer.TimeSource;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * In general there are three messaging activities:
 * <ol>
 * <li>Send a message for which no reply is expected, e.g. a broadcast.</li>
 * <li>Send a message and wait for a response with timeout and retries.</li>
 * <li>Listen for unsolicited requests.</li>
 * </ol>
 * 
 * @author Matthew Lohbihler
 */
public class HandMessageControl implements DataConsumer {
	private final static Log LOG = LogFactory.getLog(HandMessageControl.class);
	private static int DEFAULT_RETRIES = 2;
    private static int DEFAULT_TIMEOUT = 500;

    public boolean DEBUG = false;

    private Transport transport;
    private MessageParser messageParser;
    private RequestHandler requestHandler;
    private WaitingRoomKeyFactory waitingRoomKeyFactory;
    private MessagingExceptionHandler exceptionHandler = new DefaultMessagingExceptionHandler();
    private int retries = DEFAULT_RETRIES;
    private int timeout = DEFAULT_TIMEOUT;
    private int discardDataDelay = 0;
    private long lastDataTimestamp;
    private String serialNo;

    private BaseIOLog ioLog;
    private TimeSource timeSource = new SystemTimeSource();

    private final WaitingRoom waitingRoom = new WaitingRoom();
    private final ByteQueue dataBuffer = new ByteQueue();

    public void start(Transport transport, MessageParser messageParser, RequestHandler handler,
            WaitingRoomKeyFactory waitingRoomKeyFactory) throws IOException {
        this.transport = transport;
        this.messageParser = messageParser;
        this.requestHandler = handler;
        this.waitingRoomKeyFactory = waitingRoomKeyFactory;
        waitingRoom.setKeyFactory(waitingRoomKeyFactory);
        transport.setConsumer(this);
    }

    public void close() {
        transport.removeConsumer();
    }

    public void setExceptionHandler(MessagingExceptionHandler exceptionHandler) {
        if (exceptionHandler == null)
            this.exceptionHandler = new DefaultMessagingExceptionHandler();
        else
            this.exceptionHandler = exceptionHandler;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDiscardDataDelay() {
        return discardDataDelay;
    }

    public void setDiscardDataDelay(int discardDataDelay) {
        this.discardDataDelay = discardDataDelay;
    }

    public BaseIOLog getIoLog() {
        return ioLog;
    }

    public void setIoLog(BaseIOLog ioLog) {
        this.ioLog = ioLog;
    }

    public TimeSource getTimeSource() {
        return timeSource;
    }

    public void setTimeSource(TimeSource timeSource) {
        this.timeSource = timeSource;
    }

    public IncomingResponseMessage send(OutgoingRequestMessage request) throws IOException {
        return send(request, timeout, retries);
    }

    public IncomingResponseMessage send(OutgoingRequestMessage request, int timeout, int retries) throws IOException {
        byte[] data = request.getMessageData();
        LOG.debug("MessagingControl.send: " + StreamUtils.dumpHex(data));
        IncomingResponseMessage response = null;

        if (request.expectsResponse()) {
            WaitingRoomKey key = waitingRoomKeyFactory.createWaitingRoomKey(request);

            // Enter the waiting room
            waitingRoom.enter(key);

            try {
                do {
                    // Send the request.
                    write(data);

                    // Wait for the response.
                    response = waitingRoom.getResponse(key, timeout);

                    if (DEBUG && response == null)
                        System.out.println("Timeout waiting for response");
                }
                while (response == null && retries-- > 0);
            }
            finally {
                // Leave the waiting room.
                waitingRoom.leave(key);
            }

            if (response == null)
                throw new TimeoutException("request=" + request);
        }
        else
            write(data);

        return response;
    }

    public void send(OutgoingResponseMessage response) throws IOException {
    	LOG.info("HandMessagingConnection.response.send: " + StreamUtils.dumpHex(response.getMessageData()));
    	write(response.getMessageData());
    }

    /**
     * Incoming data from the transport. Single-threaded.
     */
    public void data(byte[] b, int len) {
        LOG.debug("HandMessagingConnection.read: " + StreamUtils.dumpHex(b, 0, len));
        if (ioLog != null)
            ioLog.input(b, 0, len);

        if (discardDataDelay > 0) {
            long now = timeSource.currentTimeMillis();
            if (now - lastDataTimestamp > discardDataDelay)
                dataBuffer.clear();
            lastDataTimestamp = now;
        }

        dataBuffer.push(b, 0, len);
        

        //握手包只接收一次数据读取，因此不用while方式一直等待监听
        // Attempt to parse a message.
        try {
            // Mark where we are in the buffer. The entire message may not be in yet, but since the parser
            // will consume the buffer we need to be able to backtrack.
            dataBuffer.mark();
            HandMessageRequest message = null;
            try {
            	message = (HandMessageRequest) messageParser.parseMessage(dataBuffer); //HandMessageRequest
            } catch (Exception e) {
            	LOG.error("解析握手请求包出错");
            	return;
            }
            
            if (message == null) {
                // Nothing to do. Reset the buffer and exit the loop.
                dataBuffer.reset();
            } else {
            	this.serialNo = message.getModbusRequest().getSerialNo(); //设置serialNo
            }

            if (message instanceof IncomingRequestMessage) {
                // Received a request. Give it to the request handler
                if (requestHandler != null) {
                    OutgoingResponseMessage response = requestHandler
                            .handleRequest((IncomingRequestMessage) message);

                    if (response != null)
                        send(response);
                }
            } 
        }
        catch (Exception e) {
            exceptionHandler.receivedException(e);
//             Clear the buffer
            dataBuffer.clear();
        }
       /* // There may be multiple messages in the data, so enter a loop.
        while (true) {
            // Attempt to parse a message.
            try {
                // Mark where we are in the buffer. The entire message may not be in yet, but since the parser
                // will consume the buffer we need to be able to backtrack.
                dataBuffer.mark();

                HandMessageRequest message = (HandMessageRequest) messageParser.parseMessage(dataBuffer); //HandMessageRequest
                
                if (message == null) {
                    // Nothing to do. Reset the buffer and exit the loop.
                    dataBuffer.reset();
                    break;
                } else {
                	this.serialNo = message.getModbusRequest().getSerialNo(); //设置serialNo
                }

                if (message instanceof IncomingRequestMessage) {
                    // Received a request. Give it to the request handler
                    if (requestHandler != null) {
                        OutgoingResponseMessage response = requestHandler
                                .handleRequest((IncomingRequestMessage) message);

                        if (response != null)
                            send(response);
                    }
                }
                else
                    // Must be a response. Give it to the waiting room.
                    waitingRoom.response((IncomingResponseMessage) message);
            }
            catch (Exception e) {
                exceptionHandler.receivedException(e);
                // Clear the buffer
                //                dataBuffer.clear();
            }
        }*/
    }

    private void write(byte[] data) throws IOException {
        if (ioLog != null)
            ioLog.output(data);

        synchronized (transport) {
            transport.write(data);
        }
    }

    public void handleIOException(IOException e) {
        exceptionHandler.receivedException(e);
    }

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
    
}
