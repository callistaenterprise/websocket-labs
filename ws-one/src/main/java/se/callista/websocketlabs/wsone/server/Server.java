package se.callista.websocketlabs.wsone.server;

import static se.callista.websocketlabs.wsone.server.Constants.*;
import static se.callista.websocketlabs.wsone.amq.ActiveMQParent.runActiveMQBroker;
import static se.callista.websocketlabs.wsone.http.file.HttpStaticFileServer.runHttpStaticFileServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.callista.websocketlabs.wsone.amq.Publisher;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class Server {

	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	private final int ws_port;
    
    public Server(int ws_port) {
        this.ws_port = ws_port;
    }

    public void run() throws Exception {

    	// Start embedded ActiveMQ broker with vm and ws-transports 
    	runActiveMQBroker();
    	
    	// Initiate a publisher
    	final Publisher publisher = new Publisher();

    	// Start Web Socket server
        runWebSocketServer(publisher);
        
    	// Start HTTP Static File server
        runHttpStaticFileServer();
    }

    public void runWebSocketServer(final Publisher publisher) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            final ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(final SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                        new HttpRequestDecoder(),
                        new HttpObjectAggregator(65536),
                        new HttpResponseEncoder(),
                        new WebSocketServerProtocolHandler(DEFAULT_WEBSOCKET_BASE_URI),
                        new CustomTextFrameHandler(publisher));
                }
            });

            @SuppressWarnings("unused")
			final Channel ch = sb.bind(ws_port).sync().channel();
            LOG.info("WS-One: Web socket server started at port " + ws_port);

//            ch.closeFuture().sync();
        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int ws_port;
        if (args.length > 0) {
            ws_port = Integer.parseInt(args[0]);
        } else {
            ws_port = DEFAULT_WEBSOCKET_PORT;
        }
        new Server(ws_port).run();
        LOG.info("WS-One: Server ready for business!");        
    }
}