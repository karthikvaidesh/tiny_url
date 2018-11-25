define env_setup =
	sudo apt-get update && sudo apt-get upgrade -y
	sudo apt-get install openjdk-8-jdk -y
	export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:/bin/java::")
	sudo apt-get install maven -y
	mvn package -f cassandra-spark/url-aggregator/pom.xml -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6
	echo "deb http://www.apache.org/dist/cassandra/debian 311x main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list
	curl https://www.apache.org/dist/cassandra/KEYS | sudo apt-key add -
	sudo apt-get update
	sudo apt-get install cassandra
	sudo apt-get install python-setuptools python-dev build-essential
	sudo easy_install pip
	sudo pip install --upgrade virtualenv
	virtualenv .venv
	wget http://download.redis.io/redis-stable.tar.gz
	tar xvzf redis-stable.tar.gz
	make -C redis-stable 
	sudo service cassandra start
	redis-service &
	source .venv/bin/activate
        pip install -r requirements.txt
endef

make: ;$(value env_setup)

.ONE_SHELL:
