#!/bin/bash

cd backend/demo-2
mvn package
java -jar target/SensorService-0.0.1-SNAPSHOT.jar &
cd ../..
npm install --save-dev @angular-devkit/build-angular
npm start

