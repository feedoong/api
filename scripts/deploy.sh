#!/bin/bash

echo "> Moving to /home/ec2-user directory"
cd /home/ec2-user

echo "> Checking for running instance"
CURRENT_PID=$(pgrep -f "java -jar -Dspring.profiles.active=dev /home/ec2-user/api.jar")

echo "> Currently running instance PID: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> No currently running application to stop."
else
    echo "> Stopping the currently running application (PID: $CURRENT_PID)"
    kill -15 $CURRENT_PID
    sleep 5
    CURRENT_PID=$(pgrep -f "java -jar -Dspring.profiles.active=dev /home/ec2-user/api.jar")
    if [ -z "$CURRENT_PID" ]; then
        echo "> Application successfully stopped."
    else
        echo "> Failed to stop the application. Killing forcefully (PID: $CURRENT_PID)"
        kill -9 $CURRENT_PID
        sleep 5
    fi
fi

echo "> Deploying new JAR..."

nohup java -jar /home/ec2-user/api.jar > /home/ec2-user/api.log 2>&1 &

echo "> Application deployment completed."
