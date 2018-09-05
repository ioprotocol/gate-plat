package com.github.server;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.NetSocketInternal;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;

import java.util.List;

public class TcpClient {
	protected final Logger logger = LogManager.getLogger("ZKC02 client:");

	private NetClient client;
	private Vertx vertx;
	private NetSocketInternal ctx;
	private String host;
	private int port;

	private static final List<String> MSGS = Lists.newArrayList();
	private static int index = 0;
	static {
		MSGS.add("7e7d017d0223230101010163026301");
		MSGS.add("23237e");
	}

	public static void main(String[] args) {
		VertxOptions vertxOptions = new VertxOptions().setEventLoopPoolSize(4).setWorkerPoolSize(8).setClustered(false);
		Vertx vertx = Vertx.vertx(vertxOptions);

		vertx.deployVerticle(TcpGateServerVerticle.class, new DeploymentOptions(), result -> {
		    if(result.succeeded()) {
				new TcpClient().start(vertx, "127.0.0.1", 19091);
			}
		});

	}

	public void start(Vertx vertx, String host, int port) {
		this.vertx = vertx;
		client = vertx.createNetClient(createNetOptions());
		this.host = host;
		this.port = port;
		connect();
	}

	public void connect() {
		client.connect(port, host, done -> {
			if (done.failed()) {
				logger.error(String.format("Can't connect to %s:%d", host, port), done.cause());
			} else {
				logger.info("Connection with {}:{} established successfully", host, port);

				ctx = (NetSocketInternal) done.result();

				ctx.messageHandler(this::messageHandler);
				ctx.exceptionHandler(cause -> {
					logger.error("err", cause);
				});

				/**
				 * 每隔三秒发送一条binary报文
				 */
				vertx.setPeriodic(3000, id -> {
					ctx.write(Buffer.buffer(ByteBufUtil.decodeHexDump(MSGS.get(index ++))));
					if (index >= MSGS.size()) {
						index = 0;
						vertx.cancelTimer(id);
					}
				});
			}
		});
	}

	private void messageHandler(Object msg) {
		ByteBuf byteBuf = (ByteBuf) msg;
		System.out.println("cli:" + ByteBufUtil.hexDump(byteBuf));
		byteBuf.skipBytes(byteBuf.readableBytes());
	}

	private NetClientOptions createNetOptions() {
		return new NetClientOptions().setTcpNoDelay(true).setConnectTimeout(5000).setReuseAddress(true).setReusePort(true).setTcpKeepAlive(true);
	}
}
