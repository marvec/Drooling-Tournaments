#!/bin/bash
#export JAVA_OPTS="-Dorg.jboss.byteman.verbose=true -Dorg.jboss.byteman.debug=true -javaagent:$BYTEMAN_HOME/lib/byteman.jar" #=script:byteman.btm"
#java $JAVA_OPTS -jar target/tournaments-0.9-SNAPSHOT-jar-with-dependencies.jar
bmjava.sh -l byteman.btm -s byteman.jar -Dorg.jboss.byteman.verbose=true -Dorg.jboss.byteman.debug=true -jar ../target/tournaments-0.9-SNAPSHOT-jar-with-dependencies.jar