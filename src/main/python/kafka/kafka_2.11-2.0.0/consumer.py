#!/usr/bin/env python
import threading, logging, time
import multiprocessing
from kafka import KafkaConsumer
import pickle
from cassandra.cluster import Cluster
import redis

r = redis.StrictRedis(host='localhost', port=6379)

def main():
    consumer = KafkaConsumer(bootstrap_servers='localhost:9092',
                             auto_offset_reset='earliest',
                             consumer_timeout_ms=1000)
    consumer.subscribe(['post'])

    cluster = Cluster(['127.0.0.1'], port=9042)
    session = cluster.connect('tinyurl')

    #while not self.stop_event.is_set():
    while 1:
	for message in consumer:
	    print("HELLO")
            print(message)
            print(message.value)
            long_url = pickle.loads(message.value)
            # generate tiny url
            hashed = 'hash'
            tiny_url = 'http://localhost:5000/tiny/' + hashed
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
