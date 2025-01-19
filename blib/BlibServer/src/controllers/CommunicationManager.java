package controllers;


import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CommunicationManager {

	/**
	 *	Function to send email using mailersend api
	 * @param to Recipient email
	 * @param subject Email subject
	 * @param body Email body
	 * @param senderName The name to be shown as the email from
	 */
	public static void sendMail(String to, String subject, String body, String senderName) {
		final String token = "mlsn.d4074bb922cfb8f612862b99462eb3753b364d57bf01dae6c232d34e034f83da";
		try {

			URL url = new URL("https://api.mailersend.com/v1/email");
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
			http.addRequestProperty("Authorization", "Bearer " + token);
			http.addRequestProperty("User-Agent", "Application");
			http.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(http.getOutputStream());
			OutputStreamWriter writer = new OutputStreamWriter(wr);
			writer.write("{" +
					"\"from\":{\"email\":\"Blib@trial-0r83ql39nzmlzw1j.mlsender.net\",\"name\":\"" + senderName + "\"}," +
					"\"to\":[{\"email\":\"" + to + "\"}]," +
					"\"subject\":\"" + subject + "\"," +
					"\"text\":\"" + body + "\"" +
					"}");
			writer.flush();
			writer.close();
			http.connect();

			//get output data and print if error
			if(http.getResponseCode() != 202) {
				String result;
				BufferedInputStream bis = 100 <= http.getResponseCode() && http.getResponseCode() <= 399?new BufferedInputStream(http.getInputStream()):
						new BufferedInputStream(http.getErrorStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result2 = bis.read();
				while (result2 != -1) {
					buf.write((byte) result2);
					result2 = bis.read();
				}
				result = buf.toString();
				System.out.println(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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