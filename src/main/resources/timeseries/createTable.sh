#!/bin/sh 

export HBASE_HOME=/home/xavier/hadoop2.4/hbase-0.98.5-hadoop1

test -n "$HBASE_HOME" || {
  echo >&2 'The environment variable HBASE_HOME must be set'
  exit 1
}
test -d "$HBASE_HOME" || {
  echo >&2 "No such directory: HBASE_HOME=$HBASE_HOME"
  exit 1
}

# Table that contain all the timeseries data 
VECTOR_TABLE=${VECTOR_TABLE-'unicorndb'}
META_TABLE=${META_TABLE-'unicorndb-meta'}
BLOOMFILTER=${BLOOMFILTER-'ROW'}


hbh=$HBASE_HOME

unset HBASE_HOME

exec "$hbh/bin/hbase" shell <<EOF
	create '$VECTOR_TABLE',
	{NAME => 'v', BLOOMFILTER =>'$BLOOMFILTER'}
	
	create '$META_TABLE',
	{NAME => 'name', BLOOMFILTER => '$BLOOMFILTER'}
	
	EOF
	 