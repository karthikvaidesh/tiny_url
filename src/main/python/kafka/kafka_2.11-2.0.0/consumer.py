#!/usr/bin/env python
import threading, logging, time
import multiprocessing
from kafka import KafkaConsumer

class Consumer(multiprocessing.Process):
    def __init__(self):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()
        
    def stop(self):
        self.stop_event.set()
        
    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='localhost:9092',
                                 auto_offset_reset='earliest',
                                 consumer_timeout_ms=1000)
        consumer.subscribe(['test'])

        #while not self.stop_event.is_set():
        for message in consumer:
            print(message)
            if self.stop_event.is_set():
                break

        consumer.close()

def main():
#   Consumer().start()

#   Consumer().stop()

#   Consumer().join()
    consumer = KafkaConsumer(bootstrap_servers='localhost:9092',
                             auto_offset_reset='earliest',
                             consumer_timeout_ms=1000)
    consumer.subscribe(['post'])

    #while not self.stop_event.is_set():
    while 1:
	for message in consumer:
            print(message)

    consumer.close()

if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
        )
    main()
