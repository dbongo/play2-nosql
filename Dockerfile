FROM taig/play

MAINTAINER Vincent Dubois <dubois.vct@free.fr>

RUN apt-get update && apt-get install -y git

RUN cd /opt && git clone https://github.com/vdubois/play2-nosql.git

