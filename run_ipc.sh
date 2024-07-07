#!/bin/bash
# run_ipc.sh - Script to run the receiver or initiator of the inter-process communication

if [ ! -f pom.xml ]; then
  echo "Please run this script from the root of the Maven project directory."
  exit 1
fi

mvn clean install

run_receiver() {
  mvn exec:java -Dexec.mainClass="com.zayar.messaging.ReceiverPlayer"
}

run_initiator() {
  mvn exec:java -Dexec.mainClass="com.zayar.messaging.InitiatorPlayer"
}

if [ "$1" == "receiver" ]; then
  run_receiver
elif [ "$1" == "initiator" ]; then
  run_initiator
else
  echo "Usage: $0 {receiver|initiator}"
  exit 1
fi

echo "$1 executed successfully!"
