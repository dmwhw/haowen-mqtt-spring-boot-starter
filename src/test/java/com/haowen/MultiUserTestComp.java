package com.haowen;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.haowen.mqtt.core.MyMQTTClient;
import com.haowen.mqtt.core.MyMqttConfig;
import com.haowen.mqtt.core.MyMqttInitializer;
import com.haowen.mqtt.core.listener.MqttListener;

@Component
public class MultiUserTestComp {

    @Resource()
    MyMqttConfig prop;

    // @Bean
    public MyMQTTClient nonSslMQTTClient(List<MqttListener> mqttListeners) {
        MyMqttConfig clone = (MyMqttConfig)prop.clone();
        clone.setMqttUser("admin_d444");
        clone.setMqttpw("123456a");
        clone.setOnStartedSubcribeTopics(new String[] {"$local/233933df730941bda9399fc706183966/cmdReturn"});
        // 直接赋值 listener
        return MyMqttInitializer.init(clone, mqttListeners, null, null, null);

    }

}
