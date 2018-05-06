package com.kekeinfo.web.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public class QCodeUtils {

	private Font deskfont = new Font("微软雅黑", Font.BOLD, 60);
	private Font storefont = new Font("微软雅黑", Font.BOLD, 80);
	private double rate=0.90;
	
	/**
	 * 生成包含字符串信息的二维码图片
	 * 
	 * @param outputStream
	 *            文件输出流路径
	 * @param content
	 *            二维码携带信息
	 * @param qrCodeSize
	 *            二维码图片大小
	 * @param imageFormat
	 *            二维码的格式
	 * @throws WriterException
	 * @throws IOException
	 */
	public InputStream createQrCode(String content, int qrCodeSize, String imageFormat)
			throws WriterException, IOException {
		// 设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 矫错级别
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		// 创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		// 使BufferedImage勾画QRCode (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// 使用比特矩阵画并保存图像
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		// BufferedImage 转 InputStream
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
		ImageIO.write(image, imageFormat, imageOutput);
		InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		return inputStream;
	}
	
	/**
	 * 生成包含字符串信息的二维码图片
	 * 
	 * @param outputStream
	 *            文件输出流路径
	 * @param content
	 *            二维码携带信息
	 * @param qrCodeSize
	 *            二维码图片大小
	 * @param imageFormat
	 *            二维码的格式
	 * @throws WriterException
	 * @throws IOException
	 */
	public InputStream createQrCodeWithLogo(String content, int qrCodeSize, String imageFormat)
			throws WriterException, IOException {
		// 设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 矫错级别
		//加入logo
		//File logoImg = new File("logo.jpg");
		InputStream is=this.getClass().getResourceAsStream("/logo.jpg");
		//org.springframework.core.io.Resource fileRource = new ClassPathResource("logo.jpg");
		//File logoImg  = fileRource.getFile();
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		// 创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		// 使BufferedImage勾画QRCode (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// 使用比特矩阵画并保存图像
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		 //读取logo图片  
        BufferedImage logo = ImageIO.read(is);  
        //设置二维码大小，太大，会覆盖二维码，此处20%  
        int logoWidth = logo.getWidth(null) > image.getWidth() /5 ? (image.getWidth() /5) : logo.getWidth(null);  
        int logoHeight = logo.getHeight(null) > image.getHeight() /5 ? (image.getHeight() /5) : logo.getHeight(null);  
        //设置logo图片放置位置  
        //中心  
        int x = (image.getWidth() - logoWidth) / 2;  
        int y = (image.getHeight() - logoHeight) / 2;  
        //右下角，15为调整值  
//      int x = twodimensioncode.getWidth()  - logoWidth-15;  
//      int y = twodimensioncode.getHeight() - logoHeight-15;  
		// BufferedImage 转 InputStream
      //开始合并绘制图片  
        graphics.drawImage(logo, x, y, logoWidth, logoHeight, null);  
        graphics.drawRoundRect(x, y, logoWidth, logoHeight, 15 ,15);  
        //logo边框大小  
        graphics.setStroke(new BasicStroke(2));  
        //logo边框颜色  
        graphics.setColor(Color.WHITE);  
        graphics.drawRect(x, y, logoWidth, logoHeight);  
        graphics.dispose();  
        
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
		ImageIO.write(image, imageFormat, imageOutput);
		InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		return inputStream;
	}

	/**
	 * 读二维码并输出携带的信息
	 */
	public void readQrCode(InputStream inputStream) throws IOException {
		// 从输入流中获取字符串信息
		BufferedImage image = ImageIO.read(inputStream);
		// 将图像转换为二进制位图源
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Result result = null;
		try {
			result = reader.decode(bitmap);
		} catch (ReaderException e) {
			e.printStackTrace();
		}
		System.out.println(result.getText());
	}
	
	/**
	 * 
	 * 桌号位置：90, 165
	 * logo 位置150，850 大小80*90
	 * @param is2
	 * @param x
	 * @param y
	 * @param deskno
	 * @param storeName
	 * @param logo
	 * @return
	 * @throws IOException
	 */
	public InputStream mergeBothImage(InputStream erImage, String deskno, String storeName, InputStream logo ) throws IOException{  
        InputStream is= null;  
//        OutputStream os = null; 
//        File os = new File("/Users/sam/t.jpg"); 
        try{  
        	rate = 1.2;
        	is = QCodeUtils.class.getClassLoader().getResourceAsStream("tablebg.jpg");  
//        	erImage = QCodeUtils.class.getClassLoader().getResourceAsStream("ercode.jpg");  
            BufferedImage image=ImageIO.read(is);  
            BufferedImage image2=ImageIO.read(erImage);
            BufferedImage image3 = null;
            if (logo!=null) {
            	image3 = ImageIO.read(logo);
			}
            Graphics g = image.getGraphics(); 
            //添加小程序二维码
            g.drawImage(image2, 90, 165, null); 
            //添加桌号信息
            g.setFont(deskfont);
            g.setColor(Color.white);
            //居中显示  
            int font_x= (int)(390-rate*g.getFontMetrics().stringWidth(deskno)/2); 
            MyDrawString(deskno, font_x, 130, rate, g); 
            
            //添加logo
            if (logo!=null) {
            	g.drawImage(image3, 90, 860, 80, 80, null);
			}
            //添加店名
            g.setFont(storefont);
            g.setColor(new Color(255, 128, 65));
            MyDrawString(storeName, 200, 930, rate, g); 
            
            ByteArrayOutputStream os = new ByteArrayOutputStream();  
            ImageIO.write(image, "jpg", os);  
            return new ByteArrayInputStream(os.toByteArray());
            
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
//            if(os != null){  
//                os.close();  
//            }  
            if(erImage != null){  
            	erImage.close();  
            }  
            if(logo != null){  
                logo.close();  
            }  
        }  
        return null;
    } 
	
	public static void MyDrawString(String str,int x,int y,double rate,Graphics g){  
        String tempStr=new String();  
        int orgStringWight=g.getFontMetrics().stringWidth(str);  
        int orgStringLength=str.length();  
        int tempx=x;  
        int tempy=y;  
        while(str.length()>0)  
        {  
            tempStr=str.substring(0, 1);  
            str=str.substring(1, str.length());  
            g.drawString(tempStr, tempx, tempy);  
            tempx=(int)(tempx+(double)orgStringWight/(double)orgStringLength*rate);  
        }  
    } 
	
	/** 
     * 二维码绘制logo 
     * @param twodimensioncodeImg 二维码图片文件 
     * @param logoImg logo图片文件 
     * */  
    public static BufferedImage encodeImgLogo(File twodimensioncodeImg,File logoImg){  
        BufferedImage twodimensioncode = null;  
        try{  
            if(!twodimensioncodeImg.isFile() || !logoImg.isFile()){  
                System.out.println("输入非图片");  
                return null;  
            }  
            //读取二维码图片  
            twodimensioncode = ImageIO.read(twodimensioncodeImg);  
            //获取画笔  
            Graphics2D g = twodimensioncode.createGraphics();  
            //读取logo图片  
            BufferedImage logo = ImageIO.read(logoImg);  
            //设置二维码大小，太大，会覆盖二维码，此处20%  
            int logoWidth = logo.getWidth(null) > twodimensioncode.getWidth() /5 ? (twodimensioncode.getWidth() /5) : logo.getWidth(null);  
            int logoHeight = logo.getHeight(null) > twodimensioncode.getHeight() /5 ? (twodimensioncode.getHeight() /5) : logo.getHeight(null);  
            //设置logo图片放置位置  
            //中心  
            int x = (twodimensioncode.getWidth() - logoWidth) / 2;  
            int y = (twodimensioncode.getHeight() - logoHeight) / 2;  
            //右下角，15为调整值  
//          int x = twodimensioncode.getWidth()  - logoWidth-15;  
//          int y = twodimensioncode.getHeight() - logoHeight-15;  
            //开始合并绘制图片  
            g.drawImage(logo, x, y, logoWidth, logoHeight, null);  
            g.drawRoundRect(x, y, logoWidth, logoHeight, 15 ,15);  
            //logo边框大小  
            g.setStroke(new BasicStroke(2));  
            //logo边框颜色  
            g.setColor(Color.WHITE);  
            g.drawRect(x, y, logoWidth, logoHeight);  
            g.dispose();  
            logo.flush();  
            twodimensioncode.flush();  
        }catch(Exception e){  
            System.out.println("二维码绘制logo失败");  
        }  
        return twodimensioncode;  
    }  
    
    /** 
     * 生成待logo标志的二维码 
     * @param twodimensioncodeImg 二维码图片文件 
     * @param logoImg logo图片文件 
     * @param file 输出文件 
     * */  
    public static void createErcodelogo(){
    	File twodimensioncodeImg = new File("ercode.jpg");
    	File logoImg = new File("logo.jpg");
    	String format = "JPEG";
    	File file = new File("ercodelogo.jpg");
        BufferedImage image = encodeImgLogo(twodimensioncodeImg, logoImg);  
        try {  
            ImageIO.write(image, format, file);  
        } catch (IOException e) {  
        	e.printStackTrace();
            System.out.println("二维码写入文件失败"+e.getMessage());  
        }  
    } 

}
