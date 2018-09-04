package com.github.server;

import com.github.gate.coder.DelimiterDecoder;
import com.github.gate.coder.DelimiterEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelPipeline;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.NetSocketInternal;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

public class TcpGateServerVerticle extends AbstractVerticle {
	private NetServer netServer;

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		NetServerOptions options = new NetServerOptions();
		options.setPort(19091);
		options.setTcpNoDelay(true);
		options.setTcpKeepAlive(true);
		options.setReusePort(true);
		options.setSsl(false);
		netServer = vertx.createNetServer(options);
		netServer.connectHandler(this::clientConnected);
		netServer.listen(result -> {
			if (result.succeeded()) {
                startFuture.complete();
			} else {
                startFuture.fail(result.cause());
			}
		});
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		netServer.close(result -> {
			if (result.succeeded()) {
				stopFuture.complete();
			} else {
				stopFuture.fail(result.cause());
			}
		});
	}

	protected void clientConnected(NetSocket socket) {
		NetSocketInternal netSocketInternal = (NetSocketInternal) socket;
		ChannelPipeline pipeline = netSocketInternal.channelHandlerContext().pipeline();
		pipeline.addBefore("hander", "delimiterEncoder", new DelimiterEncoder("2323", "2323"));
		pipeline.addBefore("hander", "delimiterDecoder", new DelimiterDecoder("2323", "2323"));
		netSocketInternal.messageHandler(this::messageHandler);
	}

	public void messageHandler(Object message) {
		ByteBuf byteBuf = (ByteBuf) message;
		System.out.println("svr:" + ByteBufUtil.hexDump(byteBuf));
		byteBuf.skipBytes(byteBuf.readableBytes());
	}

}
