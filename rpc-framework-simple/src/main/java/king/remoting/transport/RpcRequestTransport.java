package king.remoting.transport;

import king.remoting.dto.RpcRequest;
import king.remoting.dto.RpcResponse;

/**
 * @author King
 * @Description: send RpcRequest。
 */
public interface RpcRequestTransport {

    /**
     * send rpc request to server and get result
     *
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);

    RpcResponse sendMessage(RpcRequest rpcRequest);
}
