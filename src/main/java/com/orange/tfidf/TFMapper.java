package com.orange.tfidf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import weka.core.Stopwords;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.stemmers.Stemming;



public class TFMapper extends Mapper<Object, Object, Text, Text>{
	
	public static Logger LOG = LoggerFactory.getLogger(TFMapper.class);
	
	
	
	
	@Override
	protected void map(Object key, Object values, Context context)
			throws IOException, InterruptedException {
		Stopwords stop = new Stopwords();
		//Stemmer stemmer = new LovinsStemmer();
		
		if (values == null){
			return;
		}
		StanfordCoreNLP pipeline;
		Properties props;
		props = new Properties();
		// props.setProperty("annotators",
		// "tokenize, ssplit, parse, sentiment");
		props.setProperty("annotators", "tokenize, ssplit,pos,lemma");

		// StanfordCoreNLP loads a lot of models, so you probably
		// only want to do this once per execution
		pipeline = new StanfordCoreNLP(props);
		String data = String.valueOf(values);
		List<String> lemmas = new LinkedList<String>();
		String documentText = data;
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(documentText);

		// run all Annotators on this text
		pipeline.annotate(document);

		// Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	
		for (CoreMap sentence : sentences) {

			// Iterate over all tokens in a sentence
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {

				// Retrieve and add the lemma for each word into the list of
				// lemmas
				String lemma = token.get(LemmaAnnotation.class).toLowerCase();
//				LOG.info("DATA {}",lemma);
				if (StringUtils.isAlpha(lemma)) {
					
					if (!stop.isStopword(lemma)) {
//						LOG.info("DATA OUTPUT {}",lemma);
						lemmas.add(lemma);
					}
				}
			}
		}

		//LOG.info("LEMAS {}", lemmas);
//		LOG.info("Lemma:{}", lemmas.toString());
		for( String l : lemmas){
			context.write(new Text(l), new Text(key+" "+lemmas.size()));
		}
		
		
/*	//	LOG.info("KEY:{}",key);
		//LOG.info("VALUES: {}",values);
		String data = String.valueOf(values);
		List<String> list = new ArrayList<String>();
		List<String> dlist = new ArrayList<String>();
		int pos = 0, end;
		while ((end = data.indexOf(' ', pos)) >= 0) {
		//	LOG.info("END: {} POS: {}", end, pos);
			String d = data.substring(pos, end).toLowerCase();

			if (StringUtils.isAlpha(d)) {
				if (!stop.isStopword(d)) {
					if (list.size() > 1) {
						String w = list.get(list.size() - 1);
						dlist.add(w + " " + d);
					}
					list.add(d);
				}
			}

			pos = end + 1;
		}

		for (String l : dlist) {
			list.add(l);
		}
		for (String l : list){
			context.write(new Text(l), new Text(key+" "+list.size()));
		}*/
	}
}


