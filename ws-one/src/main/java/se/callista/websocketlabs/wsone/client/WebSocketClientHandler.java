package se.callista.websocketlabs.wsone.client;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;

import org.apache.activemq.transport.stomp.StompFrame;
import org.apache.activemq.transport.stomp.StompWireFormat;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientHandler.class);

	private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	LOG.info("WS-One: WebSocket Client disconnected!");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            LOG.info("WS-One: WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new Exception("WS-One: Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String message = textFrame.text();
            
            StompFrame sf = getStompFrame(message);
			if (sf != null) {
				LOG.info("WS-One: WebSocket Client received STOMP message: Action = " + sf.getAction() + ", Body = " + sf.getBody());
			} else {
				LOG.info("WS-One: WebSocket Client received message: " + message);
			}
        } else if (frame instanceof PongWebSocketFrame) {
        	LOG.info("WS-One: WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
        	LOG.info("WS-One: WebSocket Client received closing");
            ch.close();
        }
    }
    
    /**
     * 
     * @param message
     * @return null if message is no STOMP message
     */
	private StompFrame getStompFrame(String message) {
		try {
			InputStream is = new ByteArrayInputStream(message.getBytes());
			DataInput packet = new DataInputStream(is);
			StompFrame sf = (StompFrame)new StompWireFormat().unmarshal(packet);
			return sf;
		} catch (Exception e) {
			// IF error then this is no STOMP message, simply return null to signal a non STOMP message
			return null;
		}
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }
}
