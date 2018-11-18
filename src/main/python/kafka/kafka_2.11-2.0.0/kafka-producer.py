from flask import Flask, request, Response, jsonify

app = Flask(__name__)

@app.route('/kafkaprod/', methods=['POST'])
def publish_json_to_es():
    if request.method == 'POST':
        req = request.form
        print(req)

    return jsonify({'result': 'Failure'})
