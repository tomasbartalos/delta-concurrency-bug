package test

import org.apache.spark.internal.Logging

class BaseAppRunner extends App with Logging {
  if(args.length > 0) {
    target = args(0)
  }
}
