package king;

import king.remoting.transport.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author King
 * @Description:
 */
@Slf4j
public class DemoServiceImpl implements DemoService{
    public static void main(String[] args) {
        NettyServer server = new NettyServer(8889);
        server.run();
    }
}
