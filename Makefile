define env_setup =
	sudo apt-get update && sudo apt-get upgrade -y
	sudo apt-get install default-jdk -y
	sudo add-apt-repository ppa:linuxuprising/java -y
	sudo apt-get update
	sudo apt-get install oracle-java10-installer -y
	sudo apt install oracle-java10-set-default -y
	export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:/bin/java::")
	sudo apt-get install maven -y
	mvn package -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6
	sudo cp -pf ./player.sh /usr/bin/player
	sudo cp -pf ./watcher.sh /usr/bin/watcher
endef

make: ;$(value env_setup)

.ONE_SHELL:
