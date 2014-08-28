package com.orange.timeseries;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimeSeriesReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	private static Logger LOG = LoggerFactory.getLogger(TimeSeriesReducer.class);
	
	protected void reduce(Text key, Iterable<IntWritable> values,	Context context)
			throws IOException, InterruptedException {
		
	//	Log.info("KEY {}",key);
		
		int sum = 1;
		
		for (IntWritable val : values){
			
			sum = sum + val.get();
			//LOG.info("VAL: {}",String.valueOf(val.get()));
		}
	
		
		context.write(key, new IntWritable(sum));
		
		
	}
	
}
