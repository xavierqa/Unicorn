package com.orange.tfidf;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLReducer extends Reducer<Text, Text, Text, Text>{

	
	public static Logger LOG = LoggerFactory.getLogger(URLReducer.class);
	
	
	
	protected void reduce(Text key, Iterable<Text> values,Context context)
			throws IOException, InterruptedException {
		
		
		HashMap<String, Integer> dedup = new HashMap<String, Integer>();
		int count = 1;
		for (Text v: values){
			LOG.info("VALUES {}",v);
			count++;
		/*	String tmpValue = v.toString();
			if(dedup.containsKey(tmpValue)){
				int tmp = dedup.get(tmpValue);
				count = tmp + count;
				dedup.put(tmpValue, count);
			}else{
				dedup.put(tmpValue, count);
			}*/
			context.write(key, v);
		}
		
//		for (String k : dedup.keySet()){
//			LOG.info("Key {} , Value {}",k,dedup.get(k));
			
	//		context.write(key, v);
		
//		}
	}

}
