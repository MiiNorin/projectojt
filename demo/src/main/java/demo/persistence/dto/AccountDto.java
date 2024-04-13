package demo.persistence.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private int roleId;
    private String email;
    private String password;
    private String fullName;
    private String phone;;
    private String checkPass;
    private MultipartFile avatar;
}