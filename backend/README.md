This is a backend service for a cpsc410 project.

Prerequisites
1. IntelliJ IDEA
2. Apache Tomcat Web Server version 9

Setting up instruction
1. Download apache Tomcat from here "https://tomcat.apache.org/download-90.cgi"
2. Extract and save it in somewhere you are comfortable with. 
3. Follow this instruction: "https://medium.com/@madhawacperera/how-to-run-debug-your-web-application-with-intellij-idea-and-tomcat-ea30c3d15ba0"
4. Extra information:
    1. When you configure the application server, choose the Tomcat folder you downloaded
    2. in case you do not have Tomcat in "edit configuration" follow this instruction: "https://www.mkyong.com/intellij/intellij-idea-run-debug-web-application-on-tomcat/" (follow only the first instruction "1. Tomcat Plugin" and go back to 3)
    
Other Information
1. the base_url is "localhost:8080/backend/rest/"

Rest API
We have only one rest api in this application.
1. POST -  /getDependency
    - data has to be in this format:
       {
            "url": "github URL"
       }
    - For testing, sends POST request to "localhost:8080/backend/rest/getDependency". Add "Content-Type: application/json", and "Accept: application/json" in the header. 
