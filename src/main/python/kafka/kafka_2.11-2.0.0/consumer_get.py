#!/usr/bin/env python
import threading, logging, time
import multiprocessing
from kafka import KafkaConsumer
import pickle
from cassandra.cluster import Cluster

def main():
    consumer = KafkaConsumer(bootstrap_servers='localhost:9092',
                             auto_offset_reset='earliest',
                             consumer_timeout_ms=1000)
    consumer.subscribe(['get'])

    cluster = Cluster(['127.0.0.1'], port=9042)
    session = cluster.connect('tinyurl')

    while 1:
	for message in consumer:
	    print("HELLO")
            print(pickle.loads(message.value))
            # generate tiny url
	    query = "SELECT long_url FROM urls WHERE tiny_url=%s"
	    future = session.execute_async(query, [pickle.loads(message.value)])

	    rows = future.result()
	    print(list(rows))
	    url = rows[0]
	    print(url.long_url, url.tiny_url)

    consumer.close()

if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
        )
    main()
