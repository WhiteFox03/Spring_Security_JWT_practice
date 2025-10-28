package lacshery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;

    @NotBlank
    @Size(max = 64, message = "Имя не должна превышать 64 символа!")
    private String name;

    @NotBlank
    @Size(max = 64, message = "почта не должна превышать 64 символа!")
    private String email;

    @NotBlank
    @Size(max = 256, message = "почта не должна превышать 64 символа!")
    private String password;
}