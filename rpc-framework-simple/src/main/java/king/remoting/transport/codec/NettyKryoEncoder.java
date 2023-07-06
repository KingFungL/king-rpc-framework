package king.remoting.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import king.serialize.Serializer;
import lombok.AllArgsConstructor;

/**
 * @author King
 * @Description: 自定义编码器
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer;
    private final Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)){
            //1.对象转成byte
            byte[] body = serializer.serialize(o);
            //2.读取消息长度
            int dataLength = body.length;
            //3.写入信息对应的字节数组长度，writerIndex + 4
            byteBuf.writeInt(dataLength);
            //4.将字节数组写入ByteBuf对象中
            byteBuf.writeBytes(body);
        }
    }
}
