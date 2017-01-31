Chapter 3 - transform
----------------

This directory holds message transformation examples.

### 3.2.1 - Using the Message Translator EIP 

Translate a message using a Camel Processor:

    mvn test -Dtest=OrderToCsvProcessorTest
    mvn test -Dtest=SpringOrderToCsvProcessorTest

Or by using a bean (a.k.a. POJO):
    
    mvn test -Dtest=OrderToCsvBeanTest
    mvn test -Dtest=SpringOrderToCsvBeanTest
        
Or by using the transform method from the Java DSL:
    
    mvn test -Dtest=TransformTest
    
Or by using the <transform> element in Spring XML:
    
    mvn test -Dtest= SpringTransformMethodTest
    mvn test -Dtest= SpringTransformScriptTest
    
    
