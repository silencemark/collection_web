package com.collection.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * 	导出Excel表格的公共类
 * @author pc-asus
 *
 */
public class ExcelUtil {

	/**
	 * 	导出excel
	 * @param filename 导出的文件名称
	 * @param tablename 文件内的表的名称
	 * @param contentname 主题名称
	 * @param contentlst 需要导出的内容集合
	 * @param bgcolor 表格的背景颜色
	 * @param fontcolor 表格的字体颜色
	 * @throws IOException 
	 * @throws WriteException 
	 */
	public static void getExcel(String filename,String tablename,String contentname,List<String[]> contentlst,jxl.format.Colour bgcolor,jxl.format.Colour fontcolor,HttpServletRequest request,HttpServletResponse response) throws IOException, WriteException{
		
		//获取一个工作薄的对象
		String name=filename+".xls";
		String path=request.getSession().getServletContext().getRealPath("/upload/excel/")+name;
		WritableWorkbook book=Workbook.createWorkbook(new File(path));
		//创建一个表对象
		WritableSheet sheet=book.createSheet(tablename,0);
		
		//为表格设置样式
		//设置第一行的 高度
		sheet.setRowView(0, 1000);
		sheet.setRowView(1, 500);
		WritableFont fontColor=new WritableFont(WritableFont.createFont("隶书"),13);
		fontColor.setColour(fontcolor);
		//设置  第一个单元格  样式
		WritableCellFormat wc = new WritableCellFormat(fontColor); 
        // 设置居中   
        wc.setAlignment(Alignment.CENTRE);
        wc.setVerticalAlignment(VerticalAlignment.CENTRE);
        // 设置边框线   
        wc.setBorder(Border.ALL, BorderLineStyle.THIN);   
        // 设置单元格的背景颜色   
        wc.setBackground(bgcolor); 
		
		//设置标题
		Label b1=new Label(0, 0,contentname,wc);
		//合并单元格
		sheet.mergeCells(0, 0,contentlst.get(0).length-1, 0);
		sheet.addCell(b1);
		int length=contentlst.size();
		//遍历设置行
		for(int i=1;i<length+1;i++){
			String[] ary=contentlst.get(i-1);
			int leng=ary.length;
			//遍历设置列
			for(int j=0;j<leng;j++){
				if(i==1){
					//设置每一行的宽度
					sheet.setColumnView(j, 15);
				}
				//创建单元格
				Label b=new Label(j,i,ary[j]);
				//将单元格添加到表中
				sheet.addCell(b);
			}
		}
		//写入
		book.write();
		//关闭
		book.close();
		//创建成功
		//准备下载
		request.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="+new String(name.getBytes("gbk"),"iso-8859-1"));
        InputStream inputStream = new FileInputStream(new File(path));
        OutputStream os= response.getOutputStream();
    	byte[] b = new byte[2048];
        int len;
        while ((len= inputStream.read(b)) > 0) {
            os.write(b,0,len);
        }
        // 这里主要关闭。
        os.close();
        inputStream.close();
	}
}
