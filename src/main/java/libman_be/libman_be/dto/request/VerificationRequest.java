package libman_be.libman_be.dto.request;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String password;
}
