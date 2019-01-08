package com.haowen;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.haowen.LampDataUtils.DataFrameReader;
import com.haowen.mqtt.core.waiter.impl.AbstractWaiter;

public class CaptureWaiter extends AbstractWaiter {

	
	private Integer requestTimeStamp;
	
	private Integer waitUpSecond;
	
	
	
	public CaptureWaiter(Integer requestTimeStamp, Integer waitUpSecond) {
		super();
		this.requestTimeStamp = requestTimeStamp;
		this.waitUpSecond = waitUpSecond;
	}
 
	public  class WaitUpThread extends Thread{
		
		long lastTime=System.currentTimeMillis();
		public void run() {
			while(System.currentTimeMillis()-lastTime<=waitUpSecond){
				ThreadUtils.sleep500Millis();
			}
			synchronized (CaptureWaiter.this) {
				CaptureWaiter.this.notify();
			}
			
		};
		public void updateTime(){
			lastTime=System.currentTimeMillis();
		}
	} ;
	private  WaitUpThread w=new WaitUpThread();
	
	private   ConcurrentHashMap<Integer, DataFrameReader> datas=new ConcurrentHashMap<>();

	@Override
	public boolean collectData(String topic, MqttMessage message) {
		if (datas.size()>0){
			DataFrameReader reader = datas.entrySet().iterator().next().getValue();
			if (reader.getTotalPackage()==datas.size()){
				synchronized (CaptureWaiter.this) {
					this.notify();
					System.err.println("transfer success");

				}
				return true;
			}
  		}
		byte[] payload = message.getPayload();
		DataFrameReader frameReader = LampDataUtils.getFrameReader(payload,true);
		if (frameReader.getTimeStamp()==requestTimeStamp){
			datas.put(frameReader.getCurrentPackage(), frameReader);
			w.updateTime();
		}
		return false;
	}

	
	
 
	
	@Override
	public Object getMsg() {
		w.start();
		w.updateTime();
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
 				e.printStackTrace();
			}
		}
		byte[] bytes = LampDataUtils.combineDataToBytes(new ArrayList<>(datas.values()));
		return bytes;
	}
	
	
	
	
	

}
