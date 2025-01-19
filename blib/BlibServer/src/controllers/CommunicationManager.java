package controllers;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CommunicationManager {

	public static void sendMail() {
		// TODO - implement CommunicationManager.sendMail
		throw new UnsupportedOperationException();
	}

	/**
	 * This method sends an sms using a phone as a proxy using the application GSM sms
	 * app: https://play.google.com/store/apps/details?id=com.gsmmodem&hl=en&pli=1
	 * app helper: https://github.com/sadiqodho/GSM-Helper-Tool
	 * user: root
	 * password: Aa123456
	 * the functionality works by sending a post request to the phone with basic authorization
	 * and phone, message params in the format of json in the body
	 */
	public static void sendSMS(String phone, String message) {
		String userCredentials = "root:Aa123456";
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        try {
			//send the request
            URL url = new URL("http://192.168.1.195:8090/SendSMS");
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Authorization", basicAuth);
			http.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(http.getOutputStream());
			OutputStreamWriter writer = new OutputStreamWriter(wr, StandardCharsets.UTF_8);
			writer.write("phone=" + phone + "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString()));
			writer.flush();
			writer.close();
			http.connect();

			System.out.println(http.getResponseMessage());

			//get output data and print if error
			if(http.getResponseCode() != 200) {
				String result;
				BufferedInputStream bis = new BufferedInputStream(http.getInputStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result2 = bis.read();
				while (result2 != -1) {
					buf.write((byte) result2);
					result2 = bis.read();
				}
				result = buf.toString();
				System.out.println(result);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}