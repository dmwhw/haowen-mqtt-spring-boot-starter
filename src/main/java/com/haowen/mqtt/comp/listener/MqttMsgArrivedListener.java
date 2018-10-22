package com.haowen.mqtt.comp.listener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.haowen.mqtt.comp.MyMQTTClient;

public interface MqttMsgArrivedListener {
	public void messageArrived(MyMQTTClient client,String topic, MqttMessage message);
	
}
