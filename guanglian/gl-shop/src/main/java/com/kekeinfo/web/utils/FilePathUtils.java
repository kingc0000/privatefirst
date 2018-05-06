package com.kekeinfo.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FilePathUtils {
	
	private static InputStream failLoadInput;
	private static byte[] in2b = null;
	static {
		failLoadInput = FilePathUtils.class.getClassLoader().getResourceAsStream("failload.jpg"); //加载失败
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[100];  
        int rc = 0;  
        try {
			while ((rc = failLoadInput.read(buff, 0, 100)) > 0) {  
			    swapStream.write(buff, 0, rc);  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
        in2b = swapStream.toByteArray();  
	}
	
	public static byte[] failLoadImage() {
		return in2b;
	}
	
	public static final byte[] input2byte(InputStream inStream)  
            throws IOException {  
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        int buf_size = 1024;
        byte[] buff = new byte[buf_size];  
        int rc = 0;  
        while ((rc = inStream.read(buff, 0, buf_size)) > 0) {  
            swapStream.write(buff, 0, rc);  
        }  
        byte[] in2b = swapStream.toByteArray();  
        swapStream.close();
        return in2b;  
    }  
	
}
