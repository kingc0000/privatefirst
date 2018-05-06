package com.serotonin.modbus4j.ip.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.sero.io.StreamUtils;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

class Task implements Runnable {

	private final Log LOG = LogFactory.getLog(Task.class);
	private Socket socket;
	private TcpSlaveServer server;
	private int readDelay = 50;
	private final ByteQueue dataBuffer = new ByteQueue();

	public Task(Socket socket, TcpSlaveServer server) {
		this.socket = socket;
		this.server = server;
	}

	public void run() {
		try {
			handleSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跟客户端Socket进行通信
	 * 
	 * @throws Exception
	 */
	private void handleSocket() throws Exception {
		InputStream in = socket.getInputStream();
		boolean running = true;
		byte[] buf = new byte[1024];
        int len;
        try {
            while (running) {
                try {
                    if (in.available() == 0) {
                        synchronized (this) {
                            try {
                                wait(readDelay);
                            }
                            catch (InterruptedException e) {
                            }
                        }
                        continue;
                    }

                    len = in.read(buf); //读取数据
                    String data = StreamUtils.dumpHex(buf, 0, len);
                    LOG.debug("Task.read: " + data);
                    dataBuffer.push(buf, 0, len);
					int transactionId = ModbusUtils.popShort(dataBuffer);
                    int protocolId = ModbusUtils.popShort(dataBuffer);
                    if (protocolId != ModbusUtils.HAND_PROTOCOL_ID) {
                    	LOG.error("protocolId="+protocolId+" who is not hand_protocol_id, abandon request data");
                    	running = false;
                    } else {
                    	int length = ModbusUtils.popShort(dataBuffer); // Length, which we don't care about.
                    	String serialNo = dataBuffer.popString(length, Charset.forName("utf-8"));
                    	LOG.debug("received gatway which serialno is "+serialNo);
                    	synchronized (server.getMapConnections()) {
							server.getMapConnections().put(serialNo, socket);
						}
                    	
                    	//处理响应
                    	ByteQueue msgQueue = new ByteQueue();
                        // Write the particular message.
                        msgQueue.push(128);
                        // Create the XA message
                        ByteQueue xaQueue = new ByteQueue();
                        ModbusUtils.pushShort(xaQueue, transactionId);
                        ModbusUtils.pushShort(xaQueue, ModbusUtils.HAND_PROTOCOL_ID);
                        ModbusUtils.pushShort(xaQueue, msgQueue.size());
                        xaQueue.push(msgQueue);
                        // Return the data.
                        byte[] result = xaQueue.popAll();
                        LOG.info("response.send: " + StreamUtils.dumpHex(result));
                        OutputStream writer = socket.getOutputStream();
                        writer.write(result);
                        writer.flush();
                    }
                }
                catch (IOException e) {
                	e.printStackTrace();
                	LOG.error("监听数据不正确，丢弃数据");
                	running = false;
                }
            }
        }
        finally {
            running = false;
        }
	}
	
	void kill() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("关闭socket连接失败");
		}
	}

}
