#!/bin/bash
# check-keycloak-server-started.sh
apt-get update -y

yes | apt-get install curl

curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://keycloak-authorization-server:9001/realms/microservices-realm)

echo "result status code:" "$curlResult"

while [[ ! $curlResult == "200" ]]; do
  >&2 echo "Keycloak server is not up yet!"
  echo "result status code:" "$curlResult"
  sleep 20
  curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://keycloak-authorization-server:9001/realms/microservices-realm)
done

./cnb/process/web