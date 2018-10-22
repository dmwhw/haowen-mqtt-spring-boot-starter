package com.haowen.mqtt.starter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * @author haowen
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface MyMqttClientListener {

	/**
	 * 顺序
	 * @author haowen
	 * @time 2018年10月23日上午12:31:08
	 * @Description 
	 * @return
	 */
	int order() default 999 ;
	
	/**
	 * 要注入MQTTclient的实例的名称
	 * @author haowen
	 * @time 2018年10月23日上午12:31:17
	 * @Description 
	 * @return
	 */
	String clientName();
	
}
