package demo.controller;

import jakarta.servlet.http.HttpServletResponse;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.nio.file.*;
@RequestMapping("/excel")
@Controller
public class ExcelDownloadController {
    @GetMapping("/download")
    public void download1(HttpServletResponse response) throws IOException {
        try {
            File file = ResourceUtils.getFile("classpath:file/question.xlsx");
            byte[] data = Files.readAllBytes(file.toPath());

            // Thiết lập thông tin trả về
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
            response.setContentLength(data.length);

            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            StreamUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
