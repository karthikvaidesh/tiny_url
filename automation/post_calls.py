import requests
import ast
import json
import urllib

with open("op.txt", "r") as f:
    for line in f:
        line = ast.literal_eval(line)

        for i in range(len(line)):
            print(urllib.unquote(line[i]))

            data = {'url':urllib.unquote(line[i])}
            print(data)
	    r = requests.post("http://localhost:5000/generate/tiny/", data = json.dumps(data), headers = {'Content-Type': 'application/json'})

            print(r)
	    print(r.status_code, r.reason)
