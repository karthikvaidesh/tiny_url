#!/usr/bin/env python
import threading, logging, time
import multiprocessing
from kafka import KafkaProducer

class Producer(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.stop_event = threading.Event()
        
    def stop(self):
        self.stop_event.set()

    def run(self):
        producer = KafkaProducer(bootstrap_servers='localhost:9092')

        #while not self.stop_event.is_set():
        producer.send('test', b"AAAAAADD")
        producer.send('test', b"AAAAAAEE")
        time.sleep(1)

        producer.close()

def main():
    #Producer().start()

    #Producer().stop()
    producer = KafkaProducer(bootstrap_servers='localhost:9092')

    while 1:
        #while not self.stop_event.is_set():
        producer.send('test', b"AAAAAADD")
        producer.send('test', b"AAAAAAEE")
        time.sleep(1)

    producer.close()
  
if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
        )
    main()
