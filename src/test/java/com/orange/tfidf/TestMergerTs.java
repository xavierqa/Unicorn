package com.orange.tfidf;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.orange.timeseries.MergerTSDriver;
import com.orange.timeseries.TimeSeriesDriver;

public class TestMergerTs {
	@Test
	public void TestTimeSeries() throws Exception{
		String input = "/home/xavier/hadoop2.4/hadoop-2.4.0/newVector.txt";
		String matrix = "/home/xavier/hadoop2.4/hadoop-2.4.0/matrix.txt";
		String output = "/home/xavier/hadoop2.4/hadoop-2.4.0/merger";
		FileUtils.deleteDirectory(new File(output));
		String []args = {"-input1", input,"-input2", matrix, "-output", output};
        MergerTSDriver.main(args);
	}
}
