package com.orange.tfidf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IDFReducer extends Reducer<Text, Text, Text, Text>{

	
	public static Logger LOG = LoggerFactory.getLogger(IDFReducer.class);
	
	private Double total = 0.0;
	
	@Override
	protected void setup(org.apache.hadoop.mapreduce.Reducer.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String val = context.getConfiguration().get("total.number.documents");
		if ( val != null){
			total = Double.valueOf(val);
		}else{
			LOG.info("NO VALUE");
		}
	}
	
	protected void reduce(Text key, Iterable<Text> values,Context context)
			throws IOException, InterruptedException {
		
	//	LOG.info("TOTAL NUMBER OF DOCUMENTS {}", total);
		double count = 0;
		double IDF = 0.0;
		double TF = 0.0;
		double TFIDF = 0.0;
		HashMap<String, Double> tf = new HashMap<String, Double>();
//		LOG.info("KEY {}", key);
				
		for (Text val : values){
//			LOG.info("VALUES {}", val.toString()); 
			count = count +1.0;
			String [] vals = val.toString().split(" ");
			tf.put(vals[1], Double.valueOf(vals[0]));
		}
//		LOG.info("KEY{}",key);
//		LOG.info("Count {}", count);
		IDF =  Math.log(total /count);
		//IDF =  total /count;
//		LOG.info("IDF {}",IDF);
		
		for (String k : tf.keySet()){
		//	 context.write(key, new Text(String.valueOf(IDF)+"\t"+k));
			 TF = tf.get(k);
//			 LOG.info("TF {}",TF);
			 TFIDF = TF*IDF;
//			 LOG.info("TFIDF {}",TFIDF);
			 context.write(key, new Text(String.valueOf(TFIDF)+"\t"+k));
		}
	}
}
