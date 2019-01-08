package com.haowen.mqtt.starter.config;

import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.haowen.mqtt.core.MyMQTTClient;
import com.haowen.mqtt.core.MyMqttConfig;
import com.haowen.mqtt.core.MyMqttInitializer;
import com.haowen.mqtt.core.listener.MqttConnectlLostListener;
import com.haowen.mqtt.core.listener.MqttDeliveryCompleteListener;
import com.haowen.mqtt.core.listener.MqttListener;
import com.haowen.mqtt.core.listener.MqttMsgArrivedListener;

@Configuration
@EnableConfigurationProperties(value = MyMqttConfigBean.class)
@ConditionalOnClass({MqttClient.class,MyMQTTClient.class})
@ConditionalOnProperty(prefix = "haowen.mqtt.starter", name="enable", havingValue="true")
public class MyMqttAutoConfigurer {
	private final static  Logger log= LoggerFactory.getLogger(MyMqttAutoConfigurer.class);

	
	public final static String myMQTTClient="myMQTTClient";
	@Autowired
	private MyMqttConfig mqttConfig;

	
	@Autowired(required=false)
	private List<MqttListener> mqttListeners;
	
	@Autowired(required=false)
	private List<MqttConnectlLostListener> mqttConnectlLostListeners;
	
	@Autowired(required=false)
	private List<MqttDeliveryCompleteListener> mqttDeliveryCompleteListeners;
	
	@Autowired(required=false)
	private List<MqttMsgArrivedListener> mqttMsgArrivedListeners;
	
	
	@Bean
	public MyMQTTClient  myMQTTClient() {
		List< MqttListener> _mqttListeners=Filter.filterAndSort(mqttListeners, myMQTTClient);
		List<MqttConnectlLostListener> _mqttConnectlLostListeners=Filter.filterAndSort(mqttConnectlLostListeners, myMQTTClient);
		List<MqttDeliveryCompleteListener> _mqttDeliveryCompleteListeners=Filter.filterAndSort(mqttDeliveryCompleteListeners, myMQTTClient);
		List<MqttMsgArrivedListener> _mqttMsgArrivedListeners=Filter.filterAndSort(mqttMsgArrivedListeners, myMQTTClient);
		return MyMqttInitializer.init(mqttConfig, _mqttListeners ,  _mqttConnectlLostListeners ,  _mqttDeliveryCompleteListeners ,   _mqttMsgArrivedListeners );
	}
	
 
	
 	
}
