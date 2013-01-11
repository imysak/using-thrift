#!/bin/bash 

#PARENT = `pwd`
rm -rf  generated-sources
mkdir generated-sources

  SERVICE=$1
  SOURCE_DIR=generated-sources
  #/$SERVICE
  #mkdir $SOURCE_DIR
  thrift --gen "js:node" -r --out "$SOURCE_DIR"  "src/main/thrift/service.thrift"

