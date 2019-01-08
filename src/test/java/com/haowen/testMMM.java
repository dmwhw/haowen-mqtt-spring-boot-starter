package com.haowen;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class testMMM {

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		byte[] classPathFileBytes =com.haowen.mqtt.utils. FileUtils.getResourceFile("classpath:ca.crt");
		byte[] classPathFileBytes2 = com.haowen.mqtt.utils. FileUtils.getResourceFile("classpath:client.crt");
		byte[] classPathFileBytes3 = com.haowen.mqtt.utils. FileUtils.getResourceFile("classpath:client.key");
		byte[] a1 = Base64.encode(classPathFileBytes).getBytes("utf-8");
		byte[] a2 = Base64.encode(classPathFileBytes2).getBytes("utf-8");
		byte[] a3 = Base64.encode(classPathFileBytes3).getBytes("utf-8");
		FileUtils.writeFileBytes(new File("d:/ca.crt"), a1);
		FileUtils.writeFileBytes(new File("d:/client.crt"), a2);
		FileUtils.writeFileBytes(new File("d:/client.key"), a3);

	}
	
		
}
