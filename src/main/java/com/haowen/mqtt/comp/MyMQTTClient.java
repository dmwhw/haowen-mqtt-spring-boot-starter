package com.haowen.mqtt.comp;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.haowen.mqtt.utils.SslUtil;

public class MyMQTTClient {
	String mqttUrl;
	String mqttUser;
	String mqttpw;
	boolean useAuth = true;
	boolean useSSL = false;
	String caCrt;
	String clientCrt;
	String clientKey;
	boolean cleanSession = true;
	int connectTimeOut = 30;
	int connectReTryMaxTimes = 5;
	int connectReTryInterval = 10000;
	String[] onStartedSubcribeTopics;

	boolean isReConnect = true;
	Integer onStartedSubcribeTopicsQos[] = { 1 };
	int keepAliveTime = 60000;
	boolean isLogMessageArrived = false;
	boolean isLogMessageArrivedBody = false;
	boolean isSslStrict=false;
	
	String clientId;

	private ReceiveCallback receiveCallback;

	private Map<String, Integer> topics = new ConcurrentHashMap<>();
	private MqttClient client;
	private final ExecutorService es = Executors.newCachedThreadPool();
	private final static Logger log = LoggerFactory.getLogger(MyMQTTClient.class);
	private boolean destory = false;
	private int retryTimes = 0;

	// first,
	{
		// if ( StringUtils.isEmpty(clientId)){
		// clientId=UUID.randomUUID().toString();
		// }

	}

	public MyMQTTClient(ReceiveCallback receiveCallback) {
		super();
		this.receiveCallback = receiveCallback;
		receiveCallback.client = this;
	}

	public void connect() {
		if (destory) {
			return;
		}
		if (StringUtils.isEmpty(clientId)) {
			clientId = UUID.randomUUID().toString();
		}
		log.info("MQTT start to connect,user is {},clientid is {} ",mqttUser,clientId);
		try {

			// host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(mqttUrl, clientId, new MemoryPersistence());

			// MQTT的连接设置
			MqttConnectOptions options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(false);
			if (useAuth) {
				// 设置连接的用户名
				options.setUserName(mqttUser);
				// 设置连接的密码
				options.setPassword(mqttpw.toCharArray());

			}
			if (useSSL) {
				if (isSslStrict){
					options.setSocketFactory(SslUtil.getSocketFactory(caCrt, clientCrt, clientKey, ""));
					
				}else{
					options.setSocketFactory(SslUtil.getSocketFactory2(caCrt, clientCrt, clientKey, ""));

				}

			}
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(connectTimeOut);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(keepAliveTime);

			if (receiveCallback != null) {
				// 设置回调
				client.setCallback(receiveCallback);
			}
			// MqttTopic topic = client.getTopic(TOPIC);
			// setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
			// options.setWill(topic, "close".getBytes(), 2, true);

			client.connect(options);
			log.info("user:{} MQTT started...",mqttUser);
			retryTimes = 0;
			onConnected();
		} catch (MqttException e) {
			if (isReConnect) {
				// e.printStackTrace();
				log.info("user:{} 尝试重连..{}...{}",mqttUser, ++retryTimes, connectReTryMaxTimes);
				// 连接不上，重新连接几次
				try {
					Thread.sleep(connectReTryInterval);
				} catch (Exception e1) {
					// e1.printStackTrace();
				}
				if (retryTimes < connectReTryMaxTimes) {
					connect();
				} else {
					log.error(" MQTT连接失败{}", mqttUser);
					log.error(" MQTT连接失败{}", e);
				}

			} else {
				log.error(" MQTT连接失败{}", mqttUser);
				log.error("MQTT连接失败{}", e.toString());
			}
		} catch (Exception e) {
			log.error("MQTT连接失败{}",e);

		}

	}

