package com.haowen.mqtt.comp.listener;

import com.haowen.mqtt.comp.MyMQTTClient;

public interface MqttConnectlLostListener {

	/**
	 * when allow reconnect, return true to continue to reconnect
	 * @author haowen
	 * @time 2018年10月18日下午3:14:56
	 * @Description  
	 * @param cause
	 * @return
	 */
	public boolean preReconnected(MyMQTTClient client, Throwable cause);
	
	public void afterReconnected(MyMQTTClient client);

}
