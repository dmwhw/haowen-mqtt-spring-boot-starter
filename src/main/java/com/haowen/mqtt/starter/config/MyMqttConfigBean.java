
package com.haowen.mqtt.starter.config;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "haowen.mqtt.starter")
public class MyMqttConfigBean extends com.haowen.mqtt.core.MyMqttConfig implements Serializable,Cloneable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2467270671637208567L;
	private boolean enable;
	public boolean isEnable() {
		return enable;
	}

	public MyMqttConfigBean setEnable(boolean enable) {
		this.enable = enable;
		return this;
	}


}