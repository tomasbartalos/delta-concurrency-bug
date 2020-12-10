#! /bin/bash

# To run this test you need to have installed:
# * sbt - to compile scala code
# * docker - to run containers for spark, hadoop and testing apps - appender, compactor

sbt assembly
cp ./target/scala-2.12/delta-concurrency-bug-assembly-*.jar ./docker/app/resources
mkdir /tmp/delta-concurrency-bug
cd docker || exit
docker-compose up

#TO CHECK THE TEST RESULT, RUN:
# docker logs -f compactor
# Search for "Test Result: " string