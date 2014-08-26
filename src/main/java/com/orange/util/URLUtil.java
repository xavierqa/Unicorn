package com.orange.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLUtil {

	public static Logger LOG = LoggerFactory.getLogger(URLUtil.class);

	private Configuration conf = null;
	private List<Integer> urlblack = null;

	public URLUtil(Configuration conf) throws URISyntaxException,
			IllegalArgumentException, IOException {
		this.conf = conf;
		String Urlblacklist = this.conf.get("url.blacklist.keywords");

		if (Urlblacklist == null) {
			LOG.info("THE VALUE IS NULL");
			return;
		}
		LOG.info("PATH:{}", Urlblacklist);
		// File file = new File(URLBlacklist);

		urlblack = new ArrayList<Integer>();
		initBlackList(Urlblacklist);
	}

	private void initBlackList(String urlblacklist) throws IOException {

		Path pt = new Path(urlblacklist);
		FileSystem fs = FileSystem.get(new Configuration());
		BufferedReader br = new BufferedReader(new InputStreamReader(
				fs.open(pt)));
		String line;
		line = br.readLine();
		while (line != null) {
			line = br.readLine();
			if (line != null) {
				LOG.info("line {}", line);
				urlblack.add(line.hashCode());
			}
		}

	}

	public Boolean isBlackList(String url) {
		Boolean nogood = false;

		if (this.urlblack.contains(url.hashCode())) {
			nogood = true;
		}
		return nogood;
	}

}
