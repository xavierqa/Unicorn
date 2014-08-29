package com.orange.timeseries;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergerTSMapper extends Mapper<Object, Text, Text, IntWritable>{

	private static Logger LOG = LoggerFactory.getLogger(MergerTSMapper.class);
	
	
	@Override
	protected void map(Object key, Text value,
			org.apache.hadoop.mapreduce.Mapper.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
	}
}
