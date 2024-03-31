package demo.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;



@Getter
@Setter
public class QuestionDto {

    private String questionContext;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String solution;

    private MultipartFile image;

    private String status;

}
