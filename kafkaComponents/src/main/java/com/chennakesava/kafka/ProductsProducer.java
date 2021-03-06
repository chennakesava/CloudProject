package com.chennakesava.kafka;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.JSONException;
import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.io.FileReader;
import java.util.Iterator;
 
//import org.json.simple.JSONArray;
//import org.json.simple.parser.JSONParser;

public class ProductsProducer {
	public static void main(String[] args) {
		Random rnd = new Random();
		String filePath = args[0];
		try {
			Properties props = new Properties();
			//set broker address
			props.put("metadata.broker.list", "104.196.53.159:9092,104.196.22.222:9092");
	        props.put("serializer.class", "kafka.serializer.StringEncoder");
	        //partitioner class is set default to serializer class
	        //props.put("partitioner.class", "example.producer.SimplePartitioner");
	        props.put("request.required.acks", "1");
	 
	        ProducerConfig config = new ProducerConfig(props);
	 
	        Producer<String, String> producer = new Producer<String, String>(config);
	        int events = 0;
	        //read a file or csv file here and send parsed text to kafka
	        JSONParser parser = new JSONParser();
	        Object obj = parser.parse(new FileReader(filePath));
	        JSONArray jsonArray = (JSONArray) obj;
	        events += jsonArray.size();
	        Iterator<JSONObject> itr = jsonArray.iterator();
	        while(itr.hasNext()) {
	        	JSONObject item = itr.next();
	        	String topic = "test";
	        	String msg = item.toString();
	        	KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic,msg);
	        	System.out.println("Sending:"+msg);
	        	producer.send(data);
	        }
	        System.out.println("Pushed:"+events+" events.");
	        producer.close();
		} catch (Exception e) {
			System.err.println("Caught exception : "+e.getMessage());
		}
				
	}
}
