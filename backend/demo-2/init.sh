#!/bin/bash
mvn package
java -jar target/SensorService-0.0.1-SNAPSHOT.jar & 
cd sensorService
ng s

