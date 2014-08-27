#!/bin/bash +x

#if [ "$#" -ne 1 ];
#then
#	echo "<URLCOUNT> <TOTALDOCS> <TFIDF>"
#	exit 1
#fi


UNICORN_HOME=$UNICORN_HOME

OUTPUTURL=unicorn_url_n
OUTPUTTF=unicorn_tf_n
OUTPUTIDF=unicorn_idf_n
OUTPUTTFIDF=unicorn_tfidf_n
OUTPUTVECTOR=unicorn_vector

#NUTCH HOME

URLSEED=/home/xavier/nutch/nutch/runtime/local/urls
CRAWLEROUTPUT=/home/xavier/nutch/nutch/runtime/crawlerouput
DEEP=1
NUTCHHOME=/home/xavier/nutch/nutch/runtime/local
export JAVA_HOME=/usr/lib/jvm/default-java


BASEDIR=$CRAWLEROUTPUT
CRAWLDB=${BASEDIR}/crawldb/current
LINKDB=${BASEDIR}/linkdb/current
SEGMENTSDIR=${BASEDIR}/segments/
#OUTPUTPATH="-output ${OUTPUT}"
#OUTPUTTF="-output ${OUTTF}"


getSegments() {
	SEGMENT=`ls -c ${SEGMENTSDIR} | head -1 | awk '{print ($NF --)}'`
	echo "$SEGMENTSDIR$SEGMENT"
}

echo "LAUNCHING UNICOR DATA AGGREGATION:"

deleteOutput(){
	if [ -e "$1" ]; then
		echo "Deleting $1"
		rm -rf $1
	fi

}

getTotalFetchedDocument(){
NUTCHSTATS=`$NUTCHHOME/bin/nutch readdb $BASEDIR/crawldb/ -stats`
echo $NUTCHSTATS | awk -F"db_fetched): " '{print $2}' | awk -F" " '{print $1}'


}

#echo "removing file ${OUTPUTTF}"
#rm -rf ${OUTTF}
#echo $SEGMENT
#SEGMENTS="-dir ${SEGMENTSDIR}${SEGMENT}"
#echo $SEGMENTS
VAL=0
for input in "$@";
do
	echo $input
	
case "$input" in
	CRAWL)
		echo "crawling starting"
		 $NUTCHHOME/bin/crawl $URLSEED $CRAWLEROUTPUT -depth ${DEEP}
		 ;;

	URL)
		echo "Counting total Number of documents"
		totaldocuments=$(getTotalFetchedDocument)
		echo "TOTAL DOCUMENTS" $totaldocuments
#		deleteOutput $OUTPUTURL		
#		SEGMENTS=$(getSegments)
#		bin/hadoop jar  $UNICORN_HOME/orange-unicorn-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.orange.tfidf.URLDriver \
#		-crawldb ${CRAWLDB} -linkdb ${LINKDB} -dir ${SEGMENTS} -output ${OUTPUTURL}
#		echo "TFIDF"
		;;	
	TF)
		deleteOutput ${OUTPUTTF}
		SEGMENTS=$(getSegments)
		bin/hadoop jar  $UNICORN_HOME/orange-unicorn-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.orange.tfidf.TFDriver \
		-crawldb ${CRAWLDB} -linkdb ${LINKDB} -dir ${SEGMENTS} -output ${OUTPUTTF}
		echo "TFIDF"
		;;

	IDF)
		VAL=$(getTotalFetchedDocument)
		if [ $VAL = 0 ]; 
		then
			echo "Total Document is 0, running totalnumber of documents"
			echo "Calculating total documents"
			exit 1
		fi
		deleteOutput ${OUTPUTIDF}
		bin/hadoop jar  $UNICORN_HOME/orange-unicorn-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.orange.tfidf.IDFDriver \
		-input ${OUTPUTTF} -total $VAL -output ${OUTPUTIDF} 
		;;

	VECTOR)

		deleteOutput ${OUTPUTVECTOR}
		bin/hadoop jar  $UNICORN_HOME/orange-unicorn-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.orange.tfidf.VectorTFIDFDriver \
		-input ${OUTPUTIDF} -output ${OUTPUTVECTOR}
		;;
	*)
		echo "NO OPTION"
		;;
		
esac

done



