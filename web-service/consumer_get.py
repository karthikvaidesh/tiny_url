#!/usr/bin/env python
import threading, logging, time
import multiprocessing
from kafka import KafkaConsumer
import pickle
from cassandra.cluster import Cluster
import hashlib
import datetime

def main():
    consumer = KafkaConsumer(bootstrap_servers='152.46.18.170:9092',
                             auto_offset_reset='earliest',
                             consumer_timeout_ms=1000)
    consumer.subscribe(['get'])

    cluster = Cluster(['152.46.16.108'], port=9042)
    session = cluster.connect('tinyurl')

    while 1:
	for message in consumer:
            print(pickle.loads(message.value))
            # generate tiny url
	    query = "SELECT tiny_url,long_url FROM urls WHERE tiny_url=%s"
	    future = session.execute_async(query, [pickle.loads(message.value)])

	    rows = future.result()
	    for row in rows:
		row_id = hashlib.sha1(str(datetime.datetime.now())).hexdigest()
		session.execute(
                    """
                    INSERT INTO url_visits (id, tiny_url, long_url)
                    VALUES (%s, %s, %s)
                    """,
                    (row_id, row.tiny_url, row.long_url))
	    #print(rows)
	    #url = rows[0]
	    #print(url.long_url, url.tiny_url)

    consumer.close()

if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
        )
    main()
