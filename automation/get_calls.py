import requests
import ast
import random

with open("tiny.txt", "r") as f:
    for line in f:
	line = line[:-1]
        print(line)
	random_calls = random.randint(1, 21)
	for i in range(random_calls):
	    r = requests.get(url=line)
	    print(r)
            print(r.status_code, r.reason)
