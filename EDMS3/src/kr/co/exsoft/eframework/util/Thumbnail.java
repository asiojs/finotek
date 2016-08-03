package kr.co.exsoft.eframework.util;

import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;


/**
 * Thumbnail 클래스 - jai_core.jar include
 * @author 패키지 개발팀
 * @since 2014.07.22
 * @version 3.0
 *
 */
public class Thumbnail {

	/**
	 * 확장자 고정(JPG) 썸네일 생성
	 * @param url - 이미지 파일 디렉토리 절대경로
	 * @param filename - 이미지 파일명
	 * @param reFile - 변경된 이미지 파일명
	 * @param width - 썸네일 가로 길이
	 * @param height - 썸네일 세로 길이
	 * @return boolean
	 */
	public static boolean thumbnailMake(String url,String filename,String reFile,int width,int height) {
	
		boolean check = true;
		
		try {
			
			ParameterBlock pb = new ParameterBlock();
			
			pb.add(url + File.separator + filename);
			
			RenderedOp rop = JAI.create("fileload", pb);
			
			if(rop.getWidth() < width) {
				width = rop.getWidth();
			}
		
			if(rop.getHeight() < height) {
				height = rop.getHeight();
			}
			
			
			BufferedImage bi = rop.getAsBufferedImage();
			BufferedImage thumb = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			
			Graphics2D g = thumb.createGraphics();
			g.drawImage(bi,0,0,width,height,null);
			g.dispose();
			
			File file = new File(url+File.separator+reFile);
			
			ImageIO.write(thumb, "jpg", file);
			
		}catch(IOException e) {
			check = false;
		}
		
		return check;	
	}
	
	/**
	 * 확장자 유동 썸네일 생성
	 * @param url - 이미지 파일 디렉토리 절대경로
	 * @param filename - 이미지 파일명
	 * @param reFile - 변경된 이미지 파일명
	 * @param extension - 이미지 확장자
	 * @param width - 썸네일 가로 길이
	 * @param height - 썸네일 세로 길이
	 * @return boolean
	 */
	public static boolean makeThumbnail(String url, String filename, String reFile, String extension, int width, int height) {
	
		boolean check = true;
		
		try {
			ParameterBlock pb = new ParameterBlock();
			pb.add(url + File.separator + filename);
			RenderedOp rop = JAI.create("fileload", pb);
			if(rop.getWidth() < width) {
				width = rop.getWidth();
			}
			if(rop.getHeight() < height) {
				height = rop.getHeight();
			}
			BufferedImage bi = rop.getAsBufferedImage();
			BufferedImage thumb = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			
			Graphics2D g = thumb.createGraphics();
			g.drawImage(bi,0,0,width,height,null);
			g.dispose();
			
			File file = new File(url+File.separator+reFile);
			ImageIO.write(thumb, extension, file);
		}catch(IOException e) {
			check = false;
		}
		return check;	
	}
	
}
