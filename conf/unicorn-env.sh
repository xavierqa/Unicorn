#!/bin/bash

#environment varialbles

#### SET Java Home
if [ -z $JAVA_HOME ]; then
        export JAVA_HOME=/usr/lib/jvm/default-java

fi

### SET Nutch Home
if [ -z $NUTCH_HOME ]; then
        NUTCH_HOME=/home/xavier/nutchTest
        if [ ! -d $NUTCH_HOME ]; then
                mkdir $NUTCH_HOME
        fi

fi


if [ -z $UNICORN_HOME ]; then
        UNICORN_HOME=/home/xavier/workspace/Unicorn/target
fi


if [ -z $HADOOP_HOME ]; then
	HADOOP_HOME=/home/xavier/hadoop/hadoop-2.5.0
fi

UNICORN_HOME=$UNICORN_HOME

OUTPUTURL=unicorn_url_n
OUTPUTTF=unicorn_tf_n
OUTPUTIDF=unicorn_idf_n
OUTPUTTFIDF=unicorn_tfidf_n
OUTPUTVECTOR=unicorn_vector
BLACKLISTFILE=/home/xavier/workspace/Unicorn/blacklist.txt
#NUTCH HOME

URLSEED=$NUTCH_HOME/nutch/runtime/local/urls
CRAWLEROUTPUT=$NUTCH_HOME/nutch/runtime/local/output
DEEP=1
NUTCHHOME=$NUTCH_HOME/nutch/runtime/local


BASEDIR=$CRAWLEROUTPUT
CRAWLDB=${BASEDIR}/crawldb/current
LINKDB=${BASEDIR}/linkdb/current
SEGMENTSDIR=${BASEDIR}/segments/
#OUTPUTPATH="-output ${OUTPUT}"
#OUTPUTTF="-output ${OUTTF}"

