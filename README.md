# StudentVUE for Java
Repository for accessing StudentVUE's SOAP API within Java Applications.  
  
The file only uses the standard library for handling calls, so no additional libraries are required.

Responses from the API are returned as Strings, so you will need to parse them yourself (Example Coming Soon).
## Logging In/Usage
Copy and paste StudentVUE.java into your project, and use the following code to log in:
```java
StudentVUE studentVUE = new StudentVUE("username", "password", "district_domain");
```
As an example, here's how you would get a student's info:
```java
String studentInfo = studentVUE.request("StudentInfo");
```
In this case, "StudentInfo" is the methodName used by the SOAP API to access the student's info.  
You can find the methodName for each request in the documentation section of StudentVUE.java, or in the documentation section below
## Doumentation
StudentVUE.java contains documentation for each method, and the parameters they take. Below is an example of the methodNames that can be called with the request(String methodName) method.  

List of Method Names for request(String methodName):  
GetPXPMessages - Getting Messages  
StudentCalendar - Getting Calendar  
Attendance - Getting Attendance  
Gradebook - Getting Gradebook  
StudentHWNotes - Getting Class Notes  
StudentInfo - Getting Student Info  
StudentClassList - Getting Class Schedule  
StudentSchoolInfo - Getting School Info  
GetReportCardInitialData - Listing Report Cards  
GetStudentDocumentInitialData - Listing Documents  
StudentHealthInfo - Getting Health Info  

A Copy of StudentVUE's SOAP API Documentation can be found at https://github.com/StudentVue/docs
## Bugs
(Exact Message copied from https://github.com/StudentVue/StudentVue.py)  
Different districts may be running incompatible versions of StudentVue. If you find such an instance or to make general improvements, feel free to raise a new issue and/or open a pull request.