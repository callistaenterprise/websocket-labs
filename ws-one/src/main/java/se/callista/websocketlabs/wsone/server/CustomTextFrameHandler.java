package se.callista.websocketlabs.wsone.server;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.callista.websocketlabs.wsone.amq.Publisher;
import se.callista.websocketlabs.wsone.rpi.api.RPi;
import se.callista.websocketlabs.wsone.rpi.api.RPiFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class CustomTextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static final Logger LOG = LoggerFactory.getLogger(CustomTextFrameHandler.class);

	private Publisher publisher = null;
	RPi rpi = null;
	
	public CustomTextFrameHandler(Publisher publisher) {
		this.publisher = publisher;
		rpi = RPiFactory.getRPi();
	}

	@Override
    public void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String request = frame.text();
        InetSocketAddress adr = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientHost = adr.getHostString();
        LOG.debug("WS-One got: {} from: {}", request, clientHost);
        String response = requestHandler(clientHost, request);
		ctx.channel().write(new TextWebSocketFrame(response));
    }
	
	public String requestHandler(String clientHost, String request) {
		String response = null;
		try {
			String req = request.trim().toLowerCase();
	
			String currentLedStatus = getLedStatus();
	
			switch (req) {
			case "status":
				response = "LED is " + currentLedStatus;
				break;
	
			case "on":
			case "off":
				rpi.setLedOn(req.equals("on"));
				response = "LED is now " + getLedStatus() + " (was " + currentLedStatus + ")";
				break;
	
			default:
				response = "ERROR. Unknown command: \"" + request + "\", Usage: \"status|on|off\""; 
				break;
			}
		} catch (Exception ex) {
			response = "ERROR. Failed to process command: \"" + request + "\". See log for error: " + ex.getMessage(); 
			ex.printStackTrace();
		}
		
        publisher.publish("[" + clientHost + "] - " + response);

        return response;
	}

	private String getLedStatus() {
		return (rpi.isLedOn()) ? "on" : "off";
	}
}