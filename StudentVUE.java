import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Guide To Using StudentVUE.java
 * 
 * StudentVUE(String username, String password, String district_domain) - Constructor
 *     @param username - StudentVUE username
 *     @param password - StudentVUE password
 *     @param district_domain - The domain of the school district
 * 
 *     login() - Checks that the login credentials are valid
 * 
 * List of Methods:
 * request(String methodName) - Main Method that will be used, makes a request to callSoapService()
 *  @param methodName - The method name to call
 *  @return String - The response from the SOAP service
 * 
 * callSoapService(String requestData) - Makes the SOAP call to the PXP Web Service
 *  @param requestData - The request data to send to the SOAP service
 *  @return String - XML response from the SOAP service
 * 
 * removeXMLSequences(String data) - Removes all instances of XML sequences
 *  @param data - The data to remove XML sequences from
 *  @return String - The data without XML sequences
 * 
 * List of Method Names for request(String methodName):
 * GetPXPMessages - Getting Messages
 * StudentCalendar - Getting Calendar
 * Attendance - Getting Attendance
 * Gradebook - Getting Gradebook
 * StudentHWNotes - Getting Class Notes
 * StudentInfo - Getting Student Info
 * StudentClassList - Getting Class Schedule
 * StudentSchoolInfo - Getting School Info
 * GetReportCardInitialData - Listing Report Cards
 * GetStudentDocumentInitialData - Listing Documents
 * StudentHealthInfo - Getting Health Info
 */

public class StudentVUE {

    private String username;
    private String password;
    private String district_domain;

    public StudentVUE(String username, String password, String district_domain) {
        this.username = username;
        this.password = password;
        this.district_domain = district_domain;

        login();
    }

    public String request(String methodName) {
        String requestData = "<?xml version='1.0' encoding='utf-8'?>" +
        "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>" +
            "<soap:Body>" +
                "<ProcessWebServiceRequest xmlns='http://edupoint.com/webservices/'>" +
                    "<userID>" + this.username + "</userID>" +
                    "<password>" + this.password + "</password>" +
                    "<skipLoginLog>1</skipLoginLog>" +
                    "<parent>0</parent>" +
                    "<webServiceHandleName>PXPWebServices</webServiceHandleName>" +
                    "<methodName>" + methodName + "</methodName>" +
                    "<paramStr>&lt;Parms&gt;&lt;ChildIntID&gt;0&lt;/ChildIntID&gt;&lt;/Parms&gt;</paramStr>" +
                "</ProcessWebServiceRequest>" +
            "</soap:Body>" +
        "</soap:Envelope>";

        String response = callSoapService(requestData);
        return removeXMLSequences(response);
    }

    /* Utility Methods */
    public void login() {
        String requestData = "<?xml version='1.0' encoding='utf-8'?>" +
                "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>" +
                    "<soap:Body>" +
                        "<ProcessWebServiceRequest xmlns='http://edupoint.com/webservices/'>" +
                            "<userID>" + this.username + "</userID>" +
                            "<password>" + this.password + "</password>" +
                            "<skipLoginLog>1</skipLoginLog>" +
                            "<parent>0</parent>" +
                            "<webServiceHandleName>PXPWebServices</webServiceHandleName>" +
                            "<methodName>StudentInfo</methodName>" +
                            "<paramStr>&lt;Parms&gt;&lt;ChildIntID&gt;0&lt;/ChildIntID&gt;&lt;/Parms&gt;</paramStr>" +
                        "</ProcessWebServiceRequest>" +
                    "</soap:Body>" +
                "</soap:Envelope>";

        String response = callSoapService(requestData);
        if (response.contains("The user name or password is incorrect.") || response.contains("Invalid UserID or Password")) {
            System.out.println("Login Failed");
            System.exit(1);
        } else {
            System.out.println("Welcome to StudentVUE!");
        }
    }

    private String callSoapService(String requestData) {
        String reqUrl = this.district_domain + "/Service/PXPCommunication.asmx?WSDL";
        String response = "";
        try {
            // Create the connection where we're going to send the file.
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the initial connection
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", "http://edupoint.com/webservices/ProcessWebServiceRequest");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Send the request data
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            // Write the request data
            wr.writeBytes(requestData);
            wr.flush();
            wr.close();

            // Read the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            // Close the connection, set all objects to null
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return response;
    }

    private String removeXMLSequences(String data) {
        data = data.substring(data.indexOf("<ProcessWebServiceRequestResult>") + 32, data.indexOf("</ProcessWebServiceRequestResult>"));
        return data.replace("&amp;amp;", "").replace("&amp;lt;", "").replace("&amp;gt;", "").replace("&amp;quot;", "\"").replace("&gt;", "\n").replace("&lt;", "\n");
    }
}