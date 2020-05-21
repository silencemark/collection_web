package com.collection.util;

import java.util.Random;

/**
 * 
 * @ClassName: Generate_Verification_Code 
 * @Description: 随机生成一定数量的数字，大写字母，小写字母
 * @author 绝版
 * @date 2016-1-14
 */
public class Generate_Verification_Code 
{
	/**
	 * 随机生成一个字符窜，其中包含（数字，大写字母，小写字母）
	 * @param digit---你想得到“digit”长度的字符窜
	 * @return 返回生成的指定长度的字符窜
	 */
	public static String generate_verification_code(int digit)
	{
		String verification_code="";
		String[] str=new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"0","1","2","3","4","5","6","7","8","9"};
		int length=str.length;
		Random r=new Random();
		for(int i=0;i<digit;i++){
			verification_code=verification_code+str[r.nextInt(length)];
		}
		return verification_code;
	} 
	
	/**
	 *获取随机数
	 * @param num
	 * @return
	 */
	public static int randomNumberId(int num){
		int num1=0;
		String randomNum="";
		String[] str=new String[]{"0","1","2","3","4","5","6","7","8","9"};
		int length=str.length;
		Random r=new Random();
		for(int i=0;i<num;i++){
			randomNum+=str[r.nextInt(length)];
		}
		num1=Integer.parseInt(randomNum);
		return num1;
	}
}
