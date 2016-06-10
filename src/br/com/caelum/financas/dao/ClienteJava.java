package br.com.caelum.financas.dao;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

public class ClienteJava {
	private static int HTTP_COD_SUCESSO = 200;

	public static void main(String[] args) throws JAXBException {

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000); // Timeout
																				// Limit
		HttpResponse response;

		try {
			HttpPost post = new HttpPost("http://localhost:11030/upbc-checkout/v1/checkout/resource/order/generateUrl");
			StringEntity xml = new StringEntity(
					  "{\"codOrder\":\"1\",\"expirationDateUrl\":\"2016-06-09T09:30:54.483Z\"} "
					  );
			xml.setContentType("application/json");
			post.setEntity(xml);
			response = client.execute(post);
			
			/* Checking response */
			if (response != null) {
				InputStream in = response.getEntity().getContent(); // Get the
																	// data in
																	// the
																	// entity
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { HttpClient httpClient = new DefaultHttpClient(); HttpPost
		 * httpPost = new HttpPost(
		 * "http://localhost:11030/upbc-checkout/v1/checkout/resource/order/generateUrl"
		 * ); StringEntity xml = new StringEntity(
		 * "details={\"codOrder\":\"1293\",\"expirationDateUrl\":\"2016-06-09T09:30:54.483Z\"} "
		 * );
		 * 
		 * httpPost.addHeader("Content-Type","application/json");
		 * httpPost.addHeader("Accept","application/json");
		 * httpPost.setEntity(xml); HttpResponse response =
		 * httpClient.execute(httpPost);
		 * System.out.println(response.getStatusLine()); } catch
		 * (MalformedURLException e) { e.printStackTrace(); } catch (IOException
		 * e) { e.printStackTrace(); }
		 */
	}
}
