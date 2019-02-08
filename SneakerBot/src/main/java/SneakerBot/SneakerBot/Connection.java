package SneakerBot.SneakerBot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.json.JSONObject;

public class Connection {
	
	  private List<String> cookies;
	  private HttpsURLConnection conn;

	  private final String USER_AGENT = "Mozilla/5.0";

	  public static void main(String[] args) throws Exception {

		String url = "https://www.adidas.ca/en/continental-80-shoes/G27706.html";
		String cartUrl = "https://www.adidas.ca/api/cart_items?sitePath=en";

		Connection http = new Connection();

		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());

		// 1. Send a "GET" request, so that you can extract the form's data.
		String page = http.GetPageContent(url);
		

		// 2. Construct above post's content and then send a POST request for
		// authentication
		http.sendPost(cartUrl);
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
		conn.setRequestProperty("Referer", "https://www.adidas.ca/en/nmd_r1-shoes/BD8026.html?pr=product_rr&slot=4");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");

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
		
		SecureRandom csprng = new SecureRandom();
		byte[] randomBytes1 = new byte[4];
		csprng.nextBytes(randomBytes1);
		byte[] randomBytes2 = new byte[4];
		csprng.nextBytes(randomBytes2);
		BigInteger r1 = new BigInteger(randomBytes1).abs();
		BigInteger r2 = new BigInteger(randomBytes2).abs();
		String result = r1.toString(16) + r2.toString(16);
		
		conn.setRequestProperty("X-INSTANA-T", result);
		conn.setRequestProperty("X-INSTANA-S", result);
		conn.setRequestProperty("X-INSTANA-L", "1");  
		System.out.println(result);
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

	  private String GetPageContent(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
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
