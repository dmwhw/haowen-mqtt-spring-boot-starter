package com.haowen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class FileUtils {
	/**
	 * 写入等待
	 * @author haowen
	 * @time 2018年2月23日下午4:14:00
	 * @Description 
	 * @param file
	 */
	public  static void writeWait(File file){
		int retryTime=0;
		Long oldLen= file.length();
		while(true&&retryTime++<200){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			if (oldLen.equals( file.length())){
				break;
			}
			oldLen=file.length();
		}
	}
	/**
	 * 写入等待
	 * @author haowen
	 * @time 2018年2月23日下午4:14:00
	 * @Description 
	 * @param file
	 */
	public  static void writeWaitDelete(File file){
		int retryTime=0;
		while(true&&retryTime++<200){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			if (!file.exists()){
				return;
			}
		}
	}
	public  static void writeWaitToMove(File srcFile,File targetFile){
		int retryTime=0;
		while(true&&retryTime++<200){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			if (!srcFile.exists()&&targetFile.exists()){
				return;
			}
		}
	}
	
	
	public static boolean copyFile(File srcFile,File destFile){
		try {
			if ((destFile.exists()) && (destFile.isDirectory())) {
				throw new IOException("Destination '" + destFile + "' exists but is a directory");
			}
			if (!destFile.getParentFile().exists()){
				destFile.getParentFile().mkdirs();
			}
			FileInputStream fis = null;
			FileOutputStream fos = null;
			FileChannel input = null;
			FileChannel output = null;
			try {
				fis = new FileInputStream(srcFile);
				fos = new FileOutputStream(destFile);
				input = fis.getChannel();
				output = fos.getChannel();
				long size = input.size();
				long pos = 0L;
				long count = 0L;
				while (pos < size) {
					count = (size - pos > 31457280L) ? 31457280L : size - pos;
					pos += output.transferFrom(input, pos, count);
				}
			} finally {
				close(output,fos,input,fis);
			}

			if (srcFile.length() != destFile.length()) {
				throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
 	public static boolean reNameToFile(File src,File tar){
		if (!tar.getParentFile().exists()){
			tar.getParentFile().mkdirs();
		}
		return src.renameTo(tar);

	}
	
	public static String getFileRealPath(File file){
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] getClassPathFileBytes(String filename){
		InputStream is =FileUtils.class. getClass().getResourceAsStream(filename);
		byte[] buff=new byte[1024];
		ByteArrayOutputStream  baos=new ByteArrayOutputStream();
		try {
			int len=0;
			while((len=is.read(buff))>0){
				baos.write(buff,0,len);
			}
			return baos.toByteArray();

		} catch (Exception e) {
			//e.printStackTrace();
		}finally {
			close(baos,is);
		}
		return buff;

	}
	
	public static byte[] getFileBytes(File file){
		byte[] buff=null;
		try {
			FileInputStream fis=new FileInputStream(file);
			buff = new byte[new Long(file.length()).intValue()];
			fis.read(buff);
			fis.close();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return buff;
	}
	
	public static String getMD5Checksum(String filename) throws Exception {
		File file = new File(filename);
		return getMD5Checksum(file);
	}
	
	public static String getMD5Checksum(File file) throws Exception {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = fis.read(buffer, 0, 1024)) != -1) {
				md.update(buffer, 0, length);
			}
			BigInteger bigInt = new BigInteger(1, md.digest());
			return bigInt.toString(16).toUpperCase();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static boolean writeFileBytes(File file,byte []b){
		if (b==null){
			return false;
		}
		if (!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		OutputStream os=null;
		try {
			os=new BufferedOutputStream(new FileOutputStream(file));
			os.write(b);;
			os.close();
			return file.exists();
		} catch (Exception e) {
 		}finally {
			close(os);
		}
		return false;
	}
	
	public static boolean inputStreamToFile(InputStream inputStream,File target){
		OutputStream outputStream =null; 
		try {
			byte[] buffer = new byte[1024];
			int lenth = 0;
			 outputStream = new FileOutputStream( target);
			while ((lenth = inputStream.read(buffer)) != -1) {
			    outputStream.write(buffer, 0, lenth);
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
			close(outputStream);
		}
         return false;
	}
	
	
	public static ByteArrayInputStream toByteArrayInputStream(File file) throws Exception {
		// flush the image
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			// use buffer to enhance the performance,
			is = new BufferedInputStream(new FileInputStream(file));
			bos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) >= 0) {
				bos.write(buffer, 0, len);
			}
			return new ByteArrayInputStream(bos.toByteArray());
		}catch (Exception e) {
			throw e;
		}finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException e) {
				}
			}
		}
	}
	
	public final static void close(Closeable ...closes){
		if (closes!=null&&closes.length>0){
			for (Closeable closeable : closes) {
				try {
					if (closeable!=null){
						closeable.close();
					}
				} catch (Exception e) {
 				}
			}
		}
	}
	
}
