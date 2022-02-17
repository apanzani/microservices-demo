Per lanciare kafka da WSL2 portarsi nella directory /mnt/c/Users/andrea.panzani/ideaProjects/microservices-demo/docker-compose$
e lanciare il comando -> docker-compose -f common.yml -f kafka_cluster.yml up

Per controllare che kafka sia su e che siano stati creati i topic eseguire il comando -> docker run -it --network=host confluentinc/cp-kafkacat kafkacat -L -b localhost:29092

Se si vuole vedere i messaggi inviati su kafka eseguire il comando -> docker run -it --network=host confluentinc/cp-kafkacat kafkacat -C -b localhost:19092 -t twitter-topic

#### JCE ###
Per utilizzare la JCE ho bisogno di spring-cloud-cli installato in modo da fare la encrypt della password. Se uso powershell è direttamente utilizzabile come comando "spring", in alternativa,
usando la GIT BASH, mi devo portare sul path /c/Users/andrea.panzani/scoop/apps/springboot/2.6.3/bin e poi lanciare il comando "./spring"
Per fare le encrypt utilizzare il comando " ./spring encrypt {password_che_voglio} --key '{ENCRYPT_KEY}'" es. ./spring encrypt springCloud_Pwd! --key 'Demo_Pwd!2020'
Devo aggiungere come variabile d'ambiente la ENCRYPT_KEY assegnandoglio la mia secret, in questo caso Demo_Pwd!2020
Per vedere la encrypt e decrypt con JCE - Java Cryptography Extension - eseguire il ConfigServer
fare una chiamata da Postman POST http://localhost:8888/encrypt passando come body la chiave che vogliamo
criptare. Per decriptare usare l'url http://localhost:8888/decrypt

La configurazione Run/Debug configurations di SPRING_SECURITY_USER_PASSWORD deve essere senza apici singoli
es: SPRING_SECURITY_USER_PASSWORD={cipher}5fdfaab1a3644d27689467247c5783714fe78d43e855b0ecb7391349406f70719b8319b8ab0d744c0488c000be90dc6d


### ElasticSearch  ###
Se lanciando docker-compose ottengo exited with code 78 bootstrap check failure [1] of [1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
allora devo lanciare nel terminal ubuntu il comando "sudo sysctl -w vm.max_map_count=262144"
Lanciare il comando -> docker-compose -f common.yml -f elastic_cluster.yml up per creare il cluster elasticsearch
Verificare che sia up tramite Postman con una chimata GET all'url localhost:9200 aggiungendo in header Content-Type application/json, dovrei ricevere una cosa simile
{
    "name": "elastic-1",
    "cluster_name": "es-twitter-cluster",
    "cluster_uuid": "MTMpXgMjRUitWGOdIkFwOw",
    "version": {
        "number": "7.13.4",
        "build_flavor": "default",
        "build_type": "docker",
        "build_hash": "c5f60e894ca0c61cdbae4f5a686d9f08bcefc942",
        "build_date": "2021-07-14T18:33:36.673943207Z",
        "build_snapshot": false,
        "lucene_version": "8.8.2",
        "minimum_wire_compatibility_version": "6.8.0",
        "minimum_index_compatibility_version": "6.0.0-beta1"
    },
    "tagline": "You Know, for Search"
}




