package com.orange.tfidf;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.WordStemmer;
import edu.stanford.nlp.util.CoreMap;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Stopwords;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;

public class WekaTest {

	public static Logger LOG = LoggerFactory.getLogger(WekaTest.class);

	private String data = "Gigaom Events Research researches Subscribe is Gigaom Newsletters fantasized Uber Big Data"

			+ "Don't miss out. Sign up to get all the news you need delivered right to your inbox. "
			+ "Get started You're subscribed! If you like, you can update your settings Gigaom Research Individual "
			+ "subscription Get access to Gigaom Research for a year. Get started You're subscribed! Review details here . "
			+ "Corporate subscription Company-wide access to Gigaom Research, analyst briefings or inquiries, Gigaom Event tickets & much more. "
			+ "Get started Coming up at Gigaom Editorial Events Meetups Webinars All posts All events All meetups All webinars Follow us "
			+ "Follow @gigaom Become a fan Connect with us Subscribe to newsletter Get feed Cloud Data Media Mobile Science & Energy Social & "
			+ "Web Podcasts ★ Must-reads Apple Q3 earnings: 35 million iPhones sold, but iPad sales continue to dip Microsoft cites "
			+ "(what else?) strong cloud growth in Q4 earnings EMC, with Elliott Management at the door, braces for a showdown Apple patent "
			+ "filing reveals ideas for a smartwatch labeled ‘iTime’ Google pinned in Texas patent swamp by Rockstar, the Apple-backed troll "
			+ "Stories for Jul. 22, 2014 In Brief Watch SpaceX’s Falcon 9 rocket land in the Atlantic Ocean By Signe Brewster 47 mins ago Jul. "
			+ "22, 2014 - 3:02 PM PDT SpaceX’s Falcon 9 rocket is now equipped with landing legs, which could eventually allow it to be "
			+ "reusable–a crucial step toward lowering the cost of carrying cargo to space. The space startup released a video today taken "
			+ "from the surface of the rocket as it passed through the planet’s atmosphere and crashed into the Atlantic Ocean. "
			+ "“The water impact caused loss of hull integrity, but we received all the necessary data to achieve a successful landing on a "
			+ "future flight,” a blog post states . “At this point, we are highly confident of being able to land successfully on a floating "
			+ "launch pad or back at the launch site and refly the rocket with no required refurbishment.” "
			+ "As AppDynamics lands $120 million, its focus shifts to expansion and new products By Jonathan Vanian 1 hour ago Jul. 22, 2014 - "
			+ "2:47 PM PDT photo: Shutterstock / ramcreations As the startup today said it received $120 million in funding, AppDynamics "
			+ "hopes to eventually branch out from only doing monitoring and analytics and plans to develop ways to remediate errors in the "
			+ "system. Read more » Sponsored post: Nearly 90 percent surveyed stop using apps due to poor performance By Gigaom 2 Big Data "
			+ "hours ago Jul. 22, 2014 - 1:55 PM PDT When applications fail to perform, consumers are frequently feeling disappointment and "
			+ "frustration. The App Attention span examines how increased mobile device usage and performance expectations are impacting consumer"
			+ " behavior and what implications this has on businesses. Read more » Upcoming Events Gigaom Structure Connect 2014 Building "
			+ "the Internet of Things October 21 – 22, 2014 San Francisco, CA Gigaom Roadmap 2014 The Intersection of Design and Experience "
			+ "November 18 - 19, 2014 San Francisco, CA Advertisement Must read Apple Q3 earnings: 35 million iPhones sold, but iPad sales "
			+ "continue to dip By Kif Leswing 2 hours ago Jul. 22, 2014 - 1:35 PM PDT Must read Microsoft cites (what else?) strong cloud growth "
			+ "in Q4 earnings By Barb Darrow 3 hours ago Jul. 22, 2014 - 1:18 PM PDT Netflix comes to France with enough capacity to supply a "
			+ "small ISP By Stacey Higginbotham 3 hours ago Jul. 22, 2014 - 12:30 PM PDT This British startup is building a super solar "
			+ "material By Ucilia Wang 4 hours ago Jul. 22, 2014 - 11:37 AM PDT  MTV teams up with Spotify to deliver music to apps and sites "
			+ "By Janko Roettgers 5 hours ago Jul. 22, 2014 - 10:38 AM PDT Report: The U.S. joins 6 other countries in achieving 100% wireless "
			+ "data penetration By Kevin Fitchard 5 hours ago Jul. 22, 2014 - 10:32 AM PDT Must read Apple patent filing reveals ideas for a smartwatch "
			+ "labeled ‘iTime’ By Kif Leswing 5 hours ago Jul. 22, 2014 - 10:21 AM PDT Join the conversation Advertisement Gigaom Research webinar: "
			+ "building content-centric applications: the new shift in enterprise content management By Gigaom 7 hours ago Jul. 22, 2014 - 8:55 AM PDT "
			+ "Google’s $1M contest to shrink the power inverter is now ready for you By Katie Fehrenbacher 7 hours ago Jul. 22, 2014 - 8:46 AM "
			+ "PDT Must read Google pinned in Texas patent swamp by Rockstar, the Apple-backed troll By Jeff John Roberts 8 hours ago Jul. 22, 2014 - 8:10 AM PDT 5 "
			+ "Comments SAP throws in with the OpenStack crowd By Barb Darrow 8 hours ago Jul. 22, 2014 - 8:01 AM PDT So long, Android: Samsung brings Tizen update to "
			+ "Galaxy Gear smartwatch By Kevin C. Tofel 8 hours ago Jul. 22, 2014 - 7:49 AM PDT  Verizon’s mobile growth engine revs back up, fueled by the tablet "
			+ "By Kevin Fitchard 8 hours ago Jul. 22, 2014 - 7:33 AM PDT Red-hot Chinese smartphone maker Xiaomi announces an LTE phone and a $13 fitness "
			+ "bracelet By Kif Leswing 9 hours ago Jul. 22, 2014 - 6:53 AM PDT 3 Comments Google could face criminal proceedings in Italy if it doesn’t clean up its act "
			+ "on privacy By David Meyer 9 hours ago Jul. 22, 2014 - 6:45 AM PDT In Brief At long last, touchscreen Chromebooks gain pinch-to-zoom "
			+ "support By Kevin C. Tofel 10 hours ago Jul. 22, 2014 - 6:09 AM PDT GE’s got a new in-house fuel cell startup By Katie Fehrenbacher "
			+ "10 hours ago Jul. 22, 2014 - 6:00 AM PDT 1 Comment Making optical cables out of air could boost communication in space By Signe Brewster 10 hours ago "
			+ "Jul. 22, 2014 - 6:00 AM PDT 2 Comments The Nvidia Shield is a high-powered tablet optimized for gaming By Kif Leswing 10 hours "
			+ "ago Jul. 22, 2014 - 6:00 AM PDT 1 Comment Sponsored post: Werner Vogels, AWS #cloudfather, bets on the importance of log management "
			+ "By Gigaom 10 hours ago Jul. 22, 2014 - 5:55 AM PD";

//	@Test
	public void TestNLP() {
		StanfordCoreNLP pipeline;
		Properties props;
		props = new Properties();
		// props.setProperty("annotators",
		// "tokenize, ssplit, parse, sentiment");
		props.setProperty("annotators", "tokenize, ssplit,pos,lemma");

		// StanfordCoreNLP loads a lot of models, so you probably
		// only want to do this once per execution
		pipeline = new StanfordCoreNLP(props);

		List<String> lemmas = new LinkedList<String>();
		String documentText = data;
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(documentText);

		// run all Annotators on this text
		pipeline.annotate(document);

		// Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		Stopwords stop = new Stopwords();
		for (CoreMap sentence : sentences) {

			// Iterate over all tokens in a sentence
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {

				// Retrieve and add the lemma for each word into the list of
				// lemmas
				String lemma = token.get(LemmaAnnotation.class).toLowerCase();
				if (StringUtils.isAlpha(lemma)) {
					if (!stop.isStopword(lemma)) {
						lemmas.add(lemma);
					}
				}
			}
		}

		LOG.info("LEMAS {}", lemmas);

		// Stemmer snow = new SnowballStemmer();
	}

