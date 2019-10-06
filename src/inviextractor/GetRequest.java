package inviextractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrique
 */
public class GetRequest {

    private URL url;
    private HttpURLConnection con;
    private String USER_AGENT = "Google Chrome Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
    private int responseCode;

    public GetRequest(String URL) {

        setUpConnection(URL);

    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public HttpURLConnection getCon() {
        return con;
    }

    public String getHeader(String header) {
        return (con.getHeaderField(header));
    }

    public void setCon(HttpURLConnection con) {
        this.con = con;
    }

    public String getHTML() {
        try {
            BufferedReader in = null;
            StringBuffer response = null;
            Charset charset = Charset.forName("UTF8");

            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(GetRequest.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(GetRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            return (response.toString());
        } catch (Exception ex) {
        }

        return "";
    }

    public int getResponseCode() {
        return responseCode;
    }

    private void setUpConnection(String URL) {

        try {
            url = new URL(URL);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            this.responseCode = con.getResponseCode();

        } catch (MalformedURLException ex) {
            Logger.getLogger(GetRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
