package demo.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

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

    @NotEmpty(message = "The option is required")
    private String solution;
    private MultipartFile image;

    @NotBlank(message = "You must set the status for this question")
    private String status;


}
