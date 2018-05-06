package com.kekeinfo.web.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public class ImageUtils {
	
	public static BufferedImage resizeImage(BufferedImage originalImage,int width, int height, int type){
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	    }

	    public static BufferedImage resizeImageWithHint(BufferedImage originalImage,int width, int height,  int type){

		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	    }

	    public static ByteArrayInputStream ByteArrayOutputStream(InputStream inputStream) throws IOException{
	    	BufferedImage originalImage = ImageIO.read(inputStream);
	    	ByteArrayOutputStream os = new ByteArrayOutputStream();
	    	//ImageOutputStream outputStream = ImageIO.createImageOutputStream(originalImage);
	    	ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();

	    	// Configure JPEG compression: 70% quality
	    	ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
	    	jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    	jpgWriteParam.setCompressionQuality(0.7f);

	    	// Set your in-memory stream as the output
	    	jpgWriter.setOutput(ImageIO.createImageOutputStream(os));

	    	// Write image as JPEG w/configured settings to the in-memory stream
	    	// (the IIOImage is just an aggregator object, allowing you to associate
	    	// thumbnails and metadata to the image, it "does" nothing)
	    	jpgWriter.write(null, new IIOImage(originalImage, null, null), jpgWriteParam);

	    	// Dispose the writer to free resources
	    	jpgWriter.dispose();

	    	return new ByteArrayInputStream(os.toByteArray());
	    }
}
