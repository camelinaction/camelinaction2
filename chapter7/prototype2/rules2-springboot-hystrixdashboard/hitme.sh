#!/bin/sh

while :; do 
  sleep 0.100;
  echo ""
  curl http://localhost:8181/api/rules/999;
  echo ""
done  