import requests
from bs4 import BeautifulSoup
import re

with open("sample.txt", "r") as f:
    for line in f:
        url = "https://www.google.dz/search?q="+line
        page = requests.get(url)
        soup = BeautifulSoup(page.content)
        links = soup.findAll("a")
        for link in  soup.find_all("a",href=re.compile("(?<=/url\?q=)(htt.*://.*)")):
            print re.split(":(?=http)",link["href"].replace("/url?q=",""))
