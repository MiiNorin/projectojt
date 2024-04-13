package demo.service;

import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import org.apache.poi.ss.usermodel.*;
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
            Sheet sheet = workbook.getSheet("questions");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'questions' not found in Excel file");
            }
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Questions questions = new Questions();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {
                        switch (cellIndex) {
                            case 0:
                                questions.setQuestionId((int) cell.getNumericCellValue());
                                break;
                            case 1:
                                questions.setQuestionContext(cell.getStringCellValue());
                                break;
                            case 2:
                                questions.setOptionA(cell.getStringCellValue());
                                break;
                            case 3:
                                questions.setOptionB(cell.getStringCellValue());
                                break;
                            case 4:
                                questions.setOptionC(cell.getStringCellValue());
                                break;
                            case 5:
                                questions.setOptionD(cell.getStringCellValue());
                                break;
                            case 6:
                                questions.setStatus(cell.getStringCellValue());
                                break;
                            case 7:
                                questions.setAnswerId((int) cell.getNumericCellValue());
                                break;
                            case 8:
                                questions.setSolution(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        // In case of any exception, print the error message along with the row and column index
                        throw new IllegalArgumentException("Error processing row " + rowIndex + ", column " + cellIndex + ": " + e.getMessage());
                    }
                    cellIndex++;
                }
                questions.setCreateDate(currentDate);
                if ((questions.getQuestionId() != 0) &&
                        (!questions.getQuestionContext().isEmpty()) &&
                        (!questions.getOptionA().isEmpty() || !questions.getOptionB().isEmpty() || !questions.getOptionC().isEmpty() || !questions.getOptionD().isEmpty()) &&
                        (!questions.getStatus().isEmpty()) &&
                        (questions.getAnswerId() != 0) &&
                        (!questions.getSolution().isEmpty())) {
                    questionsList.add(questions);
                } else {
                    throw new IllegalArgumentException("Missing or invalid data in row " + rowIndex);
                }
                rowIndex++;
            }
        } catch (IOException exception) {
            // Print the full stack trace in case of an IO exception
            exception.printStackTrace();
        }
        return questionsList;
    }
}
