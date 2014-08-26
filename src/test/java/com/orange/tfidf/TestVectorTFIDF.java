package com.orange.tfidf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVectorTFIDF{
	public static Logger LOG = LoggerFactory.getLogger(TestVectorTFIDF.class);
	
	
	//@Test
	public void testVector() throws Exception{
		String input = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn_test_idf/";
		String output = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn_test_vector";
		FileUtils.deleteDirectory(new File(output));
		String []args = {"-input", input, "-output",output};
		VectorTFIDFDriver.main(args);
	}
}
