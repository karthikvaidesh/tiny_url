from flask import Flask, request, Response, jsonify
from kafka import KafkaProducer
import pickle

app = Flask(__name__)

@app.route('/kafkaprod/', methods=['POST'])
def publish_json_to_es():
    producer = KafkaProducer(bootstrap_servers='localhost:9092')

    if request.method == 'POST':
        req = request.form['param1']
	print(type(req))
        producer.send('post', pickle.dumps(req))
        print(req)

    producer.close()
    return jsonify({'result': 'Success'})
