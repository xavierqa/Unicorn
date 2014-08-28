package com.orange.timeseries;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSeriesMapper extends Mapper<Object, Text, Text, IntWritable>{

	
	private static Logger LOG = LoggerFactory.getLogger(TimeSeriesMapper.class);
	
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		
		String [] words = value.toString().split("\t");
		if (words.length >2 ){
		//	LOG.info("Value {}",words[3]);
			String [] keywords = words[3].split(",");
			IntWritable one = new IntWritable(1);
			for (String keyword : keywords){
			//	LOG.info("keyword {}",keyword);
				context.write(new Text(keyword), one);
			}
		}
		
	}
	
	
}
