package se.callista.websocketlabs.wstwo.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;

public class Client {
	
	private static long sleepTime = 10000L;
	private static String endpointAddress = "http://localhost:8080/ws-two/nonblocking/bytecounter";
//	private static String endpointAddress = "http://localhost:8080/ws-two/blocking/bytecounter";
	private static String keystorePath = "src/consumer.jks";
	private static String keystoreType = "JKS";
	private static String keystorePassword = "password";
	private static String truststorePath = "src/truststore.jks";
	private static String truststoreType = "JKS";
	private static String truststorePassword = "password";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
			
		if (args.length > 0) {
		      sleepTime = Long.parseLong(args[0]);
		      endpointAddress = args[1];
		      keystorePath = args[2];
		      keystoreType = args[3];
		      keystorePassword = args[4];
		      truststorePath = args[5];
		      truststoreType = args[6];
		      truststorePassword = args[7]; 
		}
				
		// System.setProperty("javax.net.debug", "ssl");
//        final HttpEntity requestEntity = createRequest();
        final String expectedResponse = createExpectedResponse();
        
		while (true) {
			new Thread(new Runnable() {public void run() {new Client().performHttpsPost(expectedResponse);}}).start();
			Thread.sleep(sleepTime);
		}
	}

	private static String createExpectedResponse() throws IOException {
		InputStream expectedResponseStream = Client.class.getClassLoader().getResourceAsStream("client/expectedResponse.txt");
        final String expectedResponse = streamToString(expectedResponseStream);
		return expectedResponse;
	}

	private static HttpEntity createRequest() throws IOException,
			UnsupportedEncodingException {
		final InputStream stream = Client.class.getClassLoader().getResourceAsStream("client/request.xml");
//        String request = streamToString(stream);
        
        ContentType ct = ContentType.create("text/xml", Consts.UTF_8);
		InputStream is = new InputStream() {

			private long cnt = 0;
			
			@Override
			public int read() throws IOException {
				int nextByte = stream.read();
				if (nextByte > -1) cnt++;
				
				if (cnt % 1000 == 0) {
					long ts = 1000;
					System.err.println("Written " + cnt + " bytes, sleep " + ts  + " ms...");
					try {Thread.sleep(ts);} catch (InterruptedException e) {}
					System.err.println("Write continues...");
				}
				return nextByte;
			}
			
		    @Override
		    public void close() throws IOException {
		    	super.close();
		    	stream.close();
		    	System.err.println("Bytes: " + cnt);
		    }
		};
		final HttpEntity requestEntity = new InputStreamEntity(is, -1, ct);
		return requestEntity;
	}

	public void performHttpsPost(String expectedContent) {
		long ts = System.currentTimeMillis();
		long id = Thread.currentThread().getId();

        try {
//        	Registry<ConnectionSocketFactory> x_socketFactoryRegistry = setupSSLSocketFactory();
//        	
//            HttpClientConnectionManager x_connManager = new BasicHttpClientConnectionManager(
//                    x_socketFactoryRegistry);

	        HttpClientContext context = HttpClientContext.create();

	        HttpClient httpclient = HttpClients.custom()
		        .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
//		        .setConnectionManager(connManager)
		        .build();
	
			RequestConfig requestConfig = RequestConfig.custom()
	    		.setConnectTimeout(60000)
	    		.setSocketTimeout(60000)
	            .build();
	
	        HttpPost httpPost = new HttpPost(endpointAddress);
	        httpPost.setConfig(requestConfig);
	        httpPost.setEntity(createRequest());
	
	        HttpResponse response = httpclient.execute(httpPost, context);
	
	        HttpEntity responseEntity = response.getEntity();
            int retCode = response.getStatusLine().getStatusCode();
            String content = getContent(responseEntity);

            if (!content.contains(expectedContent)) {
            	System.err.println("Expected response: [" + expectedContent + "]");
            	throw new RuntimeException("Unexpected response: [" + content + "]");
            }
            
            logOkRequest(ts, id, "Status = " + retCode);
        } catch (Exception e) {
			logErrorRequest(ts, id, e);
			e.printStackTrace();
		}		
	}
	
	@SuppressWarnings("unused")
	private Registry<ConnectionSocketFactory> x_setupSSLSocketFactory()
			throws Exception, NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		KeyStore keystore = x_load(keystorePath, keystorePassword.toCharArray(),
				keystoreType);
		char[] keyPassword = keystorePassword.toCharArray();
		KeyStore truststore = x_load(truststorePath,
				truststorePassword.toCharArray(), truststoreType);

		SSLContext sslContext = SSLContexts.custom()
				.loadKeyMaterial(keystore, keyPassword)
				.loadTrustMaterial(truststore).build();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register(
						"https",
						new SSLConnectionSocketFactory(
								sslContext,
								SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
				.build();
		return socketFactoryRegistry;
	}

	
	private KeyStore x_load(final String res, final char[] passwd, String type) throws Exception {
		final KeyStore keystore = KeyStore.getInstance(type);
		keystore.load(new FileInputStream(res), passwd);
		return keystore;
	}
			
	static public String streamToString(InputStream in) throws IOException {
		  StringBuilder out = new StringBuilder();
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  for(String line = br.readLine(); line != null; line = br.readLine()) 
		    out.append(line);
		  br.close();
		  return out.toString();
	}
	
	private static int okCnt = 0;
	public void logOkRequest(long ts, long id, String msg) {
		okCnt++;
        log(ts, id, "OK: " + msg);
	}

	private static int errorCnt = 0;
	public void logErrorRequest(long ts, long id, Exception e) {
		errorCnt++;
        log(ts, id, "ERROR: " + e.getMessage());
	}

	public void log(long ts, long id, String msg) {
		if (Thread.currentThread().getId() != id) {
			System.err.println("### ERROR THREAD ID INCORRECT");
		}

		int threadCnt = Thread.activeCount();
		System.out.println(new Date() + ": (" + threadCnt + "/" + okCnt + "/" + errorCnt + ") " + getTs(ts) + ": " + msg);
	}

	private static long getTs(long ts) {
		return System.currentTimeMillis() - ts;
	}

	public String getContent(HttpEntity entity) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String inputLine;
			StringBuffer content = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			return content.toString();
		
		} catch (Exception e) {
			throw new RuntimeException(e);

		} finally {
			if (in != null) try {in.close();} catch (IOException e) {}
		}
	}
}