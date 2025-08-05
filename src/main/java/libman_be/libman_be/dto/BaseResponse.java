package libman_be.libman_be.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse <T>{
    public String status;
    public String message;
    public T data;
}
