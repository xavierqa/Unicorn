package com.orange.tfidf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.orange.timeseries.TimeSeriesDriver;

public class TestTimeSeries {

	
	
	
	//@Test
	public void TestTimeSeries() throws Exception{
		String input = "/home/xavier/hadoop2.4/hadoop-2.4.0/unicorn_vector/part-r-00000";
		String output = "/home/xavier/hadoop2.4/hadoop-2.4.0/timeseries_1";
		FileUtils.deleteDirectory(new File(output));
		String []args = {"-input", input, "-output", output};
        TimeSeriesDriver.main(args);
	}
}
