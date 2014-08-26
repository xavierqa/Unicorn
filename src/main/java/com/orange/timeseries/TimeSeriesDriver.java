package com.orange.timeseries;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.tfidf.VectorTFIDFDriver;
import com.orange.tfidf.VectorTFIDFMapper;
import com.orange.tfidf.VectorTFIDFReducer;

public class TimeSeriesDriver extends Configured implements Tool {

	public static Logger LOG = LoggerFactory.getLogger(TimeSeriesDriver.class);

	
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub

		if (args.length < 2) {
			System.err
					.println("Usage:  -input <Vector> -output <TimeSeries> ");
			return -1;
		}
		Path inputVector = null;
		String outputdir = null;
		

		for (int i = 0; i < args.length; i++) {
			LOG.info("argument[{}]", args[i]);
			if (args[i].equals("-input")) {
				inputVector = new Path(args[++i]);
				LOG.info("path: INPUT:{}",inputVector);
			} else if (args[i].equals("-output")) {
				outputdir = args[++i];
				LOG.info("outputPath{}", outputdir);
			}
		}

		
		
		Configuration conf = getConf();
		Job job = new Job(conf, "TimeSeries");
		// Distributed cache
		

		if ( inputVector == null){
			LOG.info("No input data");
			return -1;
		}
		LOG.info("IDFMAPPER: adding segment: " + inputVector);
		FileInputFormat.addInputPath(job, inputVector);

		// FileInputFormat.addInputPath(job, new Path(crawlDb,
		// CrawlDb.CURRENT_NAME));

	
		job.setMapperClass(TimeSeriesMapper.class);
		job.setReducerClass(TimeSeriesReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		//job.setInputFormatClass(SequenceFileInputFormat.class);
		//job.setOutputKeyClass(Text.class);
		//job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(outputdir));
		job.waitForCompletion(true);
		return 0;

	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new TimeSeriesDriver(), args);
	}
}
