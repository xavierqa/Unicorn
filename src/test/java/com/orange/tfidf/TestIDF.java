package com.orange.tfidf;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestIDF {

public static Logger LOG = LoggerFactory.getLogger(TestIDF.class);

	
	//@Test
	public void readMetadata() throws Exception{
		String input = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn/";
		String output = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn_test_idf";
		String total = "2";
		FileUtils.deleteDirectory(new File(output));
		String []args = {"-input", input, "-total", total , "-output",output};
        IDFDriver.main(args);
	}
	
//	@Test
	public void testReadData(){
		//String data="Ad      0.0017605633802816902 http://techcrunch.com/";
		String data="Alex Wilhelm          0.0017605633802816902 http://techcrunch.com/";
		StringTokenizer t1 = new StringTokenizer(data);
		//StringTokenizer t2 = new StringTokenizer(data1);
		ArrayList<String> da = new ArrayList<String>();
		while(t1.hasMoreTokens()){
			da.add(t1.nextToken());
		}
/*		ArrayList<String> da1 = new ArrayList<String>();
		while(t2.hasMoreTokens()){
			da1.add(t2.nextToken());
		}
*/		
		
		int size = da.size();
		String key=null; 
		String values = null;
		
		switch (size){
				case 4:
					key = da.get(0)+ " " +da.get(1); 
					values = da.get(2) + " " +da.get(3);
					break;
				case 3:
					key = da.get(0); 
					values = da.get(1) + " " +da.get(2);
					break;
				default:
		}		
				
		LOG.info("DA {}",da.toString());
		LOG.info("KEY {}", key);
		LOG.info("VALUE {}",values);
//		LOG.info("DA1 {}",da1.toString());
	}
}
