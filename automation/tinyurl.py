import requests
import ast

with open("op.txt", "r") as f:
    for line in f:
        line = ast.literal_eval(line)

        for i in range(len(line)):
            print(line[i])

            data = {'url':line[i]}
            print(data)
	    r = requests.post("http://localhost:5000/generate/tiny/", data = data, headers = {'Content-Type': 'application/json'})

            print(r)
	    print(r.status_code, r.reason)