	// @Test
	public void TestWeka() throws ParseException {
		Stopwords stop = new Stopwords();
		Stemmer stemmer = new LovinsStemmer();
		Stemmer snow = new SnowballStemmer();
		Stemmer iterm = new IteratedLovinsStemmer();

		StringTokenizer token = new StringTokenizer(data);
		LOG.info("SIZE:{}", token.countTokens());
		// for ( int r = 0 ; r < token.countTokens() ; r++){
		List<String> list = new ArrayList<String>();
		List<String> dlist = new ArrayList<String>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		int pos = 0, end;
		while ((end = data.indexOf(' ', pos)) >= 0) {
			LOG.info("END: {} POS: {}", end, pos);
			String d = data.substring(pos, end);

			if (StringUtils.isAlpha(d)) {
				if (!stop.isStopword(d)) {
					LOG.info("No STOP world -->: {}", d);
					String tmp = stemmer.stem(d);
					LOG.info("Stemmed words {}", tmp);
					String s = snow.stem(d);
					LOG.info("Stemmed snow {}", s);
					String i = iterm.stem(d);
					LOG.info("Stemmed iter {}", i);
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

		// }
		/*
		 * StringTokenizer tokens = new StringTokenizer(data); for (int i = 0;
		 * tokens.hasMoreTokens() ; i++){ String t = tokens.nextToken();
		 * if(StringUtils.isAlpha(t)){
		 * 
		 * if(stop.isStopword(t)){ LOG.info("STOP World -->: {}",t); }else{
		 * 
		 * LOG.info("No STOP world -->: {} {}",t, tokens.nextElement() ); } }
		 * 
		 * }
		 */
	}
}
