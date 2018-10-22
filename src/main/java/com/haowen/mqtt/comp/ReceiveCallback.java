package com.haowen.mqtt.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haowen.mqtt.comp.listener.MqttConnectlLostListener;
import com.haowen.mqtt.comp.listener.MqttDeliveryCompleteListener;
import com.haowen.mqtt.comp.listener.MqttListener;
import com.haowen.mqtt.comp.listener.MqttMsgArrivedListener;

/**
 * 发布消息的回调类
 * 
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 * 
 * public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 * 
 * public void connectionLost(Throwable cause)在断开连接时调用。
 * 
 * public void deliveryComplete(MqttDeliveryToken token)) 接收到已经发布的 QoS 1 或 QoS 2
 * 消息的传递令牌时调用。 由 MqttClient.connect 激活此回调。
 * 
 */
public class ReceiveCallback implements MqttCallback {

	MyMQTTClient client;
	private final static Logger log = LoggerFactory.getLogger(MyMQTTClient.class);

	private List<MqttListener> mqttListeners = new ArrayList<>();
	private List<MqttConnectlLostListener> mqttConnectlLostListeners = new ArrayList<>();
	private List<MqttDeliveryCompleteListener> mqttDeliveryCompleteListeners = new ArrayList<>();
	private List<MqttMsgArrivedListener> mqttMsgArrivedListener = new ArrayList<>();
	private ExecutorService es = Executors.newCachedThreadPool();

	void shutdown(){
		try {
			es.shutdown();
		} catch (Exception e) {
		}
	}

	

	public void connectionLost(Throwable cause) {

		es.execute(new Runnable() {

			@Override
			public void run() {
				mqttConnectlLostListeners.forEach(item -> {
					try {
						boolean continues = item.preReconnected(client, cause);
						if (!continues) {
							return;
						}
					} catch (Exception e) {
						log.error("{}", e);
					}
				});
				mqttListeners.forEach(item -> {
					try {
						boolean continues = item.preReconnected(client,cause);
						if (!continues) {
							return;
						}
					} catch (Exception e) {
						log.error("{}", e);
					}
				});


				// 连接丢失后，一般在这里面进行重连
				log.info("MQTT Connection lost....{}", cause);
				if (client.isReConnect) {
					client.reconnect();
				}
				mqttConnectlLostListeners.forEach(item -> {
					try {
						item.afterReconnected(client);
					} catch (Exception e) {
						log.error("{}", e);
					}
				});
				mqttListeners.forEach(item -> {
					try {
						item.afterReconnected(client);
					} catch (Exception e) {
						log.error("{}", e);
					}
				});


			}
		});

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		es.execute(new Runnable() {
			@Override
			public void run() {
				mqttDeliveryCompleteListeners.forEach(item -> {
					try {
						item.deliveryDone(client,token);
					} catch (Exception e) {
						log.error("{}", e);
					}

				});
				mqttListeners.forEach(item -> {
					try {
						item.deliveryDone(client,token);
					} catch (Exception e) {
						log.error("{}", e);
					}

				});

			}

		});

	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (client.isLogMessageArrived) {
			log.info("receive topic [{}],length is {} bytes", topic,
					message.getPayload() == null ? 0 : message.getPayload().length);
		}
		if (client.isLogMessageArrivedBody) {
			log.info("receive topic [{}] body\r\n {}", topic, new String(message.getPayload()));

		}
		es.execute(new Runnable() {
			@Override
			public void run() {
				mqttMsgArrivedListener.forEach(item -> {
					try {
						item.messageArrived(client,topic, message);
					} catch (Exception e) {
						log.error("{}", e);
					}

				});
				mqttListeners.forEach(item -> {
					try {
						item.messageArrived(client,topic, message);
					} catch (Exception e) {
						log.error("{}", e);
					}

				});
			}

		});

	}

	public List<MqttListener> getMqttListeners() {
		return mqttListeners;
	}


	public List<MqttConnectlLostListener> getMqttConnectlLostListeners() {
		return mqttConnectlLostListeners;
	}



	public List<MqttDeliveryCompleteListener> getMqttDeliveryCompleteListeners() {
		return mqttDeliveryCompleteListeners;
	}

	public List<MqttMsgArrivedListener> getMqttMsgArrivedListener() {
		return mqttMsgArrivedListener;
	}



}