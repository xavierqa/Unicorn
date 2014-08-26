package com.orange.tfidf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.util.URLUtil;

public class URLMapper extends Mapper<Object, Object, Text, Text>{
	
	public static Logger LOG = LoggerFactory.getLogger(URLMapper.class);
	private URLUtil urlUtil = null;
	
	@Override
	protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
			throws IOException, InterruptedException {
		//FileReader reader_ = new FileReader(new File())
		try {
			urlUtil = new URLUtil(context.getConfiguration());
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String getUrlKey(String url) {
		
		String hostname = null; 
		
		if ( url != null){
			URI u;
			try {
				u = new URI(url);
				hostname = u.getHost();
			} catch (URISyntaxException e) {
				LOG.error("URL ERROR:{}",e.getMessage());
				
			}
			
		}
	    return hostname;
	  }
	
	
	@Override
	protected void map(Object key, Object values, Context context)
			throws IOException, InterruptedException {

		LOG.info("KEY ----> {}", key.toString());
		LOG.info("VALUE -->{}",values);
		context.write(new Text(key.toString()), new Text(String.valueOf(values)));	
			
/*			String baseURL = getUrlKey(String.valueOf(key));
			if ( urlUtil.isBlackList(String.valueOf(key))){
	//			LOG.info("BLACK-LIST-URL {}",key);
				return;
			}else{
				if (baseURL!=null && key !=null){
					context.write(new Text(baseURL), new Text(String.valueOf(key)));
				}
					
			}*/
	}

}
