#! /bin/bash

function cleanup() {
  CONTAINER=$1

  echo "-=-= Going to remove container: $CONTAINER (with its images and volumes) =-=-"
  imageId=$(docker inspect --format='{{.Image}}' "$CONTAINER")
  docker stop "$CONTAINER"
  docker rm -v "$CONTAINER"
  docker image rm "$imageId"
}

for c in appender compactor namenode datanode # resourcemanager nodemanager1 historyserver spark spark-worker-1
do
	cleanup "$c"
done
