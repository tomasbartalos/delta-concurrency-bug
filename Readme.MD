This app tries to reproduce possible concurrency bug in delta.io.
When 2 processes are concurrently modifying delta table, they create duplicates.
The processes are:
* appender - appends new data to table
* compactor - reorganizing table files without changing the content

**The bug is reproducible on HDFS as well as on local file system**

# Howto to reproduce on local file system:
**Prerequisites:** java 1.8+, sbt

Run:
```
./run-local.sh
```

This will:
* run appender against local filesystem (`/tmp/concurrent_delta`)
* run compactor against local filesystem (`/tmp/concurrent_delta`)

**Howto check test results:**

Search for "Test Result: " string in logs

# Howto to reproduce on HDFS:
**Prerequisites:** sbt, docker

Run:
```
./run-docker.sh
```
This will:
* start spark cluster (1 master, 1 worker)
* start HDFS (1 namenode, 1 datanode, resourcemanager and nodemanager)
* start compactor against hdfs cluster (`hdfs://namenode:9000/datastore/concurrent_delta`)
* start appender against hdfs cluster (`hdfs://namenode:9000/datastore/concurrent_delta`)

**Howto check test results:**
```
docker logs -f compactor
```
Search for "Test Result: " string

# Cleanup for test restart
Run:
```
./cleanup.sh
```
This will:
* remove compiled scala binary files (`./target`)
* remove locally created data files (`/tmp/concurrent_delta`, `/tmp/delta-concurrency-bug`)
* remove created docker resources (container, images, volumes)
