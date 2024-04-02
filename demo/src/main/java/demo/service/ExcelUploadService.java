package demo.service;

import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ExcelUploadService {

    @Autowired
    private QuestionRepository questionRepository;

    public void importData(MultipartFile file) throws IOException {
        if (!file.isEmpty() && isValidExcelFile(file)) {
            try (InputStream inputStream = file.getInputStream()) {
                List<Questions> questionsList = getQuestionDataFromExcel(inputStream);
                questionRepository.saveAll(questionsList);
            }
        } else {
            throw new IllegalArgumentException("File is empty or not in the correct format");
        }
    }

    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Questions> getQuestionDataFromExcel(InputStream inputStream) throws IOException {
        List<Questions> questionsList = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheet("questions");
            int rowIndex=0;
            for(Row row: sheet){
                if(rowIndex == 0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex=0;
                Questions questions = new Questions();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch ((cellIndex)){
                        case 0 -> questions.setQuestionId((int) cell.getNumericCellValue());
                        case 1 -> questions.setQuestionContext(cell.getStringCellValue());
                        case 2 -> questions.setOptionA(cell.getStringCellValue());
                        case 3 -> questions.setOptionB(cell.getStringCellValue());
                        case 4 -> questions.setOptionC(cell.getStringCellValue());
                        case 5 -> questions.setOptionD(cell.getStringCellValue());
                        case 6 -> questions.setStatus(cell.getStringCellValue());
                        case 7 -> questions.setSolution(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                questions.setCreateDate(currentDate);
                if((questions.getQuestionId() != 0) && (questions.getQuestionContext() != "")) {
                    questionsList.add(questions);
                }
            }
            }
        catch(IOException exception){
            exception.getStackTrace();
        }
        return questionsList;
    }
}
