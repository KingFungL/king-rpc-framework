package king.loadbalance;

import king.extension.SPI;
import king.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @author King
 * @Description: Interface to the load balancing policy
 */
@SPI
public interface LoadBalance {

    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
