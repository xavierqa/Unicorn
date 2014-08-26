package com.orange.tfidf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VectorTFIDFReducer extends Reducer<Text, Text, Text, Text> {

	public static Logger LOG = LoggerFactory
			.getLogger(VectorTFIDFReducer.class);

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		HashMap<Double, TimeSeries> vector = new HashMap<Double, TimeSeries>();
		List<Double> tfidflist = new ArrayList<Double>();
//		LOG.info("KEY {}", key);
		for (Text val : values) {
//			LOG.info("VALUES: {}", val);
			String[] v = val.toString().split(" ");
			String docKey = v[v.length - 1];
			Double tfidf = Double.valueOf(docKey);
			if (!tfidflist.contains(tfidf)) {
				tfidflist.add(tfidf);
			}

			TimeSeries timeserie = null;
			if (!vector.containsKey(tfidf)) {
				timeserie = new TimeSeries(tfidf);
			} else {
				timeserie = vector.get(tfidf);
			}
			for (int i = 0; i < v.length - 1; i++) {
				timeserie.addWord(v[i]);

			}
			vector.put(tfidf, timeserie);
		}

		/*
		 * Double[] keys = (Double[]) vector.keySet().toArray(new Double[0]);
		 * Arrays.sort(keys, Collections.reverseOrder()); for(Double k : keys) {
		 * LOG.info("Values {}{}",k, vector.get(k)); }
		 */

		Collections.sort(tfidflist, Collections.reverseOrder());
		// LOG.info("vector {} ", vector.toString());
		// LOG.info("Sorted list {}", sorted.toString());
		// context.write(new Text(String.valueOf(key)), new
		// Text(vector.toString()));

		for (Double tfidf : tfidflist) {
			if (tfidf != 0.0) {
				context.write(new Text(String.valueOf(key)), new Text(vector
						.get(tfidf).toString()));
			}
		}
		/*
		 * if (sorted.size() > 5) { List<Double> top5 = new
		 * ArrayList<Double>(sorted.subList( sorted.size() - 5, sorted.size()));
		 * List<String> words = new ArrayList<String>(); for ( Double k : top5){
		 * words.add(vector.get(k).toString()); }
		 * 
		 * 
		 * }
		 */
		// LOG.info("INVALID KEY:{}", key);

	}

	class TimeSeries {

		ArrayList<String> words;
		
		Double tfidf;

		public TimeSeries(Double tfidf) {
			// TODO Auto-generated constructor stub
			words = new ArrayList<String>();
			
			this.tfidf = tfidf;
		}

		public void addWord(String word) {
			this.words.add(word);
		}

		public String getTime(){
			SimpleDateFormat time = new SimpleDateFormat("dd/M/yyyy");
			String date = time.format(new Date());
			return date;
		}
		
		private String getVector(){
			StringBuffer v = new StringBuffer();
			for (int i = 0; i< words.size() ; i++){
				if ( i == 0){
					v.append(words.get(i));
				}else{
					v.append(",");
					v.append(words.get(i));
				}
			}
			return v.toString();
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub

			return  getTime() + "\t" + String.valueOf(this.tfidf)
					+ "\t" + getVector();
		}

	}
}
