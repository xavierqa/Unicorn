package com.orange.tfidf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VectorTFIDFMapper extends Mapper<Object, Text, Text, Text> {

	public static Logger LOG = LoggerFactory.getLogger(VectorTFIDFMapper.class);

	@Override
	protected void map(Object key, Text tfidfue, Context context)
			throws IOException, InterruptedException {

		StringTokenizer tokens = new StringTokenizer(tfidfue.toString());
		String keymapper = null;
		String tfidfuesreducer = null;
		ArrayList<String> tfidf = new ArrayList<String>();
		while(tokens.hasMoreTokens()){
			tfidf.add(tokens.nextToken());
		}
		
		switch (tfidf.size()) {
		case 4:
			keymapper = tfidf.get(3); 
			tfidfuesreducer = tfidf.get(0)+ " "+tfidf.get(1) + " " + tfidf.get(2);
			break;
		case 3:
			keymapper = tfidf.get(2);
			tfidfuesreducer = tfidf.get(0) + " " + tfidf.get(1);
			break;
		default:
		}
	//	LOG.info("Keytfidfue: {}", keymapper);
		

		context.write(new Text(keymapper), new Text(tfidfuesreducer));

	}

}
