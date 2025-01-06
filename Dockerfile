FROM bellsoft/liberica-openjdk-alpine:19

#Install required utils curl & jq
RUN apk add curl jq

#create workspace in image
WORKDIR /home/selenium-docker

#add required files to image
ADD target/docker-resources /home/selenium-docker
ADD runner.sh runner.sh

#start the runner.sh
ENTRYPOINT sh runner.sh
