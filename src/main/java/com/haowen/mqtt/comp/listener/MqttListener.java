package com.haowen.mqtt.comp.listener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.haowen.mqtt.comp.MyMQTTClient;

/**
 * 监听器，其他特定事件的监听器会优先于这个执行
 * @author haowen
 *
 */
public interface MqttListener {

	
	
	/**
	 * when allow reconnect, return true to continue to reconnect
	 * @author haowen
	 * @time 2018年10月18日下午3:14:56
	 * @Description  
	 * @param cause
	 * @return
	 */
	public boolean preReconnected(MyMQTTClient client,Throwable cause);
	
	public void afterReconnected(MyMQTTClient client);


	public void deliveryDone(MyMQTTClient client,IMqttDeliveryToken token);

	public void messageArrived(MyMQTTClient client,String topic, MqttMessage message);
	
}
