import requests
import ast

with open("op.txt", "r") as f:
    for line in f:
        line = ast.literal_eval(line)
    
        for i in range(len(line)):
            print(line[i])

            data = {'link':line[i]}
            r = requests.post("http://localhost:8000", data = data)

            print(r.status_code, r.reason)
