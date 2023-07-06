package king.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import king.remoting.dto.RpcRequest;
import king.remoting.dto.RpcResponse;
import king.remoting.handler.NettyClientHandler;
import king.remoting.transport.RpcRequestTransport;
import king.remoting.transport.codec.NettyKryoDecoder;
import king.remoting.transport.codec.NettyKryoEncoder;
import king.serialize.kyro.KryoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author King
 * @Description:
 */
public class NettyClient implements RpcRequestTransport {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final String host;
    private final int port;
    private static final Bootstrap bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch){
                        /**
                         * 自定义序列化解码器
                         */
                        //RpcResponse -> ByteBuf
                        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        // ByteBuf -> RpcRequest
                        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        return null;

    }

    /**
     * 发送信息到服务端
     * @param rpcRequest
     * @return 服务端返回的数据
     */
    @Override
    public RpcResponse sendMessage(RpcRequest rpcRequest) {
        try {
            ChannelFuture f = bootstrap.connect(host, port).sync();
            logger.info("client connect {}", host + ":" + port);
            Channel futureChannel = f.channel();
            logger.info("send message");
            if (futureChannel != null){
                futureChannel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()){
                        logger.info("client send message :[{}]", rpcRequest.toString());
                    }else {
                        logger.error("Send failed:", future.cause());
                    }
                });
                //
                //阻塞等待，知道Channel关闭
                futureChannel.closeFuture().sync();
                //将服务器返回到数据也就是RpcResponse对象取出
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                return futureChannel.attr(key).get();
            }
        } catch (InterruptedException e) {
            logger.error("occur exception when connect server:", e);
        }
        return null;
    }
}
