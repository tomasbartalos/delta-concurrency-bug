#! /bin/bash

sbt '; eval System.setProperty("spark.master","local[4]") ; runMain test.ConcurrentAppend' &
sbt '; eval System.setProperty("spark.master","local[4]") ; runMain test.ConcurrentCompactor'