
package com.haowen.mqtt.comp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "haowen.mqtt.starter")
public class MyMqttConfig implements Serializable,Cloneable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8397214665876187636L;

	private boolean enable;
	
	private String mqttUrl;
	private String mqttUser;
	private String mqttpw;
	private boolean useAuth = true;
	
	
	private boolean useSsl = false;
	private String caCrt;
	private String clientCrt;
	private String clientKey;
	
	private Integer connectTimeOut = 30000;
	private Integer connectReTryMaxTimes = 5;
	private Integer connectReTryInterval = 10000;
	private boolean isReConnect=true;
	private boolean autoConnectWhenStarted = true;

	
	private String onStartedSubcribeTopics[];
	private Integer[] onStartedSubcribeTopicsQos = {0};
	
	private boolean isSslStrict=true;
	
	private Integer keepAliveTime = 60000;
	private String clientId;
	private boolean cleanSession=true;
	
	public boolean isLogMessageArrived=false;
	public boolean isLogMessageArrivedBody=false;

	public String getMqttUrl() {
		return mqttUrl;
	}

	public void setMqttUrl(String mqttUrl) {
		this.mqttUrl = mqttUrl;
	}

	public String getMqttUser() {
		return mqttUser;
	}

	public void setMqttUser(String mqttUser) {
		this.mqttUser = mqttUser;
	}

	public String getMqttpw() {
		return mqttpw;
	}

	public void setMqttpw(String mqttpw) {
		this.mqttpw = mqttpw;
	}

	public boolean isUseAuth() {
		return useAuth;
	}

	public void setUseAuth(boolean useAuth) {
		this.useAuth = useAuth;
	}

 
 

	public boolean isUseSsl() {
		return useSsl;
	}

	public void setUseSsl(boolean useSsl) {
		this.useSsl = useSsl;
	}

	public String getCaCrt() {
		return caCrt;
	}

	public void setCaCrt(String caCrt) {
		this.caCrt = caCrt;
	}

	public String getClientCrt() {
		return clientCrt;
	}

	public void setClientCrt(String clientCrt) {
		this.clientCrt = clientCrt;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

 

	public Integer getConnectReTryInterval() {
		return connectReTryInterval;
	}

	public void setConnectReTryInterval(Integer connectReTryInterval) {
		this.connectReTryInterval = connectReTryInterval;
	}

	public String[] getOnStartedSubcribeTopics() {
		return onStartedSubcribeTopics;
	}

	public void setOnStartedSubcribeTopics(String onStartedSubcribeTopics[]) {
		this.onStartedSubcribeTopics = onStartedSubcribeTopics;
	}

	public boolean isAutoConnectWhenStarted() {
		return autoConnectWhenStarted;
	}

	public void setAutoConnectWhenStarted(boolean autoConnectWhenStarted) {
		this.autoConnectWhenStarted = autoConnectWhenStarted;
	}

	public Integer getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(Integer keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public Integer[] getOnStartedSubcribeTopicsQos() {
		return onStartedSubcribeTopicsQos;
	}

	public void setOnStartedSubcribeTopicsQos(Integer[] onStartedSubcribeTopicsQos) {
		this.onStartedSubcribeTopicsQos = onStartedSubcribeTopicsQos;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	} 

	public boolean isReConnect() {
		return isReConnect;
	}

	public void setReConnect(boolean isReConnect) {
		this.isReConnect = isReConnect;
	}

	public Integer getConnectReTryMaxTimes() {
		return connectReTryMaxTimes;
	}

	public void setConnectReTryMaxTimes(Integer connectReTryMaxTimes) {
		this.connectReTryMaxTimes = connectReTryMaxTimes;
	}

	public boolean isLogMessageArrived() {
		return isLogMessageArrived;
	}

	public void setLogMessageArrived(boolean isLogMessageArrived) {
		this.isLogMessageArrived = isLogMessageArrived;
	}

	public boolean isLogMessageArrivedBody() {
		return isLogMessageArrivedBody;
	}

	public void setLogMessageArrivedBody(boolean isLogMessageArrivedBody) {
		this.isLogMessageArrivedBody = isLogMessageArrivedBody;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
	}

	public boolean isEnable() {
		return enable;
	}

	public MyMqttConfig setEnable(boolean enable) {
		this.enable = enable;
		return this;
	}

	public boolean isSslStrict() {
		return isSslStrict;
	}

	public MyMqttConfig setSslStrict(boolean isSslStrict) {
		this.isSslStrict = isSslStrict;
		return this;
	}

	@Override
	public String toString() {
		return "MyMqttConfig [mqttUrl=" + mqttUrl + ", mqttUser=" + mqttUser + ", mqttpw=" + mqttpw + ", useAuth="
				+ useAuth + ", useSsl=" + useSsl + ", caCrt=" + caCrt + ", clientCrt=" + clientCrt + ", clientKey="
				+ clientKey + ", connectTimeOut=" + connectTimeOut + ", connectReTryMaxTimes=" + connectReTryMaxTimes
				+ ", connectReTryInterval=" + connectReTryInterval + ", isReConnect=" + isReConnect
				+ ", autoConnectWhenStarted=" + autoConnectWhenStarted + ", onStartedSubcribeTopics="
				+ Arrays.toString(onStartedSubcribeTopics) + ", onStartedSubcribeTopicsQos="
				+ Arrays.toString(onStartedSubcribeTopicsQos) + ", isSslStrict=" + isSslStrict + ", keepAliveTime="
				+ keepAliveTime + ", clientId=" + clientId + ", cleanSession=" + cleanSession + ", isLogMessageArrived="
				+ isLogMessageArrived + ", isLogMessageArrivedBody=" + isLogMessageArrivedBody + "]";
	}

	@Override
	public Object clone() {
		  
		try {
			   ByteArrayOutputStream bos = new ByteArrayOutputStream();
			   ObjectOutputStream oos = new ObjectOutputStream(bos);
			   oos.writeObject(this);

			   ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			   return ois.readObject();
 		} catch (Exception e) {
 			e.printStackTrace();
		}
		return null;


	}

}