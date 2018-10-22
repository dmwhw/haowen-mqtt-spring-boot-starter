package com.haowen;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.haowen.mqtt.comp.MyMQTTClient;
import com.haowen.mqtt.comp.listener.MqttListener;
import com.haowen.mqtt.starter.annotation.MyMqttClientListener;


@MyMqttClientListener(clientName="myMQTTClient",order=1000)
public class MqttA2 implements MqttListener{

	@Override
	public boolean preReconnected(MyMQTTClient client, Throwable cause) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterReconnected(MyMQTTClient client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryDone(MyMQTTClient client, IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(MyMQTTClient client, String topic, MqttMessage message) {
		// TODO Auto-generated method stub
		
	}



}
