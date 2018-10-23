#haowen-mqtt-spring-boot-starter

2018-10-23 Version 1.0
支持重连、ssl配置、启动订阅主题、多client连接实例、自定义监听器

##一、依赖
集成需要eclipse-paho-mqtt-client、bcpro-jdk16-1.4(ssl支持)
##二、配置方式
需要开启如下配置

--模块的开关
haowen.mqtt.starter.enable=true
具体配置参照src/main/resources/META-INF/application.propperties.sample


填写好参数，使用注入获得默认的实例

@AutoWired
private MyMQTTClient myMQTTClient;

##三、疑问

1、如果我想创建另外一个mqttclient怎么办？

	@Bean
	public MyMQTTClient nonSslMQTTClient(
	List<MqttListener> mqttListeners,MyMqttConfig prop){
		//配置复制
		MyMqttConfig clone = (MyMqttConfig) prop.clone();
		clone.setUseSsl(false);
		clone.setMqttUrl(url2);
		clone.setMqttUser(userName2);
		clone.setMqttpw(pw2);
		
		//可以对Listener过滤、排序
		List<MqttListener> _mqttListeners=Filter.filterAndSort(mqttListeners, myMQTTClient);

		//直接赋值 listener
		return MyMqttInitializer.init(clone, _mqttListeners, null, null, null);
		  
	}

##请大家多多支持修改，给出建议，蟹蟹


