package king.remoting.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import king.remoting.dto.RpcRequest;
import king.remoting.dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author King
 * @Description:
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final AtomicInteger atomicInteger = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            RpcRequest rpcRequest = (RpcRequest) msg;
            logger.info("server receive msg:[{}], times:[{}]", rpcRequest, atomicInteger.getAndIncrement());
            RpcResponse messageFromServer = RpcResponse.builder().message("message from server").build();
            ChannelFuture future = ctx.writeAndFlush(messageFromServer);
            future.addListener(ChannelFutureListener.CLOSE);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }
}
