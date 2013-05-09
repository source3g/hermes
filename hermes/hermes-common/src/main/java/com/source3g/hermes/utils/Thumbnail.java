package com.source3g.hermes.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <p>
 * Title: 缩略图
 * </p>
 * 
 * <p>
 * Description: 图片缩略图
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * 
 * @author 54powerman
 * @version 1.0
 */
public class Thumbnail {
	private String srcFilePath;
	private String destFilePath;

	public static void main(String[] args) throws Exception {
//		Thumbnail thum = new Thumbnail("D:/source3g/programdata/images/ElectricMenu/a.jpg");
//		thum.resize(40,40);
		compressPic("D:/source3g/programdata/images/ElectricMenu/a.jpg","D:/source3g/programdata/images/ElectricMenu/a.jpg");
	}

	/**
	 * 构造函数
	 * 
	 * @param srcFilePath
	 *            String
	 * @throws IOException
	 */
	public Thumbnail(String srcFilePath) throws IOException {
		File file = new File(srcFilePath); // 读入文件
		String fileName = file.getName();
		this.destFilePath = file.getParent() + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "_s"+srcFilePath.substring(srcFilePath.lastIndexOf("."), srcFilePath.length());
		this.srcFilePath = srcFilePath;
	}
	/**
	 * 构造函数
	 * 
	 * @param srcFilePath
	 *            String
	 * @throws IOException
	 */
	public Thumbnail(String srcFilePath,String destFilePath) throws IOException {
//		File file = new File(srcFilePath); // 读入文件
//		String fileName = file.getName();
//		this.destFilePath = file.getParent() + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "_s"+srcFilePath.substring(srcFilePath.lastIndexOf("."), srcFilePath.length());
		this.destFilePath=destFilePath;
		this.srcFilePath = srcFilePath;
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * 
	 * @param w
	 *            int 新宽度
	 * @param h
	 *            int 新高度
	 * @throws IOException
	 */
	public void resize(int targetW, int targetH) throws IOException {
		// targetW，targetH分别表示目标长和宽
		BufferedImage srcImage = ImageIO.read(new File(srcFilePath));
		int type = srcImage.getType();
		BufferedImage target = null;
		double sx = (double) targetW / srcImage.getWidth();
		double sy = (double) targetH / srcImage.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
		// 则将下面的if else语句注释即可
		if (sx > sy) {
			sx = sy;
			targetW = (int) (sx * srcImage.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * srcImage.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = srcImage.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		ImageIO.write(target, srcFilePath.substring(srcFilePath.lastIndexOf(".") + 1, srcFilePath.length()), new File(destFilePath));
	}
	
	
	  
    public static boolean compressPic(String srcFilePath, String descFilePath)   
    {   
        File file = null;   
        BufferedImage src = null;   
        FileOutputStream out = null;   
        ImageWriter imgWrier;   
        ImageWriteParam imgWriteParams;   
  
        // 指定写图片的方式为 jpg   
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();   
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);   
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT   
        imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);   
        // 这里指定压缩的程度，参数qality是取值0~1范围内，   
        imgWriteParams.setCompressionQuality((float)0.4);   
        imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);   
        ColorModel colorModel = ColorModel.getRGBdefault();   
        // 指定压缩时使用的色彩模式   
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel   
                .createCompatibleSampleModel(16, 16)));   
  
        try  
        {   
            if(StringUtils.isBlank(srcFilePath))   
            {   
                return false;   
            }   
            else  
            {   
                file = new File(srcFilePath);   
                src = ImageIO.read(file);   
                out = new FileOutputStream(descFilePath);   
  
                imgWrier.reset();   
                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造   
                imgWrier.setOutput(ImageIO.createImageOutputStream(out));   
                // 调用write方法，就可以向输入流写图片   
                imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);   
                out.flush();   
                out.close();   
            }   
        }   
        catch(Exception e)   
        {   
            e.printStackTrace();   
            return false;   
        }   
        return true;   
    }  
}
