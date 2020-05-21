package com.collection.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.collection.controller.PhoneGapServlet;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("restriction")
public class ImageUtil {

	private static Logger logger = Logger.getLogger(ImageUtil.class);
	/**
	 * 生成组合头像
	 * 
	 * @param paths
	 *            用户图像
	 * @throws IOException
	 */
	public static void getCombinationOfhead(List<String> paths, String outPath) throws IOException {
		List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
		// 压缩图片所有的图片生成尺寸同意的 为 50x50
		for (int i = 0; i < paths.size(); i++) {
			bufferedImages.add(resize2(paths.get(i), 50, 50, true));
		}

		int width = 115; // 这是画板的宽高

		int height = 115; // 这是画板的高度

		// BufferedImage.TYPE_INT_RGB可以自己定义可查看API

		BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 生成画布
		Graphics g = outImage.getGraphics();

		Graphics2D g2d = (Graphics2D) g;

		// 设置背景色
		g2d.setBackground(new Color(255, 255, 255));

		// 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
		g2d.clearRect(0, 0, width, height);

		// 开始拼凑 根据图片的数量判断该生成那种样式的组合头像目前为4中
		int j = 1;
		for (int i = 1; i <= bufferedImages.size(); i++) {
			if (bufferedImages.size() == 4) {
				if (i <= 2) {
					g2d.drawImage(bufferedImages.get(i - 1), 50 * i + 4 * i - 50, 4, null);
				} else {
					g2d.drawImage(bufferedImages.get(i - 1), 50 * j + 4 * j - 50, 58, null);
					j++;
				}
			} else if (bufferedImages.size() == 3) {
				if (i <= 1) {

					g2d.drawImage(bufferedImages.get(i - 1), 31, 4, null);

				} else {

					g2d.drawImage(bufferedImages.get(i - 1), 50 * j + 4 * j - 50, 58, null);

					j++;
				}

			} else if (bufferedImages.size() == 2) {

				g2d.drawImage(bufferedImages.get(i - 1), 50 * i + 4 * i - 50, 31, null);

			} else if (bufferedImages.size() == 1) {

				g2d.drawImage(bufferedImages.get(i - 1), 31, 31, null);

			}

			// 需要改变颜色的话在这里绘上颜色。可能会用到AlphaComposite类
		}

		String format = "JPG";

		ImageIO.write(outImage, format, new File(outPath));
	}

	/**
	 * 图片缩放
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 */
	public static BufferedImage resize2(String filePath, int height, int width, boolean bb) {
		try {
			double ratio = 0; // 缩放比例
			File f = new File(filePath);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				// copyimg(filePath, "D:\\img");
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
			return (BufferedImage) itemp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 压缩图片
	 * @param imgsrc 源文件路径
	 * @param imgdist 目标文件路径
	 * @param widthdist 目标宽度
	 * @param heightdist 目标高度
	 */
	public static void reduceImg(String imgsrc, String imgdist , long size) {
		try {
			logger.info("come in reduceImg.....");
			File file = new File(imgsrc);
			logger.info("File file=="+file);
			if(file.exists()){
				int sizes = Integer.parseInt(String.valueOf(size))/1024;
				logger.info("File file.size=="+sizes);
				if(sizes > 20){
					//获取图片的高宽
				    FileInputStream fis = new FileInputStream(file);
				    
				    BufferedImage bufferedImg = ImageIO.read(fis);
				    int ratio = 0;
				    int widthratio = bufferedImg.getWidth()/Constants.COMPRESSIONWIDTH;
				    int heightratio = bufferedImg.getHeight()/Constants.COMPRESSIONHEIGHT;
				    if(widthratio > heightratio){
				    	ratio = widthratio;
				    }else{
				    	ratio = heightratio;
				    }
				    if(ratio<=1){
				    	ratio = 2;
				    }
				    int widthdist = bufferedImg.getWidth()/ratio;
				    int heightdist = bufferedImg.getHeight()/ratio;
				    //压缩图片
					File srcfile = new File(imgsrc);
					Image src = javax.imageio.ImageIO.read(srcfile);
					BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);
					tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
					File mubiaofile = new File(imgdist);
					if(mubiaofile.exists()){
						mubiaofile.delete();
					}
					FileOutputStream out = new FileOutputStream(imgdist);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					encoder.encode(tag);
					out.close();
				}else{
					//移动文件到指定目录
					if(!imgsrc.equals(imgdist)){
						renameFile(imgsrc,imgdist);
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	
	public static void renameFile(String file, String toFile) {  
		  
        File toBeRenamed = new File(file);  
        logger.info("File toBeRenamed=="+toBeRenamed);
        //检查要重命名的文件是否存在，是否是文件  
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {  
  
        	logger.info("File does not exist: " + file);  
            return;  
        }  
  
        File newFile = new File(toFile);  
  
        //修改文件名  
        if (toBeRenamed.renameTo(newFile)) {  
        	logger.info("File has been renamed.");  
        } else {  
        	logger.info("Error renmaing file");  
        }  
  
    }
}