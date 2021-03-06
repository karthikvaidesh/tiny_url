#!/usr/bin/env python
import threading, logging, time
import multiprocessing
from kafka import KafkaConsumer
import pickle
from cassandra.cluster import Cluster
import redis
import hashlib

r = redis.StrictRedis(host='152.46.18.111', port=6379)

def main():
    consumer = KafkaConsumer(bootstrap_servers=['152.46.18.170:9092', '152.46.18.170:9093', '152.46.18.170:9094'],
                             auto_offset_reset='earliest',
                             consumer_timeout_ms=1000)
    consumer.subscribe(['post'])

    cluster = Cluster(['152.46.16.108'], port=9042)
    session = cluster.connect('tinyurl')

    #while not self.stop_event.is_set():
    while 1:
	for message in consumer:
            long_url = pickle.loads(message.value)
	    #print(long_url)
            # generate tiny url
            hashed = hashlib.sha1(long_url.encode()).hexdigest()
            tiny_url = 'http://152.46.16.129:5000/tiny/' + hashed
	    print(tiny_url)
	    session.execute(
		"""
		INSERT INTO urls (long_url, tiny_url, count)
		VALUES (%s, %s, %s)
		""",
                (long_url, tiny_url, 0))

            p = r.pubsub()
            r.publish(long_url, tiny_url)

    consumer.close()

if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
        )
    main()
