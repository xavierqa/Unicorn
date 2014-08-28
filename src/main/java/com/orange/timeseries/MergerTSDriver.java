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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class MergerTSDriver extends Configured implements Tool {

	private static Logger LOG = LoggerFactory.getLogger(MergerTSDriver.class);
	
	
	 public static class CounterMapper extends Mapper
	 {
	  public void map(Text key, Text value, Context context)
	  throws IOException, InterruptedException
	  {
	   String[] line=value.toString().split("\t"); 

	   context.write(new Text(line[0]), new IntWritable(Integer.valueOf(line[1])));
	  }
	 }


	 public static class CountertwoMapper extends Mapper
	 {
	  public void map(Text key, Text value, Context context)
	  throws IOException, InterruptedException
	  {
	   String[] line=value.toString().split("\t");
	   context.write(new Text(line[0]), new IntWritable(Integer.valueOf(line[1])));
	  }
	 }

	 public static class CounterReducer extends Reducer
	 {
	  String line=null;

	  public void reduce(Text key, Iterable<Text> values, Context context ) 
	  throws IOException, InterruptedException
	  {
	   
	   for(Text value : values)
	   {
	    line = value.toString();
	   }

	   context.write(key, new Text(line));
	 }
	}
	 
	 
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
		MultipleInputs.addInputPath(job, newVector, TextInputFormat.class, CounterMapper.class);
		MultipleInputs.addInputPath(job, matrix, TextInputFormat.class, CountertwoMapper.class);
		
		 FileOutputFormat.setOutputPath(job, outputdir);
		 job.setReducerClass(CounterReducer.class);
		// job.setNumReduceTasks(1);
		 job.setOutputKeyClass(Text.class);
		 job.setOutputValueClass(Text.class);
		 
		 return (job.waitForCompletion(true) ? 0 : 1);
		
		
	}
	
	 public static void main(String[] args) throws Exception {

		  
		  int ecode = ToolRunner.run(new MergerTSDriver(), args);
		  System.exit(ecode);

		  
		 }

}
