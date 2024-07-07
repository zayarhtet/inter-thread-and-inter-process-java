#!/bin/bash
# run_main.sh - Script to run the ThreadMain entry, inter-thread communication

if [ ! -f pom.xml ]; then
  echo "Please run this script from the root of the Maven project directory."
  exit 1
fi

mvn clean install

mvn exec:java -Dexec.mainClass="com.zayar.messaging.ThreadMain"

echo "ThreadMain entry executed successfully!"

