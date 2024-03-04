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

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String solution;

    private MultipartFile image;

    private String status;

}
