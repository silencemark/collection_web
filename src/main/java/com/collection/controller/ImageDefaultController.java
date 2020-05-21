package com.collection.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collection.service.chat.ChatService;


/**
 * 默认图像
 */
@Controller
@RequestMapping("/default/img")
public class ImageDefaultController {
	
	@Autowired
	private ChatService chatService; 
	
	
	/**
	 * 群头像
	 * @param groupid
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/group/{groupid}")
	@ResponseBody
	public void getAdminVcode(@PathVariable("groupid") String groupid, HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		map.put("limitnum", 4);
		Map<String, Object> chart = chatService.getChatGroupInfo(map);
		boolean ok = true;
		if(chart.get("groupurl")!=null){
			File file = new File(request.getSession().getServletContext().getRealPath("/")+chart.get("groupurl"));
			if(file.exists()){
				BufferedImage image = ImageIO.read(file);
				ImageIO.write(image, "JPEG", response.getOutputStream());
				ok = false;
			}
		}
		if(ok){
			List<Map<String, Object>> datalist = (List<Map<String, Object>>) chart.get("userlist");
			List<String> strList = new ArrayList<String>();
			for(Map<String, Object> user:datalist){
				strList.add(request.getSession().getServletContext().getRealPath("/")+user.get("headimage")+"");
			}
			BufferedImage image = getCombinationOfheadBufferedImage(strList);
			ImageIO.write(image, "JPEG", response.getOutputStream());
		}
		
	}
	
	public static BufferedImage getCombinationOfheadBufferedImage(List<String> paths) throws IOException {
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

		return outImage;
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
}
