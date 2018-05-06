package com.kekeinfo.web.utils;

import javax.servlet.http.HttpServletRequest;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.web.constants.Constants;

public class ImageFilePathUtils {
	
	/**
	 * 获取商品图片的默认图片路径
	 * @param type
	 * @return
	 */
	public static String buildStaticDefaultProductImage() {
		return new StringBuilder().append("/resources/img/default/").append("/default.jpg").toString();
	}
	
	public static String buildStaticDefaultProductLGImage() {
		return new StringBuilder().append("/resources/img/default/").append("/defaultlg.jpg").toString();
	}
	
	public static String buildStaticDefaultFilePath(String imageName, String fileType) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(Constants.STORECODE).append("/").append(fileType).append("/").append(imageName).toString();
	}
	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public static String buildStaticImageFilePath(String imageName) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(FileContentType.IMAGE.name()).append("/").append(imageName).toString();
	}
	
	public static String buildStaticDefaultImageFilePath(String imageName) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(Constants.STORECODE).append("/").append(FileContentType.IMAGE.name()).append("/").append(imageName).toString();
	}
	
	public static String buildHttp(HttpServletRequest request) {
		String scheme = request.getScheme()+"://"+request.getServerName();
		if(request.getServerPort()!=80){
			scheme+=":"+request.getServerPort();
		}
		
		return scheme + request.getContextPath();
	}
	
	/**
	 * Builds a default product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public static String buildProductImageFilePath(String sku, String imageName) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(Constants.STORECODE).append("/").append(FileContentType.PRODUCT.name()).append("/")
				.append(sku).append("/").append(imageName).toString();
	}
	
	/**
	 * Builds a large product image file path that can be used by the image servlet
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public static String buildLargeProductImageFilePath(String sku, String imageName) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(Constants.STORECODE).append("/").append(FileContentType.PRODUCTLG.name()).append("/")
				.append(sku).append("/").append(imageName).toString();
	}


	
	/**
	 * Builds a merchant store logo path
	 * @param store
	 * @return
	 */
	
	public static String buildStoreLogoFilePath(String logo) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append("DEFAULT").append("/").append(FileContentType.LOGO).append("/")
				.append(logo).toString();
	}
	
	/**
	 * Builds product property image url path
	 * @param store
	 * @param imageName
	 * @return
	 */
	public static String buildProductPropertyImageFilePath(String imageName) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(FileContentType.PROPERTY).append("/")
				.append(imageName).toString();
	}
	/**
	 * 组装日志图片文件路径 
	 * @param imageName
	 * @return
	 */
	public static String buildDailyImageFilePath(String imageName) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(Constants.STORECODE).append("/").append(FileContentType.DAILY.name()).append("/")
				.append(imageName).toString();
	}
	
	public static String buildFilePathByConentType(String imageName, FileContentType contentType) {
		return new StringBuilder().append(Constants.STATIC_URI).append("/").append(Constants.STORECODE).append("/").append(contentType.name()).append("/")
				.append(imageName).toString();
	}
}
