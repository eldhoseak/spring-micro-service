C:\kafka>.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties




C:\kafka>.\bin\windows\kafka-server-start.bat .\config\server.properties




C:\kafka\bin\windows>kafka-topics.bat  --create --topic notification-topic --bootstrap-server localhost:9092



==============
prometheus

C:\prometheus>prometheus.exe --config.file prometheus.yml

--  Grafana at http://localhost:3000/d/ddyy0no4oe03ka/process-and-memory-details?orgId=1


===  

Start elastic, kibana, logstash in order and go to 

http://localhost:5601/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:60000),time:(from:now-5m,to:now))&_a=(columns:!(),filters:!(),index:'74f840b3-5784-4b33-9138-0c301ace92ac',interval:auto,query:(language:kuery,query:'Fetching%20customer'),sort:!(!('@timestamp',desc)),viewMode:documents)


======== zipkin

C:\zipkin > java -jar zipkin-server-3.4.1-exec.jar

then go to http://localhost:9411/zipkin


Test Data

1=====  Signup

{
"firstName": "Eldhose",
"lastName": "Abraham",
"email": "eldhose@mailinator.com",
"password": "password123"
}


2==== Login

{
  "username": "eldhose@mailinator.com",
  "password": "password123"
}


3 === Reservation

    {
      "customerId": 15,
	  "roomId": 6 ,
      "startDate": "2024-10-02",
      "endDate": "2024-10-05"
    }

