package com.orange.crawl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorDriver extends Configured implements Tool{

	public static Logger log = LoggerFactory.getLogger(GeneratorDriver.class);
	
	
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		
		context.write(null, null);
	}
	
	
	private void reduce(Text arg0, Iterable<Text> arg1,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		context.write(arg0, arg1);
	}
	
	
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		JobConf job = new JobConf(conf, GeneratorDriver.class);
		
		Path in = new Path(args[1]);
		Path output = new Path(args[2]);
		
		job.setJobName("URL Generator");
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, output);
		
		
		return 0;
	}

	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(),new GeneratorDriver(), args);
	}
	
}
