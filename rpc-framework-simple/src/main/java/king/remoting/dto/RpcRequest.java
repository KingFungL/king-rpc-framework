package king.remoting.dto;

import lombok.*;

/**
 * @author King
 * @Description: 传输实体类
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class RpcRequest {
    private String interfaceName;
    private String methodName;
}
