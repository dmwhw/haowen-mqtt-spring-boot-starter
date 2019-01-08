package com.haowen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class DataUtils {
	
	public static byte[] toByte(Blob blob) {
		BufferedInputStream is = null;
		byte[] im = null;
		try {
			im = new byte[(int) blob.length()];
			is = new BufferedInputStream(blob.getBinaryStream());
			int len = (int) blob.length();
			int offset = 0;
			int read = 0;

			while (offset < len
					&& (read = is.read(im, offset, len - offset)) >= 0) {
				offset += read;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return im;
	}
	
	
	/**
	 *获得unsignShort的checksum
	 * @author haowen
	 * @time 2018年8月17日上午10:12:30
	 * @Description  
	 * @param data
	 * @return
	 */
	public  static Integer  getUnsignedShortCheckSum(byte[] data ){
		if (data==null){
			return null;
		}
		return getUnsignedShortCheckSum(data,0,data.length);
	}
	
	
	public  static Integer  getUnsignedShortCheckSum(byte[] data,int offset, int len){
		if (data==null){
			return null;
		}
		Integer dataCheckSum=0;
		for ( int j=offset;data!=null&&j<len;j++){
			dataCheckSum+= getUnsignedCharAsInt(data[j]);
		}
		dataCheckSum=dataCheckSum&0xFF;
		return dataCheckSum;
	}
	
	
	/**
	 *  
	 * @author haowen
	 * @time 2018年8月17日上午10:00:30
	 * @Description  
	 * @param hight 高字节位
	 * @param low
	 * @param inverses true时，高字节位放在低位，一般是放false
	 * @return
	 */
	public static int getUnsignedShort(int hight,int low,boolean inverses){
		if(inverses){
			return hight<<0|low<<8;//低位放高位
		}
		return hight<<8|low<<0;
	}
	
	
	
	/**
	 *  
	 * @author haowen
	 * @time 2018年8月17日上午10:20:23
	 * @Description  
	 * @param n
	 * @param inverse true时，高位放在低位，一般是放false
	 * @return
	 */
	public static byte[] getUnsignShortByte(int n,boolean inverse){
		byte [] buff=new byte[2];
		if (inverse){
			buff[0]=new Integer(( n&0xff00)>>8).byteValue();//高位放低位
			buff[1]=new Integer( (n&0x00ff)>>0).byteValue();
			return buff;
		}
		buff[1]=new Integer( (n&0xff00)>>8).byteValue();
		buff[0]=new Integer( (n&0x00ff)>>0).byteValue();
		return buff;
	}
	/**
	 * int转c的unsign int的字节
	 * @author haowen
	 * @time 2018年8月17日上午9:56:50
	 * @Description  
	 * @param n
	 * @param inverse
	 * @return
	 */
	/**
	 * 
	 * @author haowen
	 * @time 2018年8月17日上午10:20:46
	 * @Description  
	 * @param n
	 * @param inverse true时，高位放低位，一般是放false
	 * @return
	 */
	public static byte[] getUnsignIntByte(int n,boolean inverse){
		byte [] buff=new byte[4];
		if (inverse){
			buff[0]=new Integer(( n&0xff000000)>>24).byteValue();//高位放低位
			buff[1]=new Integer( (n&0x00ff0000)>>16).byteValue();
			buff[2]=new Integer( (n&0x0000ff00)>>8).byteValue();
			buff[3]=new Integer( (n&0x000000ff)>>0).byteValue();
			return buff;
		}
		buff[3]=new Integer( (n&0xff000000)>>24).byteValue();
		buff[2]=new Integer( (n&0x00ff0000)>>16).byteValue();
		buff[1]=new Integer( (n&0x0000ff00)>>8).byteValue();
		buff[0]=new Integer( (n&0x000000ff)>>0).byteValue();
		return buff;
	}
	
	
	/**
	 * byte转无符号的数字。unsigned char
	 * @author haowen
	 * @time 2018年8月17日上午10:21:13
	 * @Description  
	 * @param b
	 * @return
	 */
	public static int getUnsignedCharAsInt(byte b){
		int c=b;
			if (b<0){
			c=b+0xFF+1;
			return c;
		}
		return b;
	}
	 

	/**
	 * int类型
	 * @author haowen
	 * @time 2018年8月2日下午10:35:55
	 * @Description 
	 * @param h2
	 * @param h1
	 * @param l2
	 * @param l1
	 * @param inverse true时，低位的l1放高位.一般是放false
	 * @return
	 */
	public static int getUnsignedInt(int h2,int h1,int l2,int l1,boolean inverse){
		if(inverse){
			return h2<<0|h1<<8|l2<<16|l1<<24;
		}
		return  h2<<24|h1<<16|l2<<8|l1<<0;
	}
	

}
