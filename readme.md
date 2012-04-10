#Welcome to Socializer

Socializer is a demo for Seam Social using prime face.It's still very rough but all pull request are welcome to make it better
Out of the box it'll run on JBoss AS 7.11 (tested) and any Java EE 6 server (not tested). As it relies on CDI it could also be enhanced to run on tomcat and Jetty thru [weld-servlet](http://docs.jboss.org/weld/reference/latest/en-US/html/environments.html#d0e5221 "Weld outside Java EE") CDI engine (idea of improvement ?)

##Functionalities

Right now it supports Twitter and Facebook timeline / update and LinkedIn update. Seam Social provides pagination for Twitter / Facebook timeline, but it's not used yet in socializer.

##Running Socializer

You'll need to install :

 * Java 6
 * [JBoss AS 7.11](http://download.jboss.org/jbossas/7.1/jboss-as-7.1.1.Final/jboss-as-7.1.1.Final.tar.gz "Download JBoss")
 * [Maven 3](http://www.apache.org/dyn/closer.cgi/maven/binaries/apache-maven-3.0.4-bin.tar.gz "Download Maven")
 
These being installed, you have to launch JBoss  with
 
`$JBOSS_HOME/bin/standalone.sh`
 
and 
 
`mvn package jboss-as:deploy`
 
in Socializer root directory. You can then use the application on `http://localhost:8080/socializer`
 
Enjoy and contribute :-)
 
