package com.orange.tfidf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.LinkDb;
import org.apache.nutch.crawl.NutchWritable;
import org.apache.nutch.parse.ParseData;
import org.apache.nutch.parse.ParseText;
import org.apache.nutch.util.HadoopFSUtil;
import org.apache.nutch.util.NutchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.util.URLUtil;


public class IDFDriver extends Configured implements Tool {

	public static Logger LOG = LoggerFactory.getLogger(IDFDriver.class);

	private String blacklist = "/home/xavier/workspace/Hadoop_TFIDF/blacklist.txt";

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub

		if (args.length < 2) {
			System.err
					.println("Usage:  -input <TFFolder> -total <TotalDocumetSize> -output <dir>");
			return -1;
		}
		Path inputTF = null;
		String totalDocumentSize = null;
		String outputdir = null;

		for (int i = 0; i < args.length; i++) {
			LOG.info("argument[{}]", args[i]);
			if (args[i].equals("-input")) {
				inputTF = new Path(args[++i]);
				LOG.info("path: INPUT:{}",inputTF);
			} else if (args[i].equals("-total")) {
				totalDocumentSize = args[++i];
				LOG.info("Total NUmber of documents:{}",totalDocumentSize);
			} else if (args[i].equals("-output")) {
				outputdir = args[++i];
				LOG.info("outputPath{}", outputdir);
			}
		}

		if (totalDocumentSize == null) {
			LOG.info("ERROR totalDocument is Null");
			return -1;
		}
		
		if (!StringUtils.isNumeric(totalDocumentSize)){
			LOG.info("Error no total size of documents {}",totalDocumentSize);
			return -1;
		}
		
		
		Configuration conf = getConf();
		Job job = new Job(conf, "IDF");
		job.getConfiguration().set("url.blacklist.keywords", blacklist);
		job.getConfiguration().set("total.number.documents", totalDocumentSize);
		// Distributed cache
		

		if ( inputTF == null){
			LOG.info("No input data");
			return -1;
		}
		LOG.info("IDFMAPPER: adding segment: " + inputTF);
		FileInputFormat.addInputPath(job, inputTF);

		// FileInputFormat.addInputPath(job, new Path(crawlDb,
		// CrawlDb.CURRENT_NAME));

	
		job.setMapperClass(IDFMapper.class);
		job.setReducerClass(IDFReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		//job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(outputdir));
		job.waitForCompletion(true);
		return 0;

	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new IDFDriver(), args);
	}

}
