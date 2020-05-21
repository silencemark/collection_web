package com.collection.util;

import java.awt.image.BufferedImage;
import java.io.File; 
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable; 

import javax.imageio.ImageIO;


import javax.servlet.http.HttpServletRequest;

/*import com.google.zxing.BarcodeFormat; 
import com.google.zxing.EncodeHintType; 
import com.google.zxing.MultiFormatWriter; 
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix; */

public class QRcode{ 
   
    /**
     * @param args
     * @throws Exception 
     */
	public static String getQRcode(String codeContent,HttpServletRequest request,String organizeid){
		/*String newImg = System.currentTimeMillis()/1000l + "_" + organizeid;
        String f= request.getSession().getServletContext().getRealPath("upload/qrcodes");
		 String text = codeContent; 
	        int width = 300; 
	        int height = 300; 
	        String format = "jpg"; 
	        Hashtable hints = new Hashtable(); 
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
	        BitMatrix bitMatrix = null;
			try {
				bitMatrix = new MultiFormatWriter().encode(text, 
				        BarcodeFormat.QR_CODE, width, height, hints);
				
		        File outputFile = new File(f+"/"+newImg+".jpg");
		        if(!outputFile.exists())    
		        {    
		            try {
		            	outputFile.createNewFile();    
		            } catch (IOException e) {    
		                // TODO Auto-generated catch block    
		                e.printStackTrace();    
		            }    
		        }    
				one.writeToFile(bitMatrix, format, outputFile);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return  "/upload/qrcodes/"+newImg+".jpg";*/
		return null;
	}

    /*public final static class one {
       
      private static final int BLACK = 0xFF000000; 
      private static final int WHITE = 0xFFFFFFFF; 
       
      private one() {} 
       
         
      public static BufferedImage toBufferedImage(BitMatrix matrix) { 
        int width = matrix.getWidth(); 
        int height = matrix.getHeight(); 
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        for (int x = 0; x < width; x++) { 
          for (int y = 0; y < height; y++) { 
            image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE); 
          } 
        } 
        return image; 
      } 
       
         
      public static void writeToFile(BitMatrix matrix, String format, File file) 
          throws IOException { 
        BufferedImage image = toBufferedImage(matrix); 
        if (!ImageIO.write(image, format, file)) { 
          throw new IOException("Could not write an image of format " + format + " to " + file); 
        } 
      } 
       
         
      public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) 
          throws IOException { 
        BufferedImage image = toBufferedImage(matrix); 
        if (!ImageIO.write(image, format, stream)) { 
          throw new IOException("Could not write an image of format " + format); 
        } 
      } 
       
    }*/
}