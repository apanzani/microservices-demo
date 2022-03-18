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

Qualora volessi settare la ENCRYPT_KEY come enviromental variable su windows devo andare nell'apposita configurazione e successivamente riavviare intellij. Controllare che poi
la variabile venga letta dalle Run/Debug configurations

La configurazione Run/Debug configurations di SPRING_SECURITY_USER_PASSWORD deve essere senza apici singoli
es: SPRING_SECURITY_USER_PASSWORD={cipher}5fdfaab1a3644d27689467247c5783714fe78d43e855b0ecb7391349406f70719b8319b8ab0d744c0488c000be90dc6d

Se utilizzo WSL fare l'export delle variabili d'ambiente nel sistema WSL

### CONFIG SERVER  ###
Se voglio vedere le configurazioni in cloud mi devo collegare all'indirizzo http://localhost:8888/config-client/{name} dove name è l'active profile settato
nell'application.yml del mio servizio. Es: localhost:8888/config-client/elastic_query
La password di connessione al server si trova nel file application.yml e su gitHub è salvata nella sezione token con il nome di config-server authorization

### ELASTICSEARCH  ###
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



#### DOCKER CONNECTION SUCCESSFULLY ON INTELLIJ

In Engine API URL mettere tcp://localhost:2375
Aggiugnere questo nel .bashrc file :

# Start Docker daemon automatically when logging in if not running.
RUNNING=`ps aux | grep dockerd | grep -v grep`
if [ -z "$RUNNING" ]; then
    sudo dockerd > /dev/null 2>&1 &
    disown
fi
export DOCKER_HOST=tcp://0.0.0.0:2375

Eseguire il comando "sudo visudo" ed aggiungere alla fine :
# Docker daemon specification
apanzani ALL=(ALL) NOPASSWD: /usr/bin/dockerd

Creare questo file se non esiste /etc/docker/daemon.json ed aggingere questa riga {"hosts": ["tcp://0.0.0.0:2375", "unix:///var/run/docker.sock"]}
Riavviare WSL e provare a collegarsi docker -H tcp://localhost:2375


#### ELASTIC-QUERY-SERVICE ###
Per lanciarlo una prima volta occore aver startato elastic con il comando
-> docker-compose  -f common.yml -f elastic_single_node.yml up
Dopo di che far partire da Intellij il configServer e poi l'ElasticQueryServiceApplication
le chiamate che vengono gestite sono:
GET localhost:8184/elastic-query-service/documents
GET localhost:8184/elastic-query-service/documents/1
POST localhost:8184/elastic-query-service/documents/get-documet-by-text body {
                                                                                 "text":"prova"
                                                                             }

Se voglio chiamare la versione V2 utilizzando il media type, attraverso postman devo settare
nell'header il campo Acccept con i valori che trovo all'interno di produces,
in questo caso con application/vnd.api.v1+json o application/vnd.api.v2+json


OPENAPI Specification
The OpenAPI Specification (OAS) defines a standard, language-agnostic interface to RESTful APIs which allows
both humans and computers to discover and understand the capabilities of the service without
access to source code, documentation, or through network traffic inspection. When properly defined,
a consumer can understand and interact with the remote service with a minimal amount of implementation logic.

An OpenAPI definition can then be used by documentation generation tools to display the API, code generation
tools to generate servers and clients in various programming languages, testing tools, and many other use cases.

Per vedere la descrizione delle api in formato yaml andare al link
http://localhost:8184/elastic-query-service/api-docs
http://localhost:8184/elastic-query-service/api-docs.yaml
Se richiedere username e password inserire test test1234

Al link http://localhost:8184/elastic-query-service/swagger-ui/index.html#/ ho la versione dello swagger


#### ELASTIC-QUERY-WEB-CLIENT ####
Per testare singlarmente il modulo devo attivare il container di elasticsearch in questo modo
docker-compose  -f common.yml -f elastic_single_node.yml up
poi faccio partire il ConfigServer, ElasticQueryServiceApplication e alla fine ElasticQueryWebClientApplication.
Mi porto al link localhost:8185/elastic-query-web-client/ effattuo la login con test e test1234 e dopo di che
posso fare la mia ricerca

#### KEYCLOAK ####
Dopo aver configurato keycloak utilizzare il link seguende per vedere tutte le configurazioni del realm creato
http://localhost:9001/realms/microservices-realm/.well-known/openid-configuration

Per ottenere un access token per l'utente nel microservices-realm con username app_user e password "password"
fare la seguente chiamata curl

curl -d "client_id=elastic-query-web-client" -d "client_secret=RBsJHwLX4x7f1dJ8rM2PniE53jBMM8MP" -d "username=app_user" -d "password=password" -d "grant_type=password" "http://localhost:9001/realms/microservices-realm/protocol/openid-connect/token"

