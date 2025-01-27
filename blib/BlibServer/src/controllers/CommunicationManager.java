package controllers;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Handles communication mostly. Currently only Email and SMS.
 */
public class CommunicationManager {
	public static String emailServer = "https://api.mailersend.com/v1/email";
	public static String smsServer = "http://192.168.1.195:8090/SendSMS";

	/**
	 * Function to send email using mailersend api
	 * @param to Recipient email
	 * @param subject Email subject
	 * @param body Email body
	 * @param senderName The name to be shown as the email from
	 */
	public static void sendMail(String to, String subject, String body, String senderName) {
		System.out.println("Sending mail to " + to);
		final String token = "mlsn.a08cad3d63636fd35834cdbcae0a2346075f07c3432e942fecfefa1c168db331";
		try {

			URL url = new URL(emailServer);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
			http.addRequestProperty("Authorization", "Bearer " + token);
			http.addRequestProperty("User-Agent", "Application");
			http.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(http.getOutputStream());
			OutputStreamWriter writer = new OutputStreamWriter(wr, StandardCharsets.UTF_8);
			writer.write("{" +
					"\"from\":{\"email\":\"blib@luffyd.xyz\",\"name\":\"" + senderName.replace("\"", "'") + "\"}," +
					"\"to\":[{\"email\":\"" + to.replace("\"", "'") + "\"}]," +
					"\"subject\":\"" + subject.replace("\"", "'") + "\"," +
					"\"html\":\"" + body.replace("\"", "'") + "\"" +
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
	 * This method sends an SMS using a phone as a proxy using the application GSM sms
	 * app: https://play.google.com/store/apps/details?id=com.gsmmodem&hl=en&pli=1
	 * app helper: https://github.com/sadiqodho/GSM-Helper-Tool
	 * user: root
	 * password: Aa123456
	 * the functionality works by sending a post request to the phone with basic authorization
	 * and phone, message params in the format of json in the body
	 * @param phone Recipient phone number
	 * @param message Message to send throw sms
	 */
	public static void sendSMS(String phone, String message) {
		String userCredentials = "root:Aa123456";
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        try {
			//send the request
            URL url = new URL(smsServer);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}