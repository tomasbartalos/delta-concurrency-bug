#! /bin/bash

echo "Going to delete app binaries"
rm -rf target
echo "Going to remove local datafiles"
rm -rf /tmp/delta-concurrency-bug/*
rm -rf /tmp/concurrent_delta
./docker/cleanup.sh