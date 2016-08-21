Chapter 3 - order
----------------

This directory holds examples related to converting messages that contain domain objects (in this case PurchaseOrder).

### 3.3.2 - Transforming from XML to a POJO using JAXB

This example can be run using:

    mvn test -Dtest=PurchaseOrderJaxbTest

### 3.4.2 - Using Camel’s CSV data format

This example can be run using:

    mvn test -Dtest=PurchaseOrderCsvTest
    mvn test -Dtest=PurchaseOrderCsvSpringTest

### 3.4.3 - Using Camel’s Bindy data format
 
 This example can be run using:
 
    mvn test –Dtest=PurchaseOrderBindyTest
    mvn test –Dtest=PurchaseOrderUnmarshalBindyTest
    
### 3.4.4 - Using Camel’s JSON data format
 
 This example can be run using:
 
    mvn test –Dtest=PurchaseOrderJSONTest
    
### 3.5.1 - Using Apache Velocity
 
 This example can be run using:
 
    mvn test -Dtest=PurchaseOrderVelocityTest        
