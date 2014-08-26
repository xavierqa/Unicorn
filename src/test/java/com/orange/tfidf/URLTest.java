package com.orange.tfidf;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.nutch.net.URLFilterException;
import org.apache.nutch.net.URLFilters;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.util.URLUtil;

public class URLTest {

	public static Logger LOG = LoggerFactory.getLogger(URLTest.class);
	
	//@Test
	public void testApp() throws Exception{
		String base = "/home/xavier/nutch/nutch/runtime/local/output1";
		String crawldb = base+"/crawldb";
		String linkdb = base+"/linkdb";
		String segments = base+"/segments/20140812145925";
		String output = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn";
		FileUtils.deleteDirectory(new File(output));
		String []args = {"-crawldb", crawldb, "-linkdb", linkdb, "-dir", segments, "-output",output};
		URLDriver.main(args);
	}
	
	//@Test
	public void testURL() throws IOException, IllegalArgumentException, URISyntaxException{
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "TFIDF");
		String blacklist = "/home/xavier/workspace/Hadoop_TFIDF/blacklist.txt";
		job.getConfiguration().set("url.blacklist.keywords", blacklist);
	}
}
