package king.remoting.dto;

import lombok.*;

/**
 * @author King
 * @Description:
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class RpcResponse {
    String message;
}
