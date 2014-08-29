package com.orange.timeseries;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;


import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class MergerTSDriver extends Configured implements Tool {

	private static Logger LOG = LoggerFactory.getLogger(MergerTSDriver.class);
	
	
	 
	 
	@Override
	public int run(String[] args) throws Exception {
		if (args.length < 2) {
			System.err
					.println("Usage:  -input1 <NewVector> -input2 <matrix> -output <newmatrix> ");
			return -1;
		}
		Path newVector = null;
		Path matrix = null;
		Path outputdir = null;
		

		for (int i = 0; i < args.length; i++) {
			LOG.info("argument[{}]", args[i]);
			if (args[i].equals("-input1")) {
				newVector = new Path(args[++i]);
				LOG.info("path: INPUT:{}",newVector);
			} else if (args[i].equals("-input2")) {
				matrix = new Path(args[++i]);
				LOG.info("matrix {}", matrix);
			} else if (args[i].equals("-output")) {
				outputdir = new Path(args[++i]);
				LOG.info("outputPath {}", outputdir);
			}
		}
		LOG.info("MATRIX {}",matrix);
		LOG.info("Input {}",newVector);

		if (newVector == null || matrix == null || outputdir == null){
			LOG.info("Missing values");
			return 0;
		}
		
		Configuration conf = getConf();
		Job job = new Job(conf, "MergerTS");
		FileInputFormat.addInputPath(job, newVector);
	//	MultipleInputs.addInputPath(job, newVector, TextInputFormat.class, CounterMapper.class);
	//	MultipleInputs.addInputPath(job, matrix, TextInputFormat.class, CountertwoMapper.class);
		
		
		job.setMapperClass(MergerTSMapper.class);
		job.setReducerClass(MergerTSReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		//job.setInputFormatClass(SequenceFileInputFormat.class);
		//job.setOutputKeyClass(Text.class);
		//job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, outputdir);
		job.waitForCompletion(true);
		return 0;
		
		
	}
	
	 public static void main(String[] args) throws Exception {

		  
		  int ecode = ToolRunner.run(new MergerTSDriver(), args);
		  System.exit(ecode);

		  
		 }

}
