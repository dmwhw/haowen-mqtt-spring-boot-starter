package com.haowen.mqtt.comp.listener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import com.haowen.mqtt.comp.MyMQTTClient;

public interface MqttDeliveryCompleteListener {

	public void deliveryDone(MyMQTTClient client,IMqttDeliveryToken token);
	
	
}
