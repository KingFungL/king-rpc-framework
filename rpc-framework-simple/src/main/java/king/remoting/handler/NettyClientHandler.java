package king.remoting.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import king.remoting.dto.RpcResponse;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author King
 * @Description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            RpcResponse rpcResponse = (RpcResponse) msg;
            logger.info("client receive msg:[{}]", rpcResponse.toString());
            //生命一个AttributeKey对象
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            //将服务端的返回结果保存到AttributeMap上
            // AttributeMap可以看作上一个Channel的共享数据源
            //key是AttributeKey, value是Attribute
            ctx.channel().attr(key).set(rpcResponse);
            ctx.channel().close();
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
