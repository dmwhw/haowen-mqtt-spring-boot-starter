package com.haowen;

import java.io.File;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haowen.mqtt.core.MyMQTTClient;

//@Component
public class TestComp {

	@Autowired
	private MyMQTTClient myMQTTClient;
	
	//@PostConstruct
	public void Test(){
		System.out.println("start...");
		long start=System.currentTimeMillis();
		String regex="[\\S\\s]*((cmd)+).+((status)+)[\\S\\s]*";
		MqttMessage publishAndReturnResult = myMQTTClient.publishAndReturnResult("233933df730941bda9399fc706183966/cmd", 1, "{\"cmd\":\"status\",\"sender\":\"13888888888\",\"time\":1541477444}", 10000, "fbf6aa7edc44478e9d290fad6a40af28/cmdReturn",true, regex);
		System.err.println("pub done and getMsg ......................");
		System.out.println("time:"+(System.currentTimeMillis()-start));
		System.out.println(publishAndReturnResult==null);
	} 
	// @PostConstruct
	public void test222(){
 		String deviceUuid="fbf6aa7edc44478e9d290fad6a40af28";
		String capturetopic=deviceUuid+"/capture";;
 		String cmd=deviceUuid+"/cmd";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
		String json= "{\"cmd\":\"capture\",\"sender\":\"13888888888\",\"time\":"+requestTimeStamp+"} ";
		CaptureWaiter waiter=new CaptureWaiter(requestTimeStamp, 5000);
		byte[] imgByte= (byte[])myMQTTClient.publishWithWaiter(cmd, 1, json, 5000, capturetopic, true, waiter);
 		System.err.println(imgByte==null);
 		FileUtils.writeFileBytes(new File("d:/test6666.jpg"), imgByte);
	}
	
	
	
	
} 
 