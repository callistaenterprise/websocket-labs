package se.callista.websocketlabs.wsone.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

	private static final Logger LOG = LoggerFactory.getLogger(Client.class);

	private final URI uri;

    public Client(URI uri) {
        this.uri = uri;
    }

    public void run(String... messages) throws Exception {
    	
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            String protocol = uri.getScheme();
            if (!"ws".equals(protocol)) {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }

            HttpHeaders customHeaders = new DefaultHttpHeaders();
            customHeaders.add("MyHeader", "MyValue");

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            final WebSocketClientHandler handler =
                    new WebSocketClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, false, customHeaders));

            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline pipeline = ch.pipeline();
                     pipeline.addLast("http-codec", new HttpClientCodec());
                     pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
                     pipeline.addLast("ws-handler", handler);
                 }
             });

            LOG.info("WS-One: WebSocket Client connecting");
            Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();
            
            // Ping
            LOG.info("WS-One: WebSocket Client sending ping");
            ch.write(new PingWebSocketFrame(Unpooled.copiedBuffer(new byte[]{1, 2, 3, 4, 5, 6})));

            // Send messages
            if (messages != null) {
            	LOG.info("WS-One: Sending " + messages.length + " WebSocket messages");
	        	for (String msg : messages) {
	                ch.write(new TextWebSocketFrame(msg));
	    		}
            }
            
//            // Close
//            LOG.info("WS-One: WebSocket Client sending close");
//            ch.write(new CloseWebSocketFrame());
//
//            // WebSocketClientHandler will close the connection when the server
//            // responds to the CloseWebSocketFrame.
//            LOG.info("WS-One: WebSocket Client waits for server to close the connection...");
//            ch.closeFuture().sync();
        } finally {
//            group.shutdownGracefully();
        }
    }

    @SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

    	/* FIXME: Stomp subscriber is based on ActiveMQ v5.6 implementation, doesn't work in v5.9-SNAPSHOT...
    	LOG.info("WS-One: Prepare STOMP connect message");
        String stompConnectMsg = 
        	"CONNECT\n" + 
        	"login:\n" +
        	"passcode:\n" +
        	"\n" +
        	"\u0000\n";
        
        LOG.info("WS-One: Prepare STOMP subscribe message");
        String stompSubscribeMsg = 
        	"SUBSCRIBE\n" + 
        	"destination:/topic/" + DEFAULT_AMQ_NOTIFY_TOPIC + "\n" +
        	"id:sub-0\n" +
        	"\n" +
        	"\u0000\n";
        new Client(new URI("ws://localhost:61614/stomp")).run(stompConnectMsg, stompSubscribeMsg);
    	*/

    	/* FIXME: Add MQTT subscriber based on Eclipse Paho, http://www.eclipse.org/paho/ */
    	
    	URI uri;
        if (args.length > 0) {
            uri = new URI(args[0]);
        } else {
            uri = new URI("ws://localhost:8080/websocket");
        }

		// Send some messages and wait for responses
        LOG.info("WS-One: Prepare WebSocket messages");
		String[] msgs = new String[] {"status", "on", "on", "off", "status"};
        new Client(uri).run(msgs);
        
        LOG.info("WS-One: WebSocket Client waits for user to end program by pressing RETURN...");
        new Scanner(System.in).nextLine();
        LOG.info("WS-One: WebSocket Client ends the program...");
    }
}