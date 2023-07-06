package king.remoting.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import king.serialize.Serializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author King
 * @Description: 自定义解码器
 */
@AllArgsConstructor
@Slf4j
public class NettyKryoDecoder extends ByteToMessageDecoder {

    private final Serializer serializer;
    private final Class<?> genericClass;

    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= BODY_LENGTH) {
            in.markReaderIndex();
            int dataLength = in.readInt();
            if (dataLength < 0 || in.readableBytes() < 0){
                log.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            if (in.readableBytes() < dataLength){
                in.resetReaderIndex();
                return;
            }
            byte[] body = new byte[dataLength];
            in.readBytes(body);
            Object obj = serializer.deserialize(body, genericClass);
            out.add(obj);
            log.info("successful decode BuyteBuf to Object");
        }
    }
}
