package com.orange.tfidf;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.tools.GetConf;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.NutchWritable;
import org.apache.nutch.net.URLFilters;
import org.apache.nutch.parse.ParseData;
import org.apache.nutch.parse.ParseText;
import org.apache.nutch.util.NutchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Stopwords;

import com.orange.util.URLUtil;

public class IDFMapper extends Mapper<Object, Object, Text, Text>{
	
	public static Logger LOG = LoggerFactory.getLogger(IDFMapper.class);
	private URLUtil urlUtil = null;
	
	
	
	@Override
	protected void map(Object key, Object values, Context context)
			throws IOException, InterruptedException {
		
		
		if (values == null){
			return;
		}
	
		StringTokenizer tokens = new StringTokenizer(values.toString());
		
		//LOG.info("SIZE {}",vals.length);
//		LOG.info("VALUE {}", values.toString());
		ArrayList<String> finalvalues = new ArrayList<String>();
		
		while(tokens.hasMoreTokens()){
			finalvalues.add(tokens.nextToken());
		}
		/*for (int i =0 ; i < vals.length ; i++){
			if (!vals[i].isEmpty()){
				finalvalues.add(vals[i]);
			}
		}*/
		
		int size = finalvalues.size();
		String keymapper=null; 
		String valuesreducer = null;
		
		switch (size){
				case 4:
					keymapper = finalvalues.get(0)+ " " +finalvalues.get(1); 
					valuesreducer = finalvalues.get(2) + " " +finalvalues.get(3);
					break;
				case 3:
					keymapper = finalvalues.get(0); 
					valuesreducer = finalvalues.get(1) + " " +finalvalues.get(2);
					break;
				default:
		}		
		
		context.write(new Text(keymapper),new Text(valuesreducer));
		
//		context.write(new Text(val[0]),new Text(val[1]));
/*		if (val.length == 2){
			
			
		}else if ( val.length == 3){
			context.write(new Text(val[0]+" "+val[1]),new Text(val[2]+'\t'+val[3]));
		}else{
			
		}*/
		
		
//		
//		
//		
//		ArrayList<String> wordgroups = new ArrayList<String>();
//		LOG.info("TOTAL TOKENS {}",tokens.countTokens());
//		for (int i = 0; tokens.hasMoreTokens() ; i++){
//			String t = tokens.nextToken();
//			if(StringUtils.isAlpha(t)){
//		
//				if(stop.isStopword(t)){
//					LOG.info("STOP World -->: {}",t);
//				}else{
//					LOG.info("No STOP world -->: {}",t);
//				}
//			}
//			
//		}
		
				
	
	}
}
