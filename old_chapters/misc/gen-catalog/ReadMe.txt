This project is used to generate an archetype catalog containing only 
Camel 2.5.0 archetypes for use in m2eclipse. We needed this because at 
the time of writing an error at Maven's central repository was preventing 
newer Camel archetypes from being shown in m2eclipse. 

See https://issues.sonatype.org/browse/MVNCENTRAL-9 for more details.

To generate this catalog, run the following command:

./generate

it will be copied to ../archetype-catalog.xml

