/*
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
package com.serotonin.modbus4j.ip.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.base.BaseMessageParser;
import com.serotonin.modbus4j.base.BaseRequestHandler;
import com.serotonin.modbus4j.base.DataModal;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.xa.HandMessageParser;
import com.serotonin.modbus4j.ip.xa.HandRequestHandler;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import com.serotonin.modbus4j.sero.messaging.HandMessageControl;
import com.serotonin.modbus4j.sero.messaging.TestableTransport;
import com.serotonin.modbus4j.sero.util.ResultConstant;
/**
 * tcp握手请求，网关作为客户端模式请求服务
 * @author sam
 *
 */
public class TcpHandSlave extends ModbusSlaveSet {
    // Configuration fields
    private final int port;
    final boolean encapsulated;
    private final Log LOG = LogFactory.getLog(TcpHandSlave.class);
//    private static final Logger LOG = LoggerFactory.getLogger(TcpHandSlave.class);

    // Runtime fields.
    private ServerSocket serverSocket;
    final ExecutorService executorService;
    final List<TcpConnectionHandler> listConnections = new ArrayList<>();

    public TcpHandSlave(boolean encapsulated) {
        this(ModbusUtils.TCP_PORT, encapsulated);
    }

    public TcpHandSlave(int port, boolean encapsulated) {
        this.port = port;
        this.encapsulated = encapsulated;
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void start() throws ModbusInitException {
        try {
        	/** 
             * 创建一个ServerSocket对象，并给它制定一个端口号， 
             * 通过这个端口号来监听客户端的连接，服务端接受客户端连接的请求是 
             * 不间断地接受的，所以服务端的编程一般都永无休止的运行 
             */ 
            serverSocket = new ServerSocket(port);
            int count = 0;
            Socket socket;
            while (true) {
            	count++;
            	/** 
                 * 在服务端调用accept()方法接受客户端的连接对象，accept()方法是 
                 * 一个阻塞式的方法，一直傻傻地等待着是否有客户端申请连接 
                 */
                socket = serverSocket.accept();
                //socket.setKeepAlive(true);
                LOG.info("第" + count + "个连接,IP地址是：" + socket.getInetAddress() + ", socket.hashCode="+socket.hashCode());
//                //获取服务端输入的消息  
//                InputStream in = socket.getInputStream();  
//                //用一个字节数字来存放消息，提高效率  
//                byte[] recData = new byte[1024];  
//                in.read(recData);  
//                String data = new String(recData);  
//                System.out.println("读取到客户端发送来的数据：" + data);
                
                TcpConnectionHandler handler = new TcpConnectionHandler(socket);
                executorService.execute(handler);
                
                synchronized (listConnections) {
                    listConnections.add(handler);
                }
            }
        }
        catch (IOException e) {
            throw new ModbusInitException(e);
        }
    }

    @Override
    public void stop() {
        // Close the socket first to prevent new messages.
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            getExceptionHandler().receivedException(e);
        }

        // Close all open connections.
        synchronized (listConnections) {
            for (TcpConnectionHandler tch : listConnections)
                tch.kill();
            listConnections.clear();
        }

        // Now close the executor service.
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            getExceptionHandler().receivedException(e);
        }
    }

    class TcpConnectionHandler implements Runnable {
        private final Socket socket;
        private TestableTransport transport;
        private HandMessageControl conn;

        TcpConnectionHandler(Socket socket) throws ModbusInitException {
            this.socket = socket;
            try {
                transport = new TestableTransport(socket.getInputStream(), socket.getOutputStream());
            }
            catch (IOException e) {
                throw new ModbusInitException(e);
            }
        }

        public Socket getSocket() {
			return socket;
		}

		public TestableTransport getTransport() {
			return transport;
		}

		public HandMessageControl getConn() {
			return conn;
		}

		@Override
        public void run() {
            BaseMessageParser messageParser;
            BaseRequestHandler requestHandler;
            
            messageParser = new HandMessageParser(false);
            requestHandler = new HandRequestHandler(TcpHandSlave.this);

            conn = new HandMessageControl();
            conn.setExceptionHandler(getExceptionHandler());
            try {
                conn.start(transport, messageParser, requestHandler, null);
                executorService.execute(transport);
            }
            catch (IOException e) {
                getExceptionHandler().receivedException(new ModbusInitException(e));
            }

            // Monitor the socket to detect when it gets closed.
           /* while (true) {
                try {
                    transport.testInputStream();
                }
                catch (IOException e) {
                    break;
                }

                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    // no op
                }
            }

            conn.close();
            kill();
            synchronized (listConnections) {
                listConnections.remove(this);
            }*/
        }

        void kill() {
            try {
                socket.close();
            }
            catch (IOException e) {
            	LOG.error("关闭socket连接失败");
            	e.printStackTrace();
                getExceptionHandler().receivedException(new ModbusInitException(e));
            }
        }
    }

	public List<TcpConnectionHandler> getListConnections() {
		return listConnections;
	}
	//15 01 00 00 00 06 02 03 00 06 00 02
	public void getDatas() {
		LOG.info("开始进行数据采集");
		int i = 0;
		if (listConnections!=null && listConnections.size()>0) {
			LOG.info("目前网关连接数："+listConnections.size());
			Iterator<TcpConnectionHandler> iter = listConnections.iterator();
			while (iter.hasNext()) {
				TcpConnectionHandler handler = iter.next();
				HandMessageControl conn = handler.getConn();
				String serialNo = conn.getSerialNo();
				//如果网关序列号不存在则移除该handler
				if (StringUtils.isBlank(serialNo)) {
					LOG.info("网关序列号不存在，移除该连接");
					handler.kill();
					iter.remove();
					continue;
				}
				LOG.info((++i)+".采集网关数据，网关序列号："+serialNo);
				Socket socket = handler.getSocket();
				
				if (socket.isConnected()) {
					LOG.info("socket.hashCode()=" + socket.hashCode() + " ip=" + socket.getInetAddress());
					ModbusMaster master = new ModbusFactory().createTcpMaster(handler.getSocket(), true);
					master.setRetries(1);
					master.setTimeout(3000);
					try {
						master.init();
						//LOG.info(master.send(new ReadHoldingRegistersRequest(2, 6, 2)));
						ModbusResponse modbusResponse = master.send(new ReadHoldingRegistersRequest(2, 0, 8));
						LOG.info("网关序列号：" + modbusResponse.getSerialNo());
						Thread.sleep(5000);
					} catch (ModbusTransportException | InterruptedException | ModbusInitException e) {
						handler.kill();
						iter.remove();
						e.printStackTrace();
					} 
				} else {
					LOG.info("与网关连接已经断开，网关序列号："+serialNo);
					handler.kill();
					iter.remove();
				}
			}
		} else {
			LOG.info("暂时没有网关连接");
		}
		LOG.info("结束数据采集");
	}
	
	public BatchResults<String> getDatas(DataModal modal) {
		if (listConnections!=null && listConnections.size()>0) {
			LOG.info("V1.1 目前网关连接数："+listConnections.size());
			Iterator<TcpConnectionHandler> iter = listConnections.iterator();
			BatchResults<String> results;
			while (iter.hasNext()) {
				TcpConnectionHandler handler = iter.next();
				HandMessageControl conn = handler.getConn();
				String serialNo = conn.getSerialNo();
				//如果网关序列号不存在则移除该handler
				if (StringUtils.isBlank(serialNo)) {
					LOG.info("网关序列号不存在，移除该连接");
					handler.kill();
					iter.remove();
					continue;
				}
				if (modal.getSerialNo().equalsIgnoreCase(serialNo)) {
					BatchRead<String> batchRead = new BatchRead<String>();
					batchRead.setContiguousRequests(false);
					int i =0;
					if (modal.getNode1()!=null && modal.getChannel1()!=null) {
						i++;
						batchRead.addLocator("node1",
			                    BaseLocator.holdingRegister(modal.getNode1(), modal.getChannel1()*2, DataType.FOUR_BYTE_INT_UNSIGNED));
//						batchRead.addLocator("node1",
//			                    BaseLocator.createLocator(modal.getNode1(), RegisterRange.HOLDING_REGISTER, modal.getChannel1()*2, DataType.FOUR_BYTE_INT_UNSIGNED, 0, 2, charset));
					} 
					if (modal.getNode2()!=null && modal.getChannel2()!=null) {
						i++;
//						batchRead.addLocator("node2",
//			                    BaseLocator.createLocator(modal.getNode2(), RegisterRange.HOLDING_REGISTER, modal.getChannel2()*2, DataType.FOUR_BYTE_INT_UNSIGNED, 0, 2, charset));
						batchRead.addLocator("node2",
			                    BaseLocator.holdingRegister(modal.getNode2(), modal.getChannel2()*2, DataType.FOUR_BYTE_INT_UNSIGNED));
					}
					if (i>0) {
						LOG.info("采集测点数据："+modal.getBusinessName()+" ，网关序列号："+serialNo);
						Socket socket = handler.getSocket();
						synchronized (socket) {
							try {
								socket.setKeepAlive(true);
							} catch (SocketException e1) {
								e1.printStackTrace();
							}
							if (socket.isConnected()) {
								ModbusMaster master = new ModbusFactory().createTcpMaster(handler.getSocket(), true);
								master.setRetries(1);
								master.setTimeout(3000);
								try {
									master.init();
									results = master.send(batchRead);
									return results;
								} catch (ModbusInitException | ModbusTransportException | ErrorResponseException e) {
									handler.kill();
									iter.remove();
									e.printStackTrace();
									LOG.error(e.getMessage());
									break;
								}
							} else {
								LOG.info("与网关连接已经断开，网关序列号："+serialNo);
								handler.kill();
								iter.remove();
							}
						}
					}
					break;
				} else {
					continue;
				}
			}
		} else {
			LOG.info("暂时没有网关连接");
		}
		LOG.info("结束数据采集");
		return null;
	}
	
	/**
	 * 读取测点的断电状态
	 * @param modal
	 * @return
	 */
	public BatchResults<String> readPowerStatus(DataModal modal) {
		if (listConnections!=null && listConnections.size()>0) {
			LOG.info("V1.1 目前网关连接数："+listConnections.size());
			Iterator<TcpConnectionHandler> iter = listConnections.iterator();
			BatchResults<String> results;
			while (iter.hasNext()) {
				TcpConnectionHandler handler = iter.next();
				HandMessageControl conn = handler.getConn();
				String serialNo = conn.getSerialNo();
				//如果网关序列号不存在则移除该handler
				if (StringUtils.isBlank(serialNo)) {
					LOG.info("网关序列号不存在，移除该连接");
					handler.kill();
					iter.remove();
					continue;
				}
				if (modal.getSerialNo().equalsIgnoreCase(serialNo)) {
					BatchRead<String> batchRead = new BatchRead<String>();
					int i =0;
					if (modal.getNode1()!=null && modal.getChannel1()!=null) {
						i++;
						batchRead.addLocator("node1",
			                    BaseLocator.holdingRegister(modal.getNode1(), modal.getChannel1(), DataType.FOUR_BYTE_INT_UNSIGNED));
					} 
					if (i>0) {
						LOG.info("读取测点的断电状态："+modal.getBusinessName()+" ，网关序列号："+serialNo);
						Socket socket = handler.getSocket();
						synchronized (socket) {
							try {
								socket.setKeepAlive(true);
							} catch (SocketException e1) {
								e1.printStackTrace();
							}
							if (socket.isConnected()) {
								ModbusMaster master = new ModbusFactory().createTcpMaster(handler.getSocket(), true);
								master.setRetries(1);
								master.setTimeout(3000);
								try {
									master.init();
									results = master.send(batchRead);
									return results;
								} catch (ModbusInitException | ModbusTransportException | ErrorResponseException e) {
									handler.kill();
									iter.remove();
									e.printStackTrace();
									LOG.error(e.getMessage());
									break;
								}
							} else {
								LOG.info("与网关连接已经断开，网关序列号："+serialNo);
								handler.kill();
								iter.remove();
							}
						}
					}
					break;
				} else {
					continue;
				}
			}
		} else {
			LOG.info("暂时没有网关连接");
		}
		LOG.info("结束测点断电状态采集");
		return null;
	}
	
	/**
	 * 更新节点的启停状态
	 * @param modal
	 * @return
	 */
	public int updateStartStatus(DataModal modal, short[] onoff) {
		if (listConnections!=null && listConnections.size()>0) {
			LOG.info("进行节点启停状态修改操作， 目前网关连接数："+listConnections.size());
			Iterator<TcpConnectionHandler> iter = listConnections.iterator();
			boolean flag = false;
			while (iter.hasNext()) {
				TcpConnectionHandler handler = iter.next();
				HandMessageControl conn = handler.getConn();
				String serialNo = conn.getSerialNo();
				//如果网关序列号不存在则移除该handler
				if (StringUtils.isBlank(serialNo)) {
					LOG.info("网关序列号不存在，移除该连接");
					handler.kill();
					iter.remove();
					continue;
				}
				if (modal.getSerialNo().equalsIgnoreCase(serialNo)) {
			        try {
			        	WriteRegistersRequest request = new WriteRegistersRequest(modal.getNode1(), modal.getChannel1(), onoff);
//			        	int value = 0;
//			        	WriteRegisterRequest request = new WriteRegisterRequest(modal.getNode1(), modal.getChannel1(), value);
			        	LOG.info("对节点进行启停控制："+modal.getBusinessName()+" ，网关序列号："+serialNo);
						Socket socket = handler.getSocket();
						synchronized (socket) {
							socket.setKeepAlive(true);
							if (socket.isConnected()) {
								ModbusMaster master = new ModbusFactory().createTcpMaster(handler.getSocket(), true);
								master.setRetries(1);
								master.setTimeout(3000);
								try {
									master.init();
									WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
									
									if (response.isException()) {
										LOG.error("Exception response: message=" + response.getExceptionMessage());
										handler.kill();
										iter.remove();
									} else {
										LOG.info("启停成功:" + modal.getBaseinfo());
										flag = true; //成功
									}
								} catch (ModbusInitException e) {
									e.printStackTrace();
									handler.kill();
									iter.remove();
								}
							} else {
								LOG.info("与网关连接已经断开，网关序列号："+serialNo);
								handler.kill();
								iter.remove();
							}
						}
			        } catch (ModbusTransportException | SocketException e) {
			            e.printStackTrace();
			            handler.kill();
						iter.remove();
						LOG.error(e.getMessage());
			        }
					break;
				} else {
					continue;
				}
			}
			LOG.info("结束对节点进行启停控制");
			if (flag) {
				//启停成功
				return ResultConstant.RESPONSE_STATUS_SUCCESS;
			} else {
				return ResultConstant.RESPONSE_STATUS_NOCONNECTION;
			}
		} else {
			LOG.info("暂时没有网关连接");
			return ResultConstant.RESPONSE_STATUS_NOGATEWAY;
		}
	}
}
