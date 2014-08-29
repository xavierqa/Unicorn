package com.orange.timeseries;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergerTSReducer extends Reducer<Text, IntWritable, Text, Text> {
	private static Logger LOG = LoggerFactory.getLogger(MergerTSReducer.class);

}
