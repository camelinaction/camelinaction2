Chapter 10 - undertow-rest-json-xml
-----------------------------------

This is a Rest DSL example using camel-undertow as chosen component.

### 10.2.5 Using XML and JSon data formats with Rest DSL

To try this example you need to first build it

    mvn clean install

And then start the example using

    mvn exec:java

From a web browser you can open the following url to retrieve order 1:

    http://localhost:8080/orders/1

However from the command line you can try the example using `curl` where you can wee the HTTP headers as well

To get an order in json format

    curl --header "Accept: application/json" http://localhost:8080/orders/1

And to get the same order in xml format

    curl --header "Accept: application/xml" http://localhost:8080/orders/1

We have prepared some sample data you can send that causes various errors


#### Order not found

To get an order that does not exists such as order id 99

    curl -i --header "Accept: application/json" http://localhost:8080/orders/99


#### Updating order causes problem

To update an existing order but causing a problem due invalid input

    curl -i -X PUT -d @invalid-update.json http://localhost:8080/orders --header "Content-Type: application/json"


#### Creating order causes problem

To create a new order but causing a problem due invalid input

    curl -i -X POST -d @invalid-create.json http://localhost:8080/orders --header "Content-Type: application/json"

#### Server side exception

To trigger a server side exception

    curl -i -X POST -d @kaboom-create.json http://localhost:8080/orders --header "Content-Type: application/json"



