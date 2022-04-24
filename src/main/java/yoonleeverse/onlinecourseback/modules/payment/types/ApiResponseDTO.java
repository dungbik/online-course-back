package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponseDTO<T> {

    private Integer code;
    private String message;
    private T response;
}
