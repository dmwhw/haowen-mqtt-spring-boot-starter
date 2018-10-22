package com.haowen.mqtt.comp;

import java.util.List;

import org.springframework.util.StringUtils;

import com.haowen.mqtt.comp.listener.MqttConnectlLostListener;
import com.haowen.mqtt.comp.listener.MqttDeliveryCompleteListener;
import com.haowen.mqtt.comp.listener.MqttListener;
import com.haowen.mqtt.comp.listener.MqttMsgArrivedListener;

public class MyMqttInitializer {

	public static MyMQTTClient init(MyMqttConfig config, List<MqttListener> mqttListeners,
			List<MqttConnectlLostListener> mqttConnectlLostListeners,
			List<MqttDeliveryCompleteListener> mqttDeliveryCompleteListeners,
			List<MqttMsgArrivedListener> mqttMsgArrivedListeners) {
		ReceiveCallback receiveCallback = new ReceiveCallback();
		if (mqttListeners != null) {
			receiveCallback.getMqttListeners().addAll(mqttListeners);
		}
		if (mqttConnectlLostListeners != null) {
			receiveCallback.getMqttConnectlLostListeners().addAll(mqttConnectlLostListeners);
		}
		if (mqttDeliveryCompleteListeners != null) {
			receiveCallback.getMqttDeliveryCompleteListeners().addAll(mqttDeliveryCompleteListeners);
		}
		if (mqttMsgArrivedListeners != null) {
			receiveCallback.getMqttMsgArrivedListener().addAll(mqttMsgArrivedListeners);
		}
		
		MyMQTTClient mqttClient = new MyMQTTClient(receiveCallback);

		if (StringUtils.isEmpty(config.getMqttUrl())) {
			throw new RuntimeException("no mqtt url specified...");
		}

		mqttClient
				// pre login
				.setMqttpw(config.getMqttpw()).setMqttUrl(config.getMqttUrl())
				.setMqttUser(config.getMqttUser())
				.setUseAuth(config.isUseAuth())
				.setClientId(config.getClientId())
				// login param
				.setCleanSession(config.isCleanSession())
				.setKeepAliveTime(config.getKeepAliveTime())
				// ssl
				.setUseSSL(config.isUseSsl()).setCaCrt(config.getCaCrt())
				.setClientCrt(config.getClientCrt())
				.setClientKey(config.getClientKey())

				// reconnect
				.setConnectTimeOut(config.getConnectTimeOut() == null ? 10000
						: config.getConnectTimeOut())
				.setReConnect(config.isReConnect())
				.setConnectReTryInterval(config.getConnectReTryInterval())
				.setConnectReTryMaxTimes(config.getConnectReTryMaxTimes())
				// connected doing
				.setOnStartedSubcribeTopics(config.getOnStartedSubcribeTopics())
				.setOnStartedSubcribeTopicsQos(config.getOnStartedSubcribeTopicsQos())
				// msg arrived
				.setLogMessageArrived(config.isLogMessageArrived)
				.setLogMessageArrivedBody(config.isLogMessageArrivedBody)
				.setSslStrict(config.isSslStrict());
		

		if (Boolean.TRUE.equals(config.isAutoConnectWhenStarted())) {
			mqttClient.connect();
		}
		return mqttClient;
	}

}
