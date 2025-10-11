#!/bin/bash
mvn clean package
sudo docker compose build
sudo docker compose up -d
