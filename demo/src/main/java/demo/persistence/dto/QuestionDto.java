package demo.persistence.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
public class QuestionDto {
    @NotEmpty(message = "The question is required")
    private String questionContext;

    @NotEmpty(message = "The option is required")
    private String optionA;

    @NotEmpty(message = "The option is required")
    private String optionB;

    @NotEmpty(message = "The option is required")
    private String optionC;

    @NotEmpty(message = "The option is required")
    private String optionD;

    @NotEmpty(message = "Phai co solution")
    private String solution;

    private MultipartFile image;

    @NotEmpty(message = "them status vao")
    private String status;

    private int answerId;

    private LocalDateTime createDate;
    public String getFormattedCreateDate() {
        if (createDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return createDate.format(formatter);
        } else {
            return "";
        }
    }

}
