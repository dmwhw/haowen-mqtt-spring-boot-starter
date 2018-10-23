package com.haowen.mqtt.starter.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.haowen.mqtt.starter.annotation.MyMqttClientListener;

public class Filter {

	private final static  Logger log= LoggerFactory.getLogger(Filter.class);

	/**
	 *  过滤并且排序
	 * @author haowen
	 * @time 2018年10月23日下午1:50:36
	 * @Description 
	 * @param list
	 * @param mqttName
	 * @return
	 */
	public final static <T> List<T> filterAndSort(List<T> list,String mqttName){
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
	/**
	 *   排序
	 * @author haowen
	 * @time 2018年10月23日下午1:50:36
	 * @Description 
	 * @param list
	 * @param mqttName
	 * @return
	 */
	public final static <T> List<T> sort(List<T> list ){
		Map<Integer,T> map=new TreeMap<Integer,T>();
		if (list!=null){
			for (T mqttListener : list) {
				MyMqttClientListener findAnnotation = AnnotationUtils.findAnnotation(mqttListener.getClass(), MyMqttClientListener.class);
				if (findAnnotation!=null ){
					map.put(findAnnotation.order(), mqttListener);
					log.info("find mqtt listener {}  ,order is {}" ,mqttListener,findAnnotation.order());
				}
			}
			
		}
		return new ArrayList<>(map.values());
	}
}
