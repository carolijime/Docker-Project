To use this micro services we have to make Windows to "talk" to kafka, which is intalled in WSL (ubuntu)
1) Make changes in kafka's config file
   a) get IP address of WSL (ubuntu terminal)
   
      $ ip addr | grep "eth0"

      4: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP group default qlen 1000
      inet 172.17.164.132/20 brd 172.17.175.255 scope global eth0
   
      use the 172.17.164.132 ip address (may change)

   b) add ip address to kafka's config file (save changes)
      open file: 
         vi  ~/kafka/config/server.properties
      add ip address in server.properties file:
         listeners=PLAINTEXT://172.17.164.132:9092
   c) use the specified ip address for that broker in the properties of the micro-service
         properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.164.132:9092"); 
   d) modify port kafka's second node's config file (save changes)
       open file:      
         vi  ~/kafka/config/server2.properties
       modify ip address in server.properties file:
         listeners=PLAINTEXT://172.17.164.132:9093

2) start zookeeper (in ubuntu)
   ~/kafka/bin/zookeeper-server-start.sh ~/kafka/config/zookeeper.properties
3) start kafka service (default port 9092)
   ~/kafka/bin/kafka-server-start.sh ~/kafka/config/server.properties
4) start kafka service second node (port 9093)
   ~/kafka/bin/kafka-server-start.sh ~/kafka/config/server2.properties
5) List all topics
   ~/kafka/bin/kafka-topics.sh --list --bootstrap-server 172.17.164.132:9092 
6) Open console consumer (just to see messages received)
   ~/kafka/bin/kafka-console-consumer.sh --topic user_create --bootstrap-server 172.17.164.132:9092
7) Open console consumer (just to see messages received from the beginning of times)
   ~/kafka/bin/kafka-console-consumer.sh --topic user_create --bootstrap-server 172.17.164.132:9092 --from-beginning