	private void onConnected() {
		try {
			if (onStartedSubcribeTopics == null || onStartedSubcribeTopics.length == 0) {
				return;
			}
			for (int i = 0; i < onStartedSubcribeTopics.length; i++) {
				String topic = onStartedSubcribeTopics[i];
				if (onStartedSubcribeTopicsQos == null || i >= onStartedSubcribeTopicsQos.length) {
					subscribe(topic, 0);
				} else {
					subscribe(topic, onStartedSubcribeTopicsQos[i]);
				}
			}
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	public void subscribe(final String topic, final Integer Qos) {
		es.execute(new Runnable() {
			@Override
			public void run() {
				Set<String> keySet = topics.keySet();
				if (keySet.contains(topic))
					return;
				try {
					client.subscribe(new String[] { topic }, new int[] { Qos });
					topics.put(topic, Qos);
					log.info("user:{}新增了话题监听...{} Qos{}",mqttUser, topic, Qos);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		});

	}

	public void subscribeAll() {
		es.execute(new Runnable() {

			@Override
			public void run() {
				while (topics.isEmpty()) {
					return;
				}
				Set<String> keySet = topics.keySet();
				String[] topic1 = new String[topics.size()];
				keySet.toArray(topic1);

				// 订阅消息
				int[] Qos = new int[topics.size()];
				for (int i = 0; i < Qos.length; i++) {
					Qos[i] = topics.get(topic1[i]);
				}

				try {
					client.subscribe(topic1, Qos);
					log.info("user:{} 话题监听全加载...{}",mqttUser, Arrays.toString(topic1));
				} catch (Exception e) {
					log.error("", e);

				}
			}
		});
	}

	public void publish(String topic, int Qos, String payload) {
		try {
			MqttTopic mqttTopic = client.getTopic(topic);
			mqttTopic.publish(payload.getBytes("UTF-8"), 0, false);
		} catch (Exception e) {
			log.info("{}", e);
		}
	}

	public void publish(String topic, int Qos, byte[] payload) {
		try {
			MqttTopic mqttTopic = client.getTopic(topic);
			mqttTopic.publish(payload, 0, false);
		} catch (Exception e) {
			log.info("{}", e);
		}
	}

	public void publishAsyn(final String topic, int Qos, final String payload) {
		try {
			publishAsyn(topic, 2, payload.getBytes("UTF-8"));
		} catch (Exception e) {
			log.info("{}", e);
		}
	}

	public void publishAsyn(final String topic, int Qos, final byte[] payload) {
		es.execute(new Runnable() {

			@Override
			public void run() {
				try {
					MqttTopic mqttTopic = client.getTopic(topic);
					mqttTopic.publish(payload, 2, false);
				} catch (Exception e) {
					log.info("{}", e);
				}
			}
		});
	}

	public void reconnect() {
		if (destory) {
			return;
		}
		if (client != null) {
			try {
				client.close();
			} catch (Exception e) {
			}
			client = null;
		}
		try {
			connect();
			subscribeAll();
		} catch (Exception e) {
			log.error("", e);

		}
	}

	@PreDestroy
	public void destory() {
		destory = true;
		try {
			client.close();
		} catch (Exception e) {

		}
		try {
			es.shutdown();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		receiveCallback.shutdown();
		log.info("close MQTT...");
	}

	public String getMqttUrl() {
		return mqttUrl;
	}

	public MyMQTTClient setMqttUrl(String mqttUrl) {
		this.mqttUrl = mqttUrl;
		return this;
	}

	public String getMqttUser() {
		return mqttUser;
	}

	public MyMQTTClient setMqttUser(String mqttUser) {
		this.mqttUser = mqttUser;
		return this;
	}

	public String getMqttpw() {
		return mqttpw;
	}

	public MyMQTTClient setMqttpw(String mqttpw) {
		this.mqttpw = mqttpw;
		return this;
	}

	public boolean isUseAuth() {
		return useAuth;
	}

	public MyMQTTClient setUseAuth(boolean useAuth) {
		this.useAuth = useAuth;
		return this;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public MyMQTTClient setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
		return this;
	}

	public String getCaCrt() {
		return caCrt;
	}

	public MyMQTTClient setCaCrt(String caCrt) {
		this.caCrt = caCrt;
		return this;
	}

	public String getClientCrt() {
		return clientCrt;
	}

	public MyMQTTClient setClientCrt(String clientCrt) {
		this.clientCrt = clientCrt;
		return this;
	}

	public String getClientKey() {
		return clientKey;
	}

	public MyMQTTClient setClientKey(String clientKey) {
		this.clientKey = clientKey;
		return this;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public MyMQTTClient setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
		return this;
	}

	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}

	public MyMQTTClient setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
		return this;
	}

	public Integer getConnectReTryMaxTimes() {
		return connectReTryMaxTimes;
	}

	public MyMQTTClient setConnectReTryMaxTimes(int connectReTryMaxTimes) {

		this.connectReTryMaxTimes = connectReTryMaxTimes;
		return this;
	}

	public Integer getConnectReTryInterval() {
		return connectReTryInterval;
	}

	public MyMQTTClient setConnectReTryInterval(Integer connectReTryInterval) {
		this.connectReTryInterval = connectReTryInterval;
		return this;
	}

	public boolean isReConnect() {
		return isReConnect;
	}

	public MyMQTTClient setReConnect(boolean isReConnect) {
		this.isReConnect = isReConnect;
		return this;
	}

	public Integer getKeepAliveTime() {
		return keepAliveTime;
	}

	public MyMQTTClient setKeepAliveTime(Integer keepAliveTime) {
		if (keepAliveTime != null) {
			this.keepAliveTime = keepAliveTime;

		}
		return this;
	}

	public boolean isLogMessageArrived() {
		return isLogMessageArrived;
	}

	public MyMQTTClient setLogMessageArrived(boolean isLogMessageArrived) {
		this.isLogMessageArrived = isLogMessageArrived;
		return this;
	}

	public boolean isLogMessageArrivedBody() {
		return isLogMessageArrivedBody;
	}

	public MyMQTTClient setLogMessageArrivedBody(boolean isLogMessageArrivedBody) {
		this.isLogMessageArrivedBody = isLogMessageArrivedBody;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public MyMQTTClient setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public String[] getOnStartedSubcribeTopics() {
		return onStartedSubcribeTopics;
	}

	public MyMQTTClient setOnStartedSubcribeTopics(String[] onStartedSubcribeTopics) {
		this.onStartedSubcribeTopics = onStartedSubcribeTopics;
		return this;
	}

	public Integer[] getOnStartedSubcribeTopicsQos() {
		return onStartedSubcribeTopicsQos;
	}

	public boolean isSslStrict() {
		return isSslStrict;
	}

	public MyMQTTClient setSslStrict(boolean isSslStrict) {
		this.isSslStrict = isSslStrict;
		return this;
	}

	public MyMQTTClient setOnStartedSubcribeTopicsQos(Integer[] onStartedSubcribeTopicsQos) {
		this.onStartedSubcribeTopicsQos = onStartedSubcribeTopicsQos;
		return this;
	}

}