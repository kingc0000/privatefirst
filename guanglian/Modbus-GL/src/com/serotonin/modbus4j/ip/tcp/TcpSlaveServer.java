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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.base.DataModal;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import com.serotonin.modbus4j.sero.util.ResultConstant;

/**
 * tcp握手请求，网关作为客户端模式请求服务
 * 
 * @author sam
 *
 */
public class TcpSlaveServer extends ModbusSlaveSet {
	// Configuration fields
	private final int port;
	final boolean encapsulated;
	private final Log LOG = LogFactory.getLog(TcpSlaveServer.class);
	// private static final Logger LOG =
	// LoggerFactory.getLogger(TcpHandSlave.class);

	// Runtime fields.
	private ServerSocket serverSocket;
	private boolean running = true;
	final Map<String, Socket> mapConnections = new HashMap<String, Socket>();

	public TcpSlaveServer(boolean encapsulated) {
		this(ModbusUtils.TCP_PORT, encapsulated);
	}

	public TcpSlaveServer(int port, boolean encapsulated) {
		this.port = port;
		this.encapsulated = encapsulated;
	}
	
	public Map<String, Socket> getMapConnections() {
		return mapConnections;
	}

	@Override
	public void start() throws ModbusInitException {
		try {
			/**
			 * 创建一个ServerSocket对象，并给它制定一个端口号， 通过这个端口号来监听客户端的连接，服务端接受客户端连接的请求是
			 * 不间断地接受的，所以服务端的编程一般都永无休止的运行
			 */
			serverSocket = new ServerSocket(port);
			int count = 0;
			while (true&&running) {
				Socket socket = serverSocket.accept();
				count++;
				/**
				 * 在服务端调用accept()方法接受客户端的连接对象，accept()方法是
				 * 一个阻塞式的方法，一直傻傻地等待着是否有客户端申请连接
				 */
				// socket.setKeepAlive(true);
				LOG.info("第" + count + "个连接,IP地址是：" + socket.getInetAddress() + ", socket.hashCode="
						+ socket.hashCode());

				// 每接收到一个Socket就建立一个新的线程来处理它
				new Thread(new Task(socket, this)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ModbusInitException(e);
		}
	}

	@Override
	public void stop() {
		// Close the socket first to prevent new messages.
		running = false;
		// Close all open connections.
		synchronized (mapConnections) {
			Iterator<Entry<String, Socket>> iter = mapConnections.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) iter.next();
				try {
					entry.getValue().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mapConnections.clear();
		}
		
		try {
			if (serverSocket!=null && serverSocket.isBound() && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
			getExceptionHandler().receivedException(e);
		}
		
	}

	private void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 采集测点数据
	 * @param modal
	 * @return
	 */
	public BatchResults<String> getDatas(DataModal modal) {
		if (mapConnections != null && mapConnections.size() > 0) {
			LOG.info("采集测点数据，目前网关连接数：" + mapConnections.size());
			Iterator<Entry<String, Socket>> iter = mapConnections.entrySet().iterator();
			BatchResults<String> results;
			while (iter.hasNext()) {
				Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) iter.next();
				Socket socket = entry.getValue();
				String serialNo = entry.getKey();
				if (modal.getSerialNo().equalsIgnoreCase(serialNo)) {
					BatchRead<String> batchRead = new BatchRead<String>();
					batchRead.setContiguousRequests(false);
					int i = 0;
					if (modal.getNode1() != null && modal.getChannel1() != null) {
						i++;
						batchRead.addLocator("node1", BaseLocator.holdingRegister(modal.getNode1(),
								modal.getChannel1() * 2, DataType.FOUR_BYTE_INT_UNSIGNED));
					}
					if (modal.getNode2() != null && modal.getChannel2() != null) {
						i++;
						batchRead.addLocator("node2", BaseLocator.holdingRegister(modal.getNode2(),
								modal.getChannel2() * 2, DataType.FOUR_BYTE_INT_UNSIGNED));
					}
					if (i > 0) {
						LOG.debug("采集测点数据：" + modal.getBusinessName() + " ，网关序列号：" + serialNo);
						synchronized (socket) {
							try {
								socket.setKeepAlive(true);
							} catch (SocketException e1) {
								e1.printStackTrace();
							}
							if (socket.isConnected()) {
								ModbusMaster master = new ModbusFactory().createTcpMaster(socket, true);
								master.setRetries(1);
								master.setTimeout(1000);
								try {
									master.init();
									results = master.send(batchRead);
									master.destroyWithoutSocket();
									return results;
								} catch (ModbusInitException | ModbusTransportException | ErrorResponseException e) {
//									closeSocket(socket);
//									iter.remove();
									master.destroyWithoutSocket();
									e.printStackTrace();
									LOG.error(e.getMessage());
									break;
								}
							} else {
								LOG.info("与网关连接已经断开，网关序列号：" + serialNo);
								closeSocket(socket);
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
			LOG.info("采集测点数据，暂时没有网关连接");
		}
		return null;
	}

	/**
	 * 读取测点的断电状态
	 * 
	 * @param modal
	 * @return
	 */
	public BatchResults<String> readPowerStatus(DataModal modal) {
		if (mapConnections != null && mapConnections.size() > 0) {
			LOG.info("读取测点的断电状态，目前网关连接数：" + mapConnections.size());
			Iterator<Entry<String, Socket>> iter = mapConnections.entrySet().iterator();
			BatchResults<String> results;
			while (iter.hasNext()) {
				Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) iter.next();
				Socket socket = entry.getValue();
				String serialNo = entry.getKey();
				if (modal.getSerialNo().equalsIgnoreCase(serialNo)) {
					BatchRead<String> batchRead = new BatchRead<String>();
					int i = 0;
					if (modal.getNode1() != null && modal.getChannel1() != null) {
						i++;
						batchRead.addLocator("node1", BaseLocator.holdingRegister(modal.getNode1(), modal.getChannel1()*2,
								DataType.FOUR_BYTE_INT_UNSIGNED));
					}
					if (i > 0) {
						LOG.debug("读取测点的断电状态：" + modal.getBusinessName() + " ，网关序列号：" + serialNo);
						synchronized (socket) {
							try {
								socket.setKeepAlive(true);
							} catch (SocketException e1) {
								e1.printStackTrace();
							}
							if (socket.isConnected()) {
								ModbusMaster master = new ModbusFactory().createTcpMaster(socket, true);
								master.setRetries(2);
								master.setTimeout(1000);
								try {
									master.init();
									results = master.send(batchRead);
									master.destroyWithoutSocket();
									return results;
								} catch (ModbusInitException | ModbusTransportException | ErrorResponseException e) {
//									closeSocket(socket);
//									iter.remove();
									e.printStackTrace();
									LOG.error(e.getMessage());
									break;
								}
							} else {
								LOG.info("与网关连接已经断开，网关序列号：" + serialNo);
								closeSocket(socket);
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
		return null;
	}

	/**
	 * 更新节点的启停状态
	 * 
	 * @param modal
	 * @return
	 */
	public int updateStartStatus(DataModal modal, short[] onoff) {
		if (mapConnections != null && mapConnections.size() > 0) {
			LOG.info("更新节点的启停状态，目前网关连接数：" + mapConnections.size());
			Iterator<Entry<String, Socket>> iter = mapConnections.entrySet().iterator();
			boolean flag = false;
			while (iter.hasNext()) {
				Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) iter.next();
				Socket socket = entry.getValue();
				String serialNo = entry.getKey();
				if (modal.getSerialNo().equalsIgnoreCase(serialNo)) {
					try {
						WriteRegistersRequest request = new WriteRegistersRequest(modal.getNode1(), modal.getChannel1()*2,
								onoff);
						// int value = 0;
						// WriteRegisterRequest request = new
						// WriteRegisterRequest(modal.getNode1(),
						// modal.getChannel1(), value);
						LOG.debug("对节点进行启停控制：" + modal.getBusinessName() + " ，网关序列号：" + serialNo);
						synchronized (socket) {
							socket.setKeepAlive(true);
							if (socket.isConnected()) {
								ModbusMaster master = new ModbusFactory().createTcpMaster(socket, true);
								master.setRetries(3);
								master.setTimeout(1000);
								try {
									master.init();
									WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
									master.destroyWithoutSocket();
									if (response.isException()) {
										LOG.error("Exception response: message=" + response.getExceptionMessage());
//										closeSocket(socket);
//										iter.remove();
									} else {
										LOG.debug("启停成功:" + modal.getBaseinfo());
										flag = true; // 成功
									}
								} catch (ModbusInitException e) {
									e.printStackTrace();
//									closeSocket(socket);
//									iter.remove();
								}
							} else {
								LOG.info("与网关连接已经断开，网关序列号：" + serialNo);
								closeSocket(socket);
								iter.remove();
							}
						}
					} catch (ModbusTransportException | SocketException e) {
						e.printStackTrace();
//						closeSocket(socket);
//						iter.remove();
						LOG.error(e.getMessage());
					}
					break;
				} else {
					continue;
				}
			}
			LOG.info("结束对节点进行启停控制");
			if (flag) {
				// 启停成功
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
