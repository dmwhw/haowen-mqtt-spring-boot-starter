package com.haowen.mqtt.utils;

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
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

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
	
	/**
	 * 获取文件的路径
	 * @author haowen
	 * @time 2018年10月19日下午4:34:14
	 * @Description  
	 * @param url
	 * @return
	 */
	public static byte[] getResourceFile(String url){
		if (url==null){
			return null;
		}
		
		if (url.startsWith("classpath:")){
			return getClassPathFileBytes( url.replaceFirst("classpath:", "/"));
		}else if(url.startsWith("file:")){
			return getFileBytes(new File( url.replaceFirst("file:", "")));

		}
		
		else{
			return getFileBytes(new File(url));
		}
		
	}
	
	/**
	 * 拿classPath中的。需要带个/
	 * @author haowen
	 * @time 2018年6月22日下午5:40:28
	 * @Description  
	 * @param filename
	 * @return
	 */
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
	
	
	/**
	 *  随机读取文件
	 * @author haowen
	 * @time 2018年8月17日下午2:17:30
	 * @Description 
	 * @param file
	 * @param offset 起始位置
	 * @param len 
	 * @param readOutOfBound false时，如果读取的范围len已经超出文件，就会停止，返回的byte数组的长度小于len;true时，超出范围，该位置会是00，返回的byte数组长度等于len
	 * @return
	 */
	public static byte[] getFileRandomBytes(File file,long offset,int len,boolean readOutOfBound){
		if(file==null||!file.exists()){
			return null;
		}
		int buffSize=len;
		if(!readOutOfBound&&offset+len>file.length()){
			buffSize=(int) (file.length()-offset);
		}
		 
		RandomAccessFile raf=null;
		byte [] buff=null;
		try {
			buff=new byte[buffSize];
			raf=new RandomAccessFile(file, "r");
			raf.seek(offset);
			raf.read(buff, 0, buffSize);
			return buff;
		} catch (Exception e) {
			e.printStackTrace();
			return buff;
		}finally {
			close(raf);
		}
		
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
	
	
	public static byte[] inputStreamToByte(InputStream inputStream){
		ByteArrayOutputStream outputStream =null; 
		try {
			byte[] buffer = new byte[1024];
			int lenth = 0;
			 outputStream = new ByteArrayOutputStream();
			while ((lenth = inputStream.read(buffer)) != -1) {
			    outputStream.write(buffer, 0, lenth);
			}
			return outputStream.toByteArray();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
			close(outputStream);
		}
         return null;
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
	
	public static void main(String[] args) {
		byte[] bytes = getFileRandomBytes(new File("D:/javasrc/tempwork/LampTest2/temp/voiceRecvFail/120217/1534478932915.tmp"), -5,10,true);
		for (byte b : bytes) {
			System.out.print(Integer.toHexString(b)+"-");
		}
	}

}
