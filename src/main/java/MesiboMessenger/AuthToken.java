package MesiboMessenger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class AuthToken {
    private static String APPLICATION_TOKEN = "rqmyjoa1ifb4fk84v0lr3o8319bsitb3fhuyn68as9czpf9plml6ffpt7tj7e8ka";
    private static String PACKAGE_DIRECTORY = "com.example.obeo";

    public static String generateAuthToken(String userName) {
        try {
            String url = "https://api.mesibo.com/api.php";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String requestType = "op=useradd";
            String tokenVerification = "token=" + APPLICATION_TOKEN;
            String userID = "addr=" + userName;
            String packageVerifcation = "appid=" + PACKAGE_DIRECTORY;
            String urlParameters = requestType + "&" + tokenVerification + "&" + userID + "&" + packageVerifcation;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error in response");
                return null;
            }


            String inputLine;

            StringBuffer response = new StringBuffer();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject)parse.parse(response.toString());
            JSONObject snd = (JSONObject)parse.parse(jobj.get("user").toString());
            return snd.get("token").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
