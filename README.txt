Per lanciare kafka da WSL2 portarsi nella directory /mnt/c/Users/andrea.panzani/ideaProjects/microservices-demo/docker-compose$
e lanciare il comando -> docker-compose -f common.yml -f kafka_cluster.yml up

Per controllare che kafka sia su e che siano stati creati i topic eseguire il comando -> docker run -it --network=host confluentinc/cp-kafkacat kafkacat -L -b localhost:29092

Se si vuole vedere i messaggi inviati su kafka eseguire il comando -> docker run -it --network=host confluentinc/cp-kafkacat kafkacat -C -b localhost:19092 -t twitter-topic



