#!/bin/bash +x

#
# The system works with Nutch version 1.7
#
#

#### SET Java Home
#if [ -z $JAVA_HOME ]; then
#	export JAVA_HOME=/usr/lib/jvm/default-java
	
#fi 	

### SET Nutch Home
#if [ -z $NUTCH_HOME ]; then
#	NUTCH_HOME=/home/xavier/nutchTest
#	if [ ! -d $NUTCH_HOME ]; then
#		mkdir $NUTCH_HOME
#	fi	
	
#fi


#if [ -z $UNICORN_HOME ]; then
#	UNICORN_HOME=/home/xavier/workspace/Hadoop_TFIDF
#fi

#Environment variables

. conf/unicorn-env.sh

echo $NUTCH_HOME
### GET NUTCH, decompress 

#NUTCH_VERSION=apache-nutch-1.7-src.tar.gz

if [ ! -d $NUTCH_HOME/nutch ]; then
	NUTCH_WEB=https://github.com/apache/nutch
	cd $NUTCH_HOME; git clone $NUTCH_WEB
#	tar zxvf $NUTCH_HOME/$NUTCH_VERSION -C $NUTCH_HOME --strip-components=1
fi


##Compile nutch
if [ ! -d "$NUTCH_HOME/nutch/runtime" ]; then
	cd $NUTCH_HOME/nutch; ant
fi

##Configure Nutch

cp $UNICORN_HOME/conf/crawl $NUTCH_HOME/nutch/runtime/local/bin/
cp $UNICORN_HOME/conf/nutch-site.xml $NUTCH_HOME/nutch/runtime/local/conf/

if [ ! -d "$NUTCH_HOME/nutch/runtime/local/urls" ]; then 
	mkdir $NUTCH_HOME/nutch/runtime/local/urls
fi

cp $UNICORN_HOME/conf/seed.txt $NUTCH_HOME/nutch/runtime/local/urls/seed.txt

##

