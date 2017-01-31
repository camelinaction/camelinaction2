Chapter 6 - quartz
----------------

This directory holds examples of the quartz component.

### 6.7.2 - Using the Quartz component

The first example uses trigger.repeatInterval and trigger.repeatCount options to fire the timer once. To run this example, execute the following command:

    mvn test -Dtest=QuartzTest

The next example uses a cron style expression to set up the scheduler:

    mvn test -Dtest=QuartzCronTest
