package com.haowen.mqtt.starter.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import com.haowen.mqtt.comp.MyMQTTClient;
import com.haowen.mqtt.comp.MyMqttConfig;
import com.haowen.mqtt.comp.MyMqttInitializer;
import com.haowen.mqtt.comp.listener.MqttConnectlLostListener;
import com.haowen.mqtt.comp.listener.MqttDeliveryCompleteListener;
import com.haowen.mqtt.comp.listener.MqttListener;
import com.haowen.mqtt.comp.listener.MqttMsgArrivedListener;
import com.haowen.mqtt.starter.annotation.MyMqttClientListener;

@Configuration
@EnableConfigurationProperties(value = MyMqttConfig.class)
@ConditionalOnClass(MqttClient.class)
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
	
	
	@Bean(myMQTTClient)
	public MyMQTTClient  myMQTTClient() {
		List< MqttListener> _mqttListeners=sort(mqttListeners, myMQTTClient);
		List<MqttConnectlLostListener> _mqttConnectlLostListeners=sort(mqttConnectlLostListeners, myMQTTClient);
		List<MqttDeliveryCompleteListener> _mqttDeliveryCompleteListeners=sort(mqttDeliveryCompleteListeners, myMQTTClient);
		List<MqttMsgArrivedListener> _mqttMsgArrivedListeners=sort(mqttMsgArrivedListeners, myMQTTClient);
		return MyMqttInitializer.init(mqttConfig, _mqttListeners ,  _mqttConnectlLostListeners ,  _mqttDeliveryCompleteListeners ,   _mqttMsgArrivedListeners );
	}
	
	
	/**
	 * 对注入的listener，过滤指定名称，并且排序
	 * @author haowen
	 * @time 2018年10月23日上午12:40:17
	 * @Description  
	 * @param list
	 * @param mqttName
	 * @return
	 */
	private <T> List<T> sort(List<T> list,String mqttName){
		Map<Integer,T> map=new TreeMap<Integer,T>();
		if (list!=null){
			for (T mqttListener : list) {
				MyMqttClientListener findAnnotation = AnnotationUtils.findAnnotation(mqttListener.getClass(), MyMqttClientListener.class);
				if (findAnnotation!=null&& mqttName.equals(findAnnotation.clientName())){
					map.put(findAnnotation.order(), mqttListener);
					log.info("find mqtt listener {} for {},order is {}" ,mqttListener,mqttName,findAnnotation.order());
				}
			}
			
		}
		return new ArrayList<>(map.values());
	}
	
 	
}
