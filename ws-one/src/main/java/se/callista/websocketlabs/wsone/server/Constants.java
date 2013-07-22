package se.callista.websocketlabs.wsone.server;

public interface Constants {
	public final static int    DEFAULT_WEBSOCKET_PORT = 8080;
	public final static String DEFAULT_WEBSOCKET_BASE_URI = "/websocket";

	public final static int    DEFAULT_HTTP_STATIC_PORT = 8081;

	public final static String DEFAULT_AMQ_URL = "vm://localhost?create=false&broker.persistent=false";
	public final static String DEFAULT_AMQ_WS_URL = "ws://0.0.0.0:61614";
	public final static String DEFAULT_AMQ_NOTIFY_TOPIC = "wsone.notify";
	

}
