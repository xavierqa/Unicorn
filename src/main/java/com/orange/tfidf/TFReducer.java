package com.orange.tfidf;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TFReducer extends Reducer<Text, Text, Text, Text> {

	public static Logger LOG = LoggerFactory.getLogger(TFReducer.class);

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

//		LOG.info("KEY:{}", key);
		Map<String, TFValues> count = new HashedMap();
		for (Text v : values) {
//			LOG.info("VALUE {}",v);
			int i = 1;
			String[] urldocument = v.toString().split(" ");
//			LOG.info("Urldoc {}",urldocument[0]);
			
			TFValues tfvalue = null; 
			/*new TFValues(urldocument[0],
					Integer.valueOf(urldocument[1]));*/
			if (urldocument.length > 1) {
				
				if (count.containsKey(urldocument[0])) {
					tfvalue = count.get(urldocument[0]);
					tfvalue.addCounter(i);
//					LOG.info("TFValues {}",tfvalue.toString());
					count.put(tfvalue.getURL(), tfvalue);
				} else {
					tfvalue = new TFValues(urldocument[0],Integer.valueOf(urldocument[1]));
					tfvalue.addCounter(i);
//					LOG.info("TFValues {}",tfvalue.toString());
					count.put(tfvalue.getURL(), tfvalue);
				}
			}
		}

		for (String k : count.keySet()) {
			if (count.get(k).getCounter() > 0){
				double tf = Double.valueOf(count.get(k).getCounter())/Double.valueOf(count.get(k).getDocument()); 
//				LOG.info("word: {} <---> Counter:{}", key +" <--> "+k, tf);
				
				context.write(key, new Text(String.valueOf(tf)+" "+count.get(k).getURL()));
			}
		}

	}

	class TFValues {
		private String url;
		private Integer documentSize;
		private int count = 0;

		public TFValues(String url, Integer documentSize) {
			setURL(url);
			setDocument(documentSize);
			this.count = 0;
		}

		public void setURL(String url) {
			this.url = url;
		}

		public String getURL() {
			return this.url;
		}

		public void setDocument(Integer documentSize) {
			this.documentSize = documentSize;
		}

		public Integer getDocument() {
			return this.documentSize;
		}

		public void addCounter(Integer val) {
			this.count = this.count + val;
		}

		public Integer getCounter() {
			return this.count;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return getCounter().toString() + " " + getDocument().toString();
		}

	}

}
