package SneakerBot.SneakerBot;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.List;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.json.JSONObject;

public class Connection {
	
	  private List<String> cookies;
	  private HttpsURLConnection conn;
	  private int traceId = 0;
	  private int spanId = 0;

	  private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko";

	  public static void main(String[] args) throws Exception {

		String url = "https://www.adidas.ca/en/continental-80-shoes/G27706.html";
		String sitePath = "https://www.adidas.ca/api/products/G27706?sitePath=en";
		String cartUrl = "https://www.adidas.ca/api/products/B37691?sitePath=en";

		Connection http = new Connection();

		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());

		// 1. Send a "GET" request, so that you can extract the form's data.
		String page = http.GetPageContent(url, 1);
		String page2 = http.GetPageContent(sitePath, 2);
		

		// 2. Construct above post's content and then send a POST request for
		// authentication
		//http.sendPost(cartUrl);
	  }

	  private void sendPost(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", "www.adidas.ca");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"*/*");
		conn.setRequestProperty("Accept",
				"*/*");
		conn.setRequestProperty("Accept-Encoding",
				"gzip, deflate");
		conn.setRequestProperty("Accept-Language", "en-CA,en;q=0.5");
		for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			System.out.println(cookie.split(";", 1)[0]);
		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "https://www.adidas.ca/en/ultraboost-uncaged-shoes/B37691.html");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("X-INSTANA-L", String.valueOf(1));
		String hex1 = createHex();
		String hex2 = createHex();
		conn.setRequestProperty("X-INSTANA-S", hex1);
		conn.setRequestProperty("X-INSTANA-T", hex2);

		conn.setDoOutput(true);
		conn.setDoInput(true);
		JSONObject prod = new JSONObject();
		prod.put("product_id","BD8026");
		prod.put("quantity", 1);
		prod.put("product_variation_sku", "BD8026_610");
		prod.put("productId", "BD8026_610");
		prod.put("size", "9");
		prod.put("displaySize", "9");
		prod.put("captchaResponse", "");
		
		System.out.println(prod.toString());
		conn.setRequestProperty("Content-Length", String.valueOf(prod.toString().length()));
		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(prod.toString());
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = 
	             new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		// System.out.println(response.toString());

	  }
	  
	  public String createHex() {
		    SecureRandom csprng = new SecureRandom();
			byte[] randomBytes = new byte[4];
			csprng.nextBytes(randomBytes);
			byte[] randomBytes1 = new byte[4];
			csprng.nextBytes(randomBytes1);
			BigInteger bigInt = new BigInteger(randomBytes);
			String hexString = bigInt.abs().toString(16);
			
			BigInteger bigInt2 = new BigInteger(randomBytes1);
			String hexString2 = bigInt2.abs().toString(16);
			String hex = hexString + hexString2;
			return hex;
	  }

	  private String GetPageContent(String url, int k) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Encoding", "");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-CA,en;q=0.5");
		conn.setRequestProperty("Host", "www.adidas.ca");
		conn.setRequestProperty("Referer", "https://www.adidas.ca/en/continental-80-shoes/G27706.html");
		if (cookies != null) {
			for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		if(k == 2) {
	    System.out.println("foreign car");
		conn.setRequestProperty("X-INSTANA-L", String.valueOf(1));
		String hex1 = createHex();
		String hex2 = createHex();
		conn.setRequestProperty("X-INSTANA-S", hex1);
		conn.setRequestProperty("X-INSTANA-T", hex2);
		}
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = 
	            new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
        
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			if(k ==2) {
			  System.out.println(inputLine);
			}
			
		}
		in.close();
		
		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));
		return response.toString();
	  }

	  public List<String> getCookies() {
		return cookies;
	  }

	  public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	  }
}
