SET "TMP_HOME=C:\tmp"
SET "KAFKA_HOME=C:\ml_tools\kafka_2.11-0.11.0.1"
SET "kafka_bats=%KAFKA_HOME%\bin\windows"
SET "kafka_conf=%KAFKA_HOME%\config"
SET "topic_name=milof"

RD /s /q %TMP_HOME%\zookeeper
RD /s /q %TMP_HOME%\kafka-logs

START CMD /c "title Zookeeper Server & MODE con: cols=80 lines=27 & %kafka_bats%\zookeeper-server-start %kafka_conf%\zookeeper.properties"
ECHO "Press a button to start the Kafka server"
PAUSE
START CMD /c "title Kafka Server & MODE con: cols=80 lines=27 & %kafka_bats%\kafka-server-start %kafka_conf%\server.properties"
ECHO "Press a button to start streaming data"
EXIT
%kafka_bats%\kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic %topic_name%
ECHO "Press a button to start streaming data"
PAUSE