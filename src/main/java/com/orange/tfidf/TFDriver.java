package com.orange.tfidf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.nutch.crawl.LinkDb;
import org.apache.nutch.parse.ParseText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.util.URLUtil;

public class TFDriver extends Configured implements Tool {

	public static Logger LOG = LoggerFactory.getLogger(TFDriver.class);

	private String blacklist = "/home/xavier/workspace/Hadoop_TFIDF/blacklist.txt";

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub

		if (args.length < 2) {
			System.err
					.println("Usage:  -crawldb <crawldb> -linkdb <linkdb> -dir <segment> -output <dir>");
			return -1;
		}
		Path linkDb = null;
		Path crawlDb = null;
		String outputdir = null;
		List<Path> segments = new ArrayList<Path>();

		for (int i = 0; i < args.length; i++) {
			LOG.info("argument[{}]", args[i]);
			if (args[i].equals("-linkdb")) {
				LOG.info("path: linkdb:" + args[i++]);
				linkDb = new Path(args[i++]);
			} else if (args[i].equals("-dir")) {
				Path ps = new Path(args[++i]);
				segments = Arrays.asList(ps);
			} else if (args[i].equals("-crawldb")) {
				LOG.info("path" + args[i++]);
				crawlDb = new Path(args[i++]);
			} else if (args[i].equals("-output")) {
				LOG.info("path" + args[i++]);
				outputdir = args[i++];
			}
		}

		if (crawlDb == null) {
			LOG.info("ERROR crawldb is null");
			return -1;
		}

		Configuration conf = getConf();
		Job job = new Job(conf, "TFIDF");
		job.getConfiguration().set("url.blacklist.keywords", blacklist);
		// Distributed cache
		URLUtil util = new URLUtil(job.getConfiguration());

		LOG.info("TFIDFDriver: crawldb: " + crawlDb);

		if (linkDb != null)
			LOG.info("TFIDFDriver: linkdb: " + linkDb);

		for (final Path segment : segments) {
			LOG.info("TFIDFMAPPER: adding segment: " + segment);
			// FileInputFormat.addInputPath(job, new Path(segment,
			// CrawlDatum.FETCH_DIR_NAME));
			// FileInputFormat.addInputPath(job, new Path(segment,
			// CrawlDatum.PARSE_DIR_NAME));
			// FileInputFormat.addInputPath(job, new Path(segment,
			// ParseData.DIR_NAME));
			FileInputFormat.addInputPath(job, new Path(segment,
					ParseText.DIR_NAME));
			//FileInputFormat.addInputPath(job, new Path("/home/xavier/workspace/Hadoop_TFIDF/src/test/resources/test.txt"));
			
		}

		// FileInputFormat.addInputPath(job, new Path(crawlDb,
		// CrawlDb.CURRENT_NAME));

		if (linkDb != null)
			FileInputFormat.addInputPath(job, new Path(linkDb,
					LinkDb.CURRENT_NAME));

		job.setMapperClass(TFMapper.class);
		job.setReducerClass(TFReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(outputdir));
		job.waitForCompletion(true);
		return 0;

	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new TFDriver(), args);
	}

}
