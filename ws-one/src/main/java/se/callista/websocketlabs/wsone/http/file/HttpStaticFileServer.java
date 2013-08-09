package se.callista.websocketlabs.wsone.http.file;

import static se.callista.websocketlabs.wsone.server.Constants.DEFAULT_HTTP_STATIC_PORT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpStaticFileServer {

	private static final Logger LOG = LoggerFactory.getLogger(HttpStaticFileServer.class);

	private final int http_static_port;

    public static void runHttpStaticFileServer() throws Exception {
        new HttpStaticFileServer(DEFAULT_HTTP_STATIC_PORT).run();
    }

	public HttpStaticFileServer(int http_static_port) {
        this.http_static_port = http_static_port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new HttpStaticFileServerInitializer());
            LOG.info("WS-One: HTTP Static File server started at port: " + http_static_port);
            LOG.info("WS-One: - Developent URL: http://localhost:" + http_static_port + "/target/classes/web/index.html");
            b.bind(http_static_port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            LOG.info("WS-One: HTTP Static File server shutdown");
        }
    }

//    public static void main(String[] args) throws Exception {
//        int http_static_port;
//        if (args.length > 0) {
//            http_static_port = Integer.parseInt(args[0]);
//        } else {
//            http_static_port = DEFAULT_HTTP_STATIC_PORT;
//        }
//        new HttpStaticFileServer(http_static_port).run();
//    }
}
