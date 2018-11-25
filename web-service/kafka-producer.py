from flask import Flask, request, Response, jsonify
from kafka import KafkaProducer
import pickle
import ast
import redis
import urllib

app = Flask(__name__)

@app.route('/generate/tiny/', methods=['POST'])
def handle_long_url():
    producer = KafkaProducer(bootstrap_servers='152.46.18.170:9092')

    if request.method == 'POST':
	json = request.get_json()
	url = json['url']
	#print(url)
        producer.send('post', pickle.dumps(url))
        
        r = redis.StrictRedis(host='152.46.18.111', port=6379)

        p = r.pubsub()
        p.subscribe(url)
        NOT_DONE = True

        while NOT_DONE:
            message = p.get_message()
	    if message:
		print("Found tiny url")
                value = message['data']
                print(value)
                if value != 1:
                    NOT_DONE = False
        p.close()
    producer.close()
    if value:
	print("Returning");
        return jsonify({'url': url, 'tiny_url': value})
    return jsonify({'result': 'Failure'})

@app.route('/tiny/<url>', methods=['GET'])
def handle_short_url(url):
    producer = KafkaProducer(bootstrap_servers='152.46.18.170:9092')

    if request.method == 'GET':
	print(request.url)
	print(url)
	producer.send('get', pickle.dumps(request.url))

    producer.close()
    return jsonify({'result': 'Success'})
