package com.haowen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

public class Base64 {
	private static char[] Base64Code = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '+', '/' };

	private static byte[] Base64Decode = { -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			62, -1, 63, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
			-1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
			-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
			42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

	public static String encode(byte[] b) {
		int code = 0;
		if (b == null)
			return null;
		StringBuffer sb = new StringBuffer((b.length - 1) / 3 << 6);
		for (int i = 0; i < b.length; i++) {
			code |= b[i] << 16 - i % 3 * 8 & 255 << 16 - i % 3 * 8;
			if ((i % 3 != 2) && (i != b.length - 1))
				continue;
			sb.append(Base64Code[((code & 0xFC0000) >>> 18)]);
			sb.append(Base64Code[((code & 0x3F000) >>> 12)]);
			sb.append(Base64Code[((code & 0xFC0) >>> 6)]);
			sb.append(Base64Code[(code & 0x3F)]);
			code = 0;
		}

		if (b.length % 3 > 0)
			sb.setCharAt(sb.length() - 1, '=');
		if (b.length % 3 == 1)
			sb.setCharAt(sb.length() - 2, '=');
		return sb.toString();
	}

	public static byte[] decode(String code) {
		if (code == null)
			return null;
		int len = code.length();
		if (len % 4 != 0)
			throw new IllegalArgumentException(
					"Base64 string length must be 4*n");
		if (code.length() == 0)
			return new byte[0];
		int pad = 0;
		if (code.charAt(len - 1) == '=')
			pad++;
		if (code.charAt(len - 2) == '=')
			pad++;
		int retLen = len / 4 * 3 - pad;
		byte[] ret = new byte[retLen];
		for (int i = 0; i < len; i += 4) {
			int j = i / 4 * 3;
			char ch1 = code.charAt(i);
			char ch2 = code.charAt(i + 1);
			char ch3 = code.charAt(i + 2);
			char ch4 = code.charAt(i + 3);
			int tmp = Base64Decode[ch1] << 18 | Base64Decode[ch2] << 12
					| Base64Decode[ch3] << 6 | Base64Decode[ch4];
			ret[j] = (byte) ((tmp & 0xFF0000) >> 16);
			if (i < len - 4) {
				ret[(j + 1)] = (byte) ((tmp & 0xFF00) >> 8);
				ret[(j + 2)] = (byte) (tmp & 0xFF);
			} else {
				if (j + 1 < retLen)
					ret[(j + 1)] = (byte) ((tmp & 0xFF00) >> 8);
				if (j + 2 < retLen)
					ret[(j + 2)] = (byte) (tmp & 0xFF);
			}
		}
		return ret;
	}
	
    public static byte[] int2bytes(int num){  
        byte[] result = new byte[4];  
        result[0] = (byte)((num >>> 0) & 0xff );  
        result[1] = (byte)((num >>> 8) & 0xff );  
        result[2] = (byte)((num >>> 16)& 0xff );  
        result[3] = (byte)((num >>> 24) & 0xff);
        return result;  
    }  
      
    public static int bytes2int(byte[] bytes){  
        int result = 0;  
        if(bytes.length == 4){  
            int a = (bytes[3] & 0xff) << 24;
            int b = (bytes[2] & 0xff) << 16;  
            int c = (bytes[1] & 0xff) << 8;  
            int d = (bytes[0] & 0xff);  
            result = a | b | c | d;  
        }  
        return result;  
    } 
	
	public static void base64ToFile(String base64, String path) throws IOException{
		base64ToFile(base64, new File(path));
	}
	
	public static void base64ToFile(String base64, File file) throws IOException{
		byte[] decode = Base64.decode(base64);
		OutputStream outputStream = null;
		File path = new File(file.getParent());
		path.mkdirs();
		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(decode);
		} catch (IOException e) {
			throw e;
		}finally{
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String FileToBase64(File file){
		byte[] fileToByte = FileToByte(file);
		if (fileToByte==null||fileToByte.length==0){
			return null;
		}
		return  encode(fileToByte);
	}
	
	private static byte[] FileToByte(File file){
		InputStream fis=null;
		ByteArrayOutputStream baos=null;
		try {
			fis=new BufferedInputStream(new FileInputStream(file));
			baos=new ByteArrayOutputStream();
			int len=0;
			byte [] b=new byte[1024*8];
			while((len=fis.read(b))>0){
				baos.write(b, 0, len);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fis.close();
			} catch (Exception e) {
			}

			try {
				baos.close();
			} catch (Exception e) {
			}
			
		}
		return null;
	}
	
}