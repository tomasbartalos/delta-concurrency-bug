#!/bin/bash
set -o errexit     # Use set -o errexit (a.k.a. set -e) to make your script exit when a command fails.
set -o pipefail     # Use set -o nounset (a.k.a. set -u) to exit when your script tries to use undeclared variables.
set -o nounset     # Use set -o pipefail in scripts to catch mysqldump fails in e.g. mysqldump |gzip. The exit status of the last command that threw a non-zero exit code is returned.

MASTER=${MASTER:=local}
DRIVER_MEMORY=${DRIVER_MEMORY:=1G}
EXECUTOR_MEMORY=${EXECUTOR_MEMORY:=2G}
CORES=${CORES:=5}
EXECUTOR_CORES=${EXECUTOR_CORES:=5}
CLASS=${CLASS:=class_not_set}

echo ------------------------------------------------------------
echo "Running Class: ${CLASS} on master ${MASTER}. Delta target $DELTA_TARGET"
echo "Resources: Cores: ${CORES}, driver memory: ${DRIVER_MEMORY}, executor memory: ${EXECUTOR_MEMORY}"
echo ------------------------------------------------------------

cd "$SPARK_HOME" || exit
echo "Submitting spark job ..."

"${SPARK_HOME}/bin/spark-submit" \
--driver-memory $DRIVER_MEMORY \
--executor-memory $EXECUTOR_MEMORY \
--master $MASTER \
--executor-cores=${EXECUTOR_CORES} \
--total-executor-cores=${CORES} \
--conf spark.jars.ivy=/root/.ivy \
--conf spark.driver.host="$(hostname --ip-address)" \
--class ${CLASS} \
--driver-java-options="-XX:+CrashOnOutOfMemoryError -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/logs/datalake" \
/usr/spark-app/*.jar "${DELTA_TARGET}"

echo ------------------------------------------------------------
echo COMMAND script finished.
echo ------------------------------------------------------------