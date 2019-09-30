#!/bin/bash
cd tmp
#ls
java -cp target/obeo-1.0-SNAPSHOT.jar:lib/*  ClientServer.ServerResponse.ObeoServer > /dev/null 2> /dev/null < /dev/null &