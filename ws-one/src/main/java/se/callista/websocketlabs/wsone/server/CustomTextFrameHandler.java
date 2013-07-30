package se.callista.websocketlabs.wsone.server;

import java.math.BigInteger;
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
			String[] args = request.trim().toLowerCase().split(" ");
			if (args.length == 0) return getUnknownCommandError(request);

			String req = args[0];
	
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

			case "fibonacci":
				response = calculateFibonacci(request, args);
				break;
				
			default:
				response = getUnknownCommandError(request); 
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response = getProcessingError(request, ex); 
		}
		
        publisher.publish("[" + clientHost + "] - " + response);

        return response;
	}

	private String getLedStatus() {
		return (rpi.isLedOn()) ? "on" : "off";
	}

	private String calculateFibonacci(String request, String[] args) {
		// We require two args for the fibonacci command
		if (args.length == 1) return getUnknownCommandError(request);

		try {
			int no = Integer.parseInt(args[1]);
			
			// Require that no is in the range 0..100000
			if ((no < 0) || no > 100000) return getUnknownCommandError(request); 

			long t = System.currentTimeMillis();
	    	BigInteger result = rpi.fibonacci(no);
	    	t = System.currentTimeMillis() - t;
			return "fibonacci(" + no + ") took " + t + " ms. Result: " + result;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return getProcessingError(request, ex); 
		}
		
	}

	private String getUnknownCommandError(String request) {
		return "ERROR. Unknown command: \"" + request + "\", Usage: \"status|on|off|fibonacci 0..100000\"";
	}

	private String getProcessingError(String request, Exception ex) {
		return "ERROR. Failed to process command: \"" + request + "\". See log for error: " + ex.toString();
	}
}