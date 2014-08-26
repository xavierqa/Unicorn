package com.orange.tfidf;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Options;
import org.apache.nutch.parse.ParseData;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.segment.SegmentReader;
import org.apache.nutch.util.HadoopFSUtil;
import org.apache.nutch.util.NutchConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTF {

	public static Logger LOG = LoggerFactory.getLogger(TestTF.class);

	
	//@Test
	public void readMetadata() throws Exception{
		String base = "/home/xavier/nutch/nutch/runtime/local/crawl.tst";
		String crawldb = base+"/crawldb";
		String linkdb = base+"/linkdb";
		String segments = base+"/segments/20140806164043/";
		String output = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn";
		FileUtils.deleteDirectory(new File(output));
		String []args = {"-crawldb", crawldb, "-linkdb", linkdb, "-dir", segments, "-output",output};
        TFDriver.main(args);
	}
	
	
	private void getPaths(Path path) throws IOException{
		
		Configuration conf = NutchConfiguration.create();
		FileSystem fs = path.getFileSystem(conf);
        FileStatus[] fstats = fs.listStatus(path,
                HadoopFSUtil.getPassDirectoriesFilter(fs));
        Path[] files = HadoopFSUtil.getPaths(fstats);
        LOG.info("Size:"+String.valueOf(files.length));
      //  LOG.info(files[0].getName());
        for (Path p : files) {
            LOG.info(p.getName());
        }
	}
	
	//@Test
	public void readNutch() throws Exception {
		LOG.debug("This is a test");

		Configuration conf = NutchConfiguration.create();
		Path segment = new Path(
				"/home/xavier/nutch/nutch/runtime/local/crawl.tst/segments/20140722155231/parse_text/part-00000/data");
		FileSystem fs = FileSystem.get(conf);
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, segment, conf);
		Text key = new Text();
		Content content = new Content();
		//ParseData data = new ParseData();
		// Loop through sequence files
		while (reader.next(key, content)) {
			try {
				
				//LOG.info(content.getMetadata().size());
				for (String s : content.getMetadata().names()){
					LOG.info("data: {}",s);
				}
				//System.out.write(content.getContent(), 0, content.getContent().length);		
			} catch (Exception e) {
			}
		}

		/*
		 * Configuration conf = NutchConfiguration.create(); Options opts = new
		 * Options(); String[] args = {}; GenericOptionsParser parser = new
		 * GenericOptionsParser(conf, args); String[] remainingArgs =
		 * parser.getRemainingArgs(); log.debug("Size:",remainingArgs.length);
		 * String segment = remainingArgs[0]; log.debug("segments", segment);
		 * Path file = new Path(segment, Content.DIR_NAME + "/part-00000/data");
		 * SequenceFile.Reader reader = new SequenceFile.Reader(fs, file, conf);
		 * Text key = new Text(); ; // Loop through sequence files while
		 * (reader.next(key, content)) { try {
		 * System.out.write(content.getContent(), 0,
		 * content.getContent().length); } catch (Exception e) { } }
		 */

	}

}
