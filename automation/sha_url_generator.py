import hashlib
import datetime
import time
import requests
import json
import random

ip = input()

for i in range(ip):
    current_time = datetime.datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]
    print(current_time)
    hash_val = hashlib.sha256(str(current_time)).hexdigest()
    print(hash_val)
    url = "https://www.google.dz/search?q="+hash_val
    data = {'url':url}
    r = requests.post("http://localhost:5000/generate/tiny/", data = json.dumps(data), headers = {'Content-Type': 'application/json'})
    tiny_url = json.loads(r.text)['tiny_url']
    random_calls = random.randint(1, 1000)
    for i in range(random_calls):
        r = requests.get(url=tiny_url)
    time.sleep(0.001)
