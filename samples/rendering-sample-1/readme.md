

# Renderer sample project 1

## About

This is a basic sample of how to use the renderer.

It will draw one of two single point icons on the form and it will output kml for multipoint rendering via "System.out.println()".

## Deployment

This sample project gets packaged as a tradition jar file as well as an executable jar file and can be run in your jvm by using the command: `java -jar rendering-sample-1-executable.jar`

## Requirements

This sample jar project was developed against the following web server:

	Apache Tomcat 7

The JVM used to run this jar requires:

	Orcale Java >= 1.7

## Build Commands

This is typical maven project that can be built using the following maven command: `mvn clean install`

## Libraries

Libraries Used include:

<table>
	<tr>
		<th>Dependency Name</th>
	</tr>
	<tr>
		<td>mil.army.missioncommand.mil-sym-renderer</td>
	</tr>
</table>
