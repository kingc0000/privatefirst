package com.kekeinfo.web.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.sftp.SftpFile;
import com.sshtools.j2ssh.transport.ConsoleKnownHostsKeyVerification;

import edu.emory.mathcs.backport.java.util.Arrays;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class NetworkFileUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkFileUtils.class);
	
	private String serverType;
	private String basedir;
	private String ip;
	private String user;
	private String pw;
	private String prefix; 
	
	private void baseinformation() {
		serverType = PropertiesUtils.getPropertiesValue("img.server.type");
		basedir = PropertiesUtils.getPropertiesValue("img.basedir");
		prefix = PropertiesUtils.getPropertiesValue("img.prefix");
		
		ip = PropertiesUtils.getPropertiesValue("jcifs.netbios.wins");
		user = PropertiesUtils.getPropertiesValue("jcifs.smb.client.username");
		pw = PropertiesUtils.getPropertiesValue("jcifs.smb.client.password");
		LOGGER.debug("serverType="+serverType + " basedir=" + basedir);
	}
	
	/**
	 * 获取服务器图片目录的摄像头
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AjaxResponse getCamerasList(List<Camera> list) {
		baseinformation();
		AjaxResponse resp = new AjaxResponse();
		if (serverType!=null && serverType.equalsIgnoreCase("local")) { //本地服务器
			File baseFile = new File(basedir);
			String basepath = replaceWinpath(baseFile.getAbsolutePath()); 
			LOGGER.debug("basepath="+basepath);
			if (baseFile!=null&&baseFile.exists()) {
				File[] p_files = baseFile.listFiles(); //项目
				if (p_files!=null && p_files.length>0) {
					for (int i = 0; i < p_files.length; i++) {
						File[] s_files = p_files[i].listFiles(); //工地
						if (s_files!=null && s_files.length>0) {
							for (int j = 0; j < s_files.length; j++) {
								File[] t_list = s_files[j].listFiles(); //摄像头
								if (t_list!=null && t_list.length>0) {
									for (int k = 0; k < t_list.length; k++) {
										File file = t_list[k];
										String c_name = file.getName();
										if (file.isFile()||c_name.indexOf(".")==0) { //去掉隐藏文件(夹)和文件
											continue;
										}
										String c_path = replaceWinpath(file.getAbsolutePath());
										Camera camera = new Camera(c_name, getCameraPath(c_path, basepath));
										list.add(camera);
									}
								} 
							}
						}
					}
				}
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("linux")) { //linux服务器 
			SshClient client = new SshClient();
			try {
				ConsoleKnownHostsKeyVerification key = new ConsoleKnownHostsKeyVerification();
				client.connect(ip, 22, key);
				// 设置用户名和密码
				PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
				pwd.setUsername(user);
				pwd.setPassword(pw);
				int result = client.authenticate(pwd);
				if (result == AuthenticationProtocolState.COMPLETE) {// 如果连接完成
					SftpClient sftpClient = client.openSftpClient();
					List<SftpFile> p_files = sftpClient.ls(basedir); //项目目录集合
					if (p_files!=null && p_files.size()>0) {
						for (SftpFile p_file : p_files) { //项目
							if (p_file.isDirectory()&&!(p_file.getFilename().equals(".")||p_file.getFilename().equals(".."))) { //去掉linux下的.和..文件
								String p_path = p_file.getAbsolutePath();
								List<SftpFile> s_files = sftpClient.ls(p_path); //工地目录集合
								if (s_files!=null && s_files.size()>0) {
									for (SftpFile s_file : s_files) { //工地
										if (s_file.isDirectory()&&!(s_file.getFilename().equals(".")||s_file.getFilename().equals(".."))) {
											String s_path = s_file.getAbsolutePath();
											List<SftpFile> c_files = sftpClient.ls(s_path); //摄像头目录集合
											if (c_files!=null && c_files.size()>0) {
												for (SftpFile c_file : c_files) { //摄像头
													if (c_file.isDirectory()&&!(c_file.getFilename().equals(".")||c_file.getFilename().equals(".."))) {
														String c_path = c_file.getAbsolutePath();
														String c_name = c_file.getFilename();
														Camera camera = new Camera(c_name, getCameraPath(c_path, basedir));
														list.add(camera);
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if (client.isConnected()) {
						client.disconnect();
					}
				}
			} catch (IOException e) {
				if (client.isConnected()) {
					client.disconnect();
				}
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败:"+e.getMessage());
				return resp;
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("win")) { //window服务器
//			SmbFile file_root = new SmbFile("smb://sam9901:hjfws@192.168.199.205/DESKTOP-8KJVG31/guanglian/");
			basedir = basedir.endsWith("/")?basedir:basedir+"/";
			SmbFile file_root;
			try {
				file_root = new SmbFile("smb://"+user+":"+pw+"@"+ip+basedir);
				String basepath = file_root.getPath();
				SmbFile[] f_list = file_root.listFiles(); //项目目录集合
				if (f_list!=null && f_list.length>0) {
					for (int i = 0; i < f_list.length; i++) {
						if (f_list[i].isDirectory()) {
							SmbFile[] s_list = f_list[i].listFiles(); //工地目录集合
							if (s_list!=null && s_list.length>0) {
								for (int j = 0; j < s_list.length; j++) {
									if (s_list[j].isDirectory()) {
										SmbFile[] t_list = s_list[j].listFiles(); //摄像头目录集合
										if (t_list!=null && t_list.length>0) {
											for (int k = 0; k < t_list.length; k++) {
												SmbFile c_file = t_list[k]; //摄像头目录
												if (c_file.isDirectory()) {
													String c_name = c_file.getName();
													String c_path = c_file.getPath();
													Camera camera = new Camera(c_name, getCameraPath(c_path, basepath));
													list.add(camera);
												}
											}
										} 
									}
								}
							}
						}
					}
				}
			} catch (SmbException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败:"+e.getMessage());
				return resp;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败:"+e.getMessage());
				return resp;
			}
		} else { //ftp
			FTPClient client = ftp_conn(ip, user, pw);
			try {
				FTPFile[] f_list = client.listDirectories(); //项目
				if (f_list!=null && f_list.length>0) {
					for (int i = 0; i < f_list.length; i++) {
						if (f_list[i].isDirectory()) {
							FTPFile[] s_list = client.listDirectories("/"+f_list[i].getName()); ////工地目录集合
							if (s_list!=null && s_list.length>0) {
								for (int j = 0; j < s_list.length; j++) {
									if (s_list[j].isDirectory()) {
										FTPFile[] t_list = client.listDirectories("/"+f_list[i].getName()+"/"+s_list[j].getName()); //摄像头目录集合
										if (t_list!=null && t_list.length>0) {
											for (int k = 0; k < t_list.length; k++) {
												FTPFile c_file = t_list[k];//摄像头目录
												if (c_file.isDirectory()) {
													String c_name = c_file.getName();
													String c_path = "/"+f_list[i].getName()+"/"+s_list[j].getName()+"/"+c_name;
													Camera camera = new Camera(c_name, c_path);
													list.add(camera);
												}
											}
										} 
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				ftp_closed(client);
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败:"+e.getMessage());
				return resp;
			}
			ftp_closed(client);
		}
		return resp;
	}
	
	/**
	 * 在服务器端创建摄像头目录
	 * @param path 摄像头相对目录路径
	 * @return
	 */
	public AjaxResponse createCameraDirectory(String path) {
		baseinformation();
		AjaxResponse resp = new AjaxResponse();
		if (serverType!=null && serverType.equalsIgnoreCase("local")) { //本地服务器
			File baseFile = new File(getFullPath(basedir, replaceWinpath(path)));
			baseFile.mkdirs();
		} else if (serverType!=null && serverType.equalsIgnoreCase("linux")) { //linux服务器 
			SshClient client = new SshClient();
			try {
				ConsoleKnownHostsKeyVerification key = new ConsoleKnownHostsKeyVerification();
				client.connect(ip, 22, key);
				// 设置用户名和密码
				PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
				pwd.setUsername(user);
				pwd.setPassword(pw);
				int result = client.authenticate(pwd);
				if (result == AuthenticationProtocolState.COMPLETE) {// 如果连接完成
					SftpClient sftpClient = client.openSftpClient();
					sftpClient.mkdirs(getFullPath(basedir, path));
				}
				if (client.isConnected()) {
					client.disconnect();
				}
			} catch (IOException e) {
				if (client.isConnected()) {
					client.disconnect();
				}
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("创建失败:"+e.getMessage());
				return resp;
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("win")) { //window服务器
//			SmbFile smbFileOut = new SmbFile("smb://administrator:dibindb@10.130.14.101/share/bb.txt");  
			try {
				SmbFile file_root = new SmbFile("smb://"+user+":"+pw+"@"+ip+getFullPath(basedir, path));
				if (!file_root.exists()) {
					file_root.mkdirs();
				}
			} catch (MalformedURLException | SmbException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("创建失败:"+e.getMessage());
				return resp;
			}
		} else { //ftp
			FTPClient client = ftp_conn(ip, user, pw);
			try {
				client.makeDirectory(path);
			} catch (IOException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("创建失败:"+e.getMessage());
				ftp_closed(client);
				return resp;
			}
			ftp_closed(client);
		}
		return resp;
	}
	
	/**
	 * 获取服务器端指定日期下的所有图片路径
	 * @param path 摄像头目录路径
	 * @param dt 指定日期 /yyyy-MM-dd
	 * @return
	 */
	public AjaxResponse loadImagePath(String path, String dt) {
		return loadImagePath(path, dt, "-1", "");
	}
	
	/**
	 * 获取服务器端指定日期下的所有图片路径
	 * @param path 摄像头目录路径
	 * @param dt 指定日期 /yyyy-MM-dd
	 * @param begintm 起始时间段，如果begintm=="-1"或者为空，则读取指定日期下的全天
	 * @param endtm 截止时间段
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AjaxResponse loadImagePath(String path, String dt, String begintm, String endtm) {
		baseinformation();
		AjaxResponse resp = new AjaxResponse();
		if (serverType!=null && serverType.equalsIgnoreCase("local")) { //本地服务器
			File baseFile = new File(getFullPath(basedir, getFullPath(path, dt)+prefix)); //图片目录
			if (!baseFile.exists()||baseFile.isFile()) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("访问失败：目录不存在");
				return resp;
			} else {
				File[] files;
				//隐藏文件不读取
				if (StringUtils.isBlank(begintm)||begintm.equals("-1")) { //读取全天非隐藏文件
					files = baseFile.listFiles(new HiddenFileFilter());
				} else { //读取指定时间段下的非隐藏文件（不考虑日期）
					int btm = Integer.valueOf(begintm);
					int etm = Integer.valueOf(endtm);
					files = baseFile.listFiles(new HiddenAndTimerFileFilter(btm, etm));
				}
				
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) { 
						String ab_path = replaceWinpath(files[i].getAbsolutePath());
						Map<String, String> map = new HashMap<String, String>();
						map.put("abpath", getCameraPath(ab_path, basedir));
						resp.addDataEntry(map);
					}
				}
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("linux")) { //linux服务器 
			SshClient client = new SshClient();
			try {
				ConsoleKnownHostsKeyVerification key = new ConsoleKnownHostsKeyVerification();
				client.connect(ip, 22, key);
				// 设置用户名和密码
				PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
				pwd.setUsername(user);
				pwd.setPassword(pw);
				int result = client.authenticate(pwd);
				if (result == AuthenticationProtocolState.COMPLETE) {// 如果连接完成
					SftpClient sftpClient = client.openSftpClient();
					String imgDir = getFullPath(basedir, getFullPath(path, dt)+prefix);
					List<SftpFile> s_files = sftpClient.ls(imgDir); //图片目录
					if (s_files!=null && s_files.size()>0) {
						for (SftpFile file : s_files) {
							if (file.isFile()) {
								String ab_path = file.getAbsolutePath();
								Map<String, String> map = new HashMap<String, String>();
								map.put("abpath", getCameraPath(ab_path, basedir));
								resp.addDataEntry(map);
							}
						}
					}
				}
				if (client.isConnected()) {
					client.disconnect();
				}
			} catch (IOException e) {
				if (client.isConnected()) {
					client.disconnect();
				}
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("访问失败:"+e.getMessage());
				return resp;
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("win")) { //window服务器
			try {
				SmbFile file_root = new SmbFile("smb://"+user+":"+pw+"@"+ip+getFullPath(basedir, getFullPath(path, dt)+prefix)+"/");
				SmbFile[] p_list = file_root.listFiles(); //图片集合
				if (p_list!=null && p_list.length>0) {
					for (SmbFile file : p_list) {
						if (file.isFile()) {
							String ab_path = file.getPath();
							Map<String, String> map = new HashMap<String, String>();
							map.put("abpath", getCameraPath(ab_path, basedir));
							resp.addDataEntry(map);
						}
					}
				}
			} catch (MalformedURLException | SmbException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("访问失败:"+e.getMessage());
				return resp;
			}
		} else { //ftp
			FTPClient client = ftp_conn(ip, user, pw);
			try {
				FTPFile[] t_list = client.listDirectories(getFullPath(path, dt+prefix));
				if (t_list!=null && t_list.length>0) {
					for (int k = 0; k < t_list.length; k++) {
						FTPFile c_file = t_list[k];//图片集合
						if (c_file.isFile()) {
							String name = c_file.getName();
							Map<String, String> map = new HashMap<String, String>();
							map.put("abpath", getFullPath(path, dt+prefix)+"/"+name);
							resp.addDataEntry(map);
						}
					}
				} 
			} catch (IOException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("创建失败:"+e.getMessage());
				ftp_closed(client);
				return resp;
			}
			ftp_closed(client);
		}
		return resp;
	}
	
	/**
	 * 加载图片内容
	 * @param relativePath
	 * @return
	 */
	public byte[] viewImage(String relativePath) {
		baseinformation();
		String fullPath = getFullPath(basedir, relativePath);
		if (serverType!=null && serverType.equalsIgnoreCase("local")) { //本地服务器
			File f = new File(fullPath); //图片目录
			if (f.exists()&&f.isFile()) {  
				ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
				BufferedInputStream in = null;  
				try {  
					in = new BufferedInputStream(new FileInputStream(f));  
					int buf_size = 1024;  
					byte[] buffer = new byte[buf_size];  
					int len = 0;  
					while (-1 != (len = in.read(buffer, 0, buf_size))) {  
						bos.write(buffer, 0, len);  
					}  
					return bos.toByteArray();  
				} catch (IOException e) {  
					e.printStackTrace();  
					//加载失败的图片
					return FilePathUtils.failLoadImage();
				} finally {  
					try {  
						in.close();  
						bos.close();
					} catch (IOException e) {  
						e.printStackTrace(); 
					}  
				}  
	        }  
		} else if (serverType!=null && serverType.equalsIgnoreCase("linux")) { //linux服务器 
			SshClient client = new SshClient();
			try {
				ConsoleKnownHostsKeyVerification key = new ConsoleKnownHostsKeyVerification();
				client.connect(ip, 22, key);
				// 设置用户名和密码
				PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
				pwd.setUsername(user);
				pwd.setPassword(pw);
				int result = client.authenticate(pwd);
				if (result == AuthenticationProtocolState.COMPLETE) {// 如果连接完成
					SftpClient sftpClient = client.openSftpClient();
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					sftpClient.get(fullPath, os);
					return os.toByteArray();
				} 
			} catch (IOException e) {
				if (client.isConnected()) {
					client.disconnect();
				}
				e.printStackTrace();
				return FilePathUtils.failLoadImage();
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("win")) { //window服务器
			try {
				SmbFile f = new SmbFile("smb://"+user+":"+pw+"@"+ip+fullPath);
				if (f.exists()&&f.isFile()) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.getContentLength());  
					BufferedInputStream in = null;
					try {  
						in = new BufferedInputStream(f.getInputStream());  
						int buf_size = 1024;  
						byte[] buffer = new byte[buf_size];  
						int len = 0;  
						while (-1 != (len = in.read(buffer, 0, buf_size))) {  
							bos.write(buffer, 0, len);  
						}  
						return bos.toByteArray();  
					} catch (IOException e) {  
						e.printStackTrace();  
						//加载失败的图片
						return FilePathUtils.failLoadImage();
					} finally {  
						try {  
							in.close();  
							bos.close();
						} catch (IOException e) {  
							e.printStackTrace();  
						}  
					}  
				}
			} catch (MalformedURLException | SmbException e) {
				e.printStackTrace();
				return FilePathUtils.failLoadImage();
			}
		} else { //ftp
			FTPClient client = ftp_conn(ip, user, pw);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				client.retrieveFile(relativePath, os);
			} catch (IOException e) {
				e.printStackTrace();
				ftp_closed(client);
				//加载失败的图片
				return FilePathUtils.failLoadImage();
			}
			ftp_closed(client);
			return os.toByteArray();
		}
		return FilePathUtils.failLoadImage();
	}
	
	/**
	 * 获取指定摄像头的最新图片路径
	 * @param path
	 * @param dt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AjaxResponse monitorCamera(String path, String dt) {
		baseinformation();
		AjaxResponse resp = new AjaxResponse();
		if (serverType!=null && serverType.equalsIgnoreCase("local")) { //本地服务器
			File baseFile = new File(getFullPath(basedir, getFullPath(path, dt)+prefix)); //图片目录
			if (!baseFile.exists()||baseFile.isFile()) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("访问失败：目录不存在");
				return resp;
			} else {
				File[] files = baseFile.listFiles(new HiddenFileFilter());
				if (files!=null && files.length>0) {
					Arrays.sort(files, new Comparator<File>() {
						@Override
						public int compare(File o1, File o2) {
							//System.out.println("name="+o1.getName()+" time="+DateUtil.formatDate(new Date(o1.lastModified())));
							//System.out.println("name2="+o2.getName()+" time2="+DateUtil.formatDate(new Date(o2.lastModified())));
							long l = o2.lastModified() - o1.lastModified();
							if (l>0) {
								return 1;
							} else if(l==0l) {
								return 0;
							} else {
								return -1;
							}
//							return (int) (o2.lastModified()-o1.lastModified());
						}
					});
					//最新照片
					for (File file : files) {
						if (file.isFile()) {
							resp.addEntry("abpath", getCameraPath(replaceWinpath(file.getAbsolutePath()), basedir));
							return resp;
						}
					}
				} else {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setStatusMessage("访问失败：当前没有最新图片");
					return resp;
				}
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("linux")) { //linux服务器 
			SshClient client = new SshClient();
			try {
				ConsoleKnownHostsKeyVerification key = new ConsoleKnownHostsKeyVerification();
				client.connect(ip, 22, key);
				// 设置用户名和密码
				PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
				pwd.setUsername(user);
				pwd.setPassword(pw);
				int result = client.authenticate(pwd);
				if (result == AuthenticationProtocolState.COMPLETE) {// 如果连接完成
					SftpClient sftpClient = client.openSftpClient();
					String imgDir = getFullPath(basedir, getFullPath(path, dt));
					List<SftpFile> s_files = sftpClient.ls(imgDir); //图片目录
					if (s_files!=null && s_files.size()>0) {
						Collections.sort(s_files, new Comparator<SftpFile>(){
							@Override
							public int compare(SftpFile o1, SftpFile o2) {
								long l = o2.getAttributes().getModifiedTime().longValue() - o1.getAttributes().getModifiedTime().longValue();
								if (l>0) {
									return 1;
								} else if(l==0l) {
									return 0;
								} else {
									return -1;
								}
								//return o2.getAttributes().getModifiedTime().intValue()-o1.getAttributes().getModifiedTime().intValue();
							}
						});
						//最新照片
						for (SftpFile sftpFile : s_files) {
							if (sftpFile.isFile()&&sftpFile.getFilename().indexOf(".")!=0) {
								resp.addEntry("abpath", getCameraPath(sftpFile.getAbsolutePath(), basedir));
								return resp;
							}
						}
					} else {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
						resp.setStatusMessage("访问失败：当前没有最新图片");
						return resp;
					}
				} else {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setStatusMessage("连接失败");
				}
				if (client.isConnected()) {
					client.disconnect();
				}
			} catch (IOException e) {
				if (client.isConnected()) {
					client.disconnect();
				}
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败："+e.getMessage());
				return resp;
			}
		} else if (serverType!=null && serverType.equalsIgnoreCase("win")) { //window服务器
			try {
				SmbFile file_root = new SmbFile("smb://"+user+":"+pw+"@"+ip+getFullPath(basedir, getFullPath(path, dt)));
				if (file_root.exists()||file_root.isDirectory()) {
					SmbFile[] files = file_root.listFiles();
					if (files!=null && files.length>0) {
						Arrays.sort(files, new Comparator<SmbFile>() {
							@Override
							public int compare(SmbFile o1, SmbFile o2) {
								try {
									long l = o2.lastModified() - o1.lastModified();
									if (l>0) {
										return 1;
									} else if(l==0l) {
										return 0;
									} else {
										return -1;
									}
								} catch (SmbException e) {
									e.printStackTrace();
								}
								return -1;
							}
						});
						//最新照片
						for (SmbFile file : files) {
							if (file.isFile()&&file.getName().indexOf(".")!=0) {
								resp.addEntry("abpath", getCameraPath(file.getPath(), basedir));
								return resp;
							}
						}
					}
				} else {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setStatusMessage("访问失败：目录不存在");
					return resp;
				}
			} catch (MalformedURLException | SmbException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败："+e.getMessage());
				return resp;
			}
		} else { //ftp
			FTPClient client = ftp_conn(ip, user, pw);
			try {
				FTPFile[] t_list = client.listDirectories(getFullPath(path, dt));
				if (t_list!=null && t_list.length>0) {
					Arrays.sort(t_list, new Comparator<FTPFile>() {
						@Override
						public int compare(FTPFile o1, FTPFile o2) {
							return o2.getTimestamp().compareTo(o1.getTimestamp());
						}
					});
					for (int k = 0; k < t_list.length; k++) {
						FTPFile c_file = t_list[k];//图片集合
						if (c_file.isFile()&&c_file.getName().indexOf(".")!=0) {
							String name = c_file.getName();
							resp.addEntry("abpath", getFullPath(path, dt)+"/"+name);
							return resp;
						}
					}
				} 
			} catch (IOException e) {
				e.printStackTrace();
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("连接失败:"+e.getMessage());
				ftp_closed(client);
				return resp;
			}
			ftp_closed(client);
		}
		return resp;
	}
	/**
	 * 根据完整路径和根目录，获取摄像头的相对路径，其中相对路径必须以"/"开头
	 * @param path
	 * @param basedir
	 * @return
	 */
	private String getCameraPath(String path, String basedir) {
		path = path.replaceFirst(basedir, "");
		return path.indexOf("/")==0?path:"/"+path;
	}
	/**
	 * 在Windows中的文件路径格式为 D:\source\l.c 
	 * 而在Java中的文件路径格式为 D:/source/l.c 
	 * 在java中使用前者则会报错，所以需要先将Windows中的文件路径转换为java中可识别的路径。
	 * @param path
	 * @return
	 */
	private String replaceWinpath(String path) {
		return path.replace("\\", "/");
	}
	private String getFullPath(String basedir, String relativepath) {
		String tmp = basedir + relativepath;
		tmp.replace("//", "/");
		return tmp;
	}
	
	public FTPClient ftp_conn(String server, String user, String password) {
		FTPClient ftp = new FTPClient();
		// ftp.setDefaultTimeout(5000);
		try {
			int reply;
			ftp.connect(server);
			// ftp.connect(server,21,InetAddress.getLocalHost(),21);
//			System.out.println("Connected to " + server + ".");
//			System.out.println(ftp.getReplyString());
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				LOGGER.error("FTP server refused connection.");
				return null;
			} else {
				ftp.login(user, password);
				LOGGER.debug("Login success.");
				ftp.pasv();
				ftp.enterLocalPassiveMode();
			}
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ftp;
	}
	
	public void ftp_closed(FTPClient ftp) {
		try {
			if (ftp != null) {
				ftp.logout();
				ftp.disconnect();
			}
			ftp = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 过滤掉隐藏文件
	 * @author sam
	 *
	 */
	private class HiddenFileFilter implements FileFilter {
		@Override  
        public boolean accept(File file) {  
			if (file.isHidden()) {
				return false;
			} else return true;
//            String filename = pathname.getName().toLowerCase();  
//            if(filename.contains(".txt")){  
//                return false;  
//            }else{  
//                return true;  
//            }  
        } 
	}
	
	/**
	 * 获取指定时间段下的非隐藏文件
	 * @author sam
	 *
	 */
	private class HiddenAndTimerFileFilter implements FileFilter {
		
		private int begintm;
		private int endtm;
		
		public HiddenAndTimerFileFilter(int begintm, int endtm) {
			this.begintm = begintm;
			this.endtm = endtm;
		}
		@Override  
        public boolean accept(File file) {  
			if (file.isHidden()) {
				return false;
			} else {
				long l = file.lastModified();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(l);
				//时间点
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if (begintm<=hour && hour<endtm) {
					return true;
				} else return false;
			}
        } 
	}
}
