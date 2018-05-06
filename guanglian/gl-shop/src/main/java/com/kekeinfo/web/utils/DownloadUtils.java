package com.kekeinfo.web.utils;


public class DownloadUtils {

	public static boolean canPreview(String ext) {
		if (!ext.equals("image/pjpeg")  
				&& !ext.equalsIgnoreCase("image/jpeg") 
				&& !ext.equalsIgnoreCase("image/png") 
				&& !ext.equalsIgnoreCase("image/x-png")
				&& !ext.equalsIgnoreCase("image/gif")
				&& !ext.equalsIgnoreCase("image/bmp")
				&& !ext.equalsIgnoreCase("text/plain")
				&& !ext.equalsIgnoreCase("application/pdf")) {  
					return false;
				
	}
		return true;
	}
}
