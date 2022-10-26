To use this micro services we have to make Windows to "talk" to kafka, which is installed in WSL (ubuntu)
1) For the localhost:9092 to work (micro-services config files). You have to:
   1) Disable IPv6 on WSL2 (do it once in the server):

    sudo sysctl -w net.ipv6.conf.all.disable_ipv6=1
    sudo sysctl -w net.ipv6.conf.default.disable_ipv6=1

2) If we cannot/do not want to disable IPv6, we have to do the below changes in kafka's config file
   a) get IP address of WSL (ubuntu terminal)
   
      $ ip addr | grep "eth0"

      4: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP group default qlen 1000
      inet 172.17.166.115/20 brd 172.17.175.255 scope global eth0
   
      use the 172.17.166.115 ip address (may change)

   b) add ip address to kafka's config file (save changes)
      open file: 
         vi  ~/kafka/config/server.properties
      add ip address in server.properties file:
         listeners=PLAINTEXT://172.17.166.115:9092
   c) use the specified ip address for that broker in the properties of the micro-services (all config files)
         properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.166.115:9092"); 
   d) modify port kafka's second node's config file (save changes)
       open file:      
         vi  ~/kafka/config/server2.properties
       modify ip address in server.properties file:
         listeners=PLAINTEXT://172.17.166.115:9093
   e) in all config files we have to modify the below configurations (could be one or two in each file):

       properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.166.115:9092");  

       properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.166.115:9092");
       
BEFORE RUNNING THE MICRO-SERVICES:
1) start zookeeper (in ubuntu)
   ~/kafka/bin/zookeeper-server-start.sh ~/kafka/config/zookeeper.properties
2start kafka service (default port 9092)
   ~/kafka/bin/kafka-server-start.sh ~/kafka/config/server.properties
3start kafka service second node (port 9093)
   ~/kafka/bin/kafka-server-start.sh ~/kafka/config/server2.properties

USEFUL COMMANDS (IN WSL):
- List all topics
   ~/kafka/bin/kafka-topics.sh --list --bootstrap-server 172.17.166.115:9092 
- Open console consumer (just to see messages received)
   ~/kafka/bin/kafka-console-consumer.sh --topic user_create --bootstrap-server 172.17.166.115:9092
- Open console consumer (just to see messages received from the beginning of times)
   ~/kafka/bin/kafka-console-consumer.sh --topic user_create --bootstrap-server 172.17.166.115:9092 --from-beginning

Notes:
- In UserApplication, there is a code to create the first txn_service (srv) user, it is commented,
    but if needed to create DBs from zero, need to uncomment that piece of code
- To see the project tree (see dependencies). Go to the project (and even inside the micro-service folder)
    inside PowellShell: $ mvn dependency:tree
- need to create a dummy gmail account -- DOES NOT WORK BECAUSE OF NEW GMAIL SECURITY FOR INSECURE THIRD PARTY APPLICATIONS
    turn off security (less secure app access, under security) --DOES NOT WORK! we cannot do it anymore (sending email using gmail account)