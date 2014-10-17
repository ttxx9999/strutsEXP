package org.vit.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgReader {
	
	public static Image getImage(String path){
		BufferedImage image=null;
		try {
			image = ImageIO.read(Runtime.getRuntime().getClass().getResource("/"+path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return image;
	}
	
}
