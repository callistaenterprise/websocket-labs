package se.callista.websocketlabs.wstwo.client;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SlowClient implements Runnable {

	private static long sleepTime = 25L;
	private static String endpointAddress = "http://localhost:8080/ws-two/nonblocking/bytecounter";
//	private static String endpointAddress = "http://localhost:8080/ws-two/blocking/bytecounter";
	
    public static void main(String[] args) throws Exception {
		while (true) {
			new Thread(new SlowClient()).start();
			Thread.sleep(sleepTime);
		}
    }

    @Override
	public void run() {
        try {
			slowClient(endpointAddress);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

    private static void slowClient(String urlText) throws MalformedURLException, IOException, InterruptedException {
        URL url = new URL(urlText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setChunkedStreamingMode(1);
        connection.setDoOutput(true);        
        writeSlowContent(connection);
        readReponse(connection);
    }

    private static void readReponse(URLConnection connection) throws IOException {
        InputStream in = connection.getInputStream();
        int c;
        while( (c = in.read()) != -1) {
            System.err.print((char)c);
        }
    }

    private static void writeSlowContent(URLConnection connection) throws IOException, InterruptedException {
        OutputStream out = connection.getOutputStream();
        for (int i = 0; i < 10; i++) {
        	//for (int j = 0; j < 1; j++)	
        	out.write('a');
            out.flush();
            int ts = 1000;
			System.err.println("Written " + i + " bytes, sleep " + ts  + " ms...");
			try {Thread.sleep(ts);} catch (InterruptedException e) {}
			System.err.println("Write continues...");
        }
        out.close();
    }
}