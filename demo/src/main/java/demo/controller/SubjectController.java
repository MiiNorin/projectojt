package demo.controller;


import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.SubjectsEntity;
import demo.repository.SubjectRepository;
import demo.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@RequestMapping("/subject")
@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping("/listSubjects")
    public String getSubjects(Model model) {
        List<SubjectsEntity> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "showListSubject";
    }

//    @PostMapping("/addSubject")
//    public String addSubject(@ModelAttribute SubjectsEntity newSubject) {
//        SubjectsEntity subjectsEntity = new SubjectsEntity();
//        subjectsEntity.setSubjectName(newSubject.getSubjectName());
//        subjectsEntity.setImgLink(newSubject.getImgLink());
//        LocalDateTime createDate = LocalDateTime.now();
//        subjectsEntity.setCreateDate(createDate);
//        subjectsEntity.setSlot(newSubject.getSlot());
//        subjectRepository.save(subjectsEntity);
//        return "addSubjectSuccess";
//    }
    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute SubjectsEntity newSubject,
                             @RequestParam("imageFile") MultipartFile imageFile) {
//        try {
//            // Lưu file ảnh vào thư mục trên máy chủ
//            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
//            String uploadDir = "public/subject images/";
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//            try (InputStream inputStream = imageFile.getInputStream()) {
//                Path filePath = uploadPath.resolve(fileName);
//                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//                newSubject.setImgLink(filePath.toString()); // Lưu đường dẫn của file vào cơ sở dữ liệu
//            } catch (IOException e) {
//                throw new RuntimeException("Could not save the file: " + e.getMessage());
//            }
//            SubjectsEntity subjectsEntity = new SubjectsEntity();
//            subjectsEntity.setSubjectName(newSubject.getSubjectName());
//            subjectsEntity.setImgLink(newSubject.getImgLink());
//            LocalDateTime createDate = LocalDateTime.now();
//            subjectsEntity.setCreateDate(createDate);
//            subjectsEntity.setSlot(newSubject.getSlot());
//            newSubject.setCreateDate(LocalDateTime.now());
//            subjectRepository.save(newSubject);
//            return "addSubjectSuccess";
//        } catch (Exception ex) {
//            return "error";
//        }
        try {
            if (!imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String uploadDir = "public/subject_images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    newSubject.setImgLink("/subject_images/" + fileName); // Lưu đường dẫn của file trong thư mục
                } catch (IOException e) {
                    throw new RuntimeException("Could not save the file: " + e.getMessage());
                }
            }
            SubjectsEntity subjectsEntity = new SubjectsEntity();
            subjectsEntity.setSubjectName(newSubject.getSubjectName());
            subjectsEntity.setImgLink(newSubject.getImgLink());
            LocalDateTime createDate = LocalDateTime.now();
            subjectsEntity.setCreateDate(createDate);
            subjectsEntity.setSlot(newSubject.getSlot());
            newSubject.setCreateDate(LocalDateTime.now());
            newSubject.setCreateDate(LocalDateTime.now());
            subjectRepository.save(newSubject);
            return "addSubjectSuccess";
        } catch (Exception ex) {
            return "error";
        }
    }
//    @GetMapping("/subject/editSubject/{id}")
//    public String editSubjectForm(@PathVariable int id, Model model) {
//        Optional<SubjectsEntity> subject = subjectService.getSubjectById(id);
//        subject.ifPresent(value -> model.addAttribute("subject", value));
//        return "editSubject";
//    }
//
//    @PostMapping("/subject/editSubject/{id}")
//    public String editSubject(@PathVariable int id, @ModelAttribute SubjectsEntity updatedSubject) {
//        updatedSubject.setSubjectId(id);
//        subjectService.updateSubject(updatedSubject);
//        return "redirect:/listSubjects";
//    }

    @GetMapping("/deleteSubject")
    public String deleteSubject(@RequestParam("id") int id) {
        subjectRepository.deleteById(id);
        return "redirect:/subject/listSubjects";
    }
    @GetMapping("/editSubject")
    public String showEditSubjectForm(@RequestParam("subjectId") int subjectId, Model model) {
        Optional<SubjectsEntity> subjectOptional = subjectService.getSubjectById(subjectId);
        if (subjectOptional.isPresent()) {
            model.addAttribute("subject", subjectOptional.get());
            return "editSubject";
        } else {
            return "redirect:/subject/listSubjects";
        }
    }
    @PostMapping("/editSubject")
    public String editSubject(@RequestParam("subjectId") int subjectId,
                              @RequestParam(value = "newImage", required = false) MultipartFile newImage,
                              @ModelAttribute SubjectsEntity subject) {
        try {
            // Kiểm tra xem môn học có tồn tại hay không
            Optional<SubjectsEntity> optionalExistingSubject = subjectService.getSubjectById(subjectId);
            if (!optionalExistingSubject.isPresent()) {
                // Nếu môn học không tồn tại, xử lý trường hợp này một cách phù hợp
                // Ví dụ: hiển thị thông báo lỗi hoặc chuyển hướng người dùng đến trang khác
                return "redirect:/subject/listSubjects";
            }

            SubjectsEntity existingSubject = optionalExistingSubject.get();

            // Xóa hình ảnh cũ nếu người dùng không tải lên hình ảnh mới
            if (newImage == null || newImage.isEmpty()) {
                String oldImagePath = existingSubject.getImgLink();
                if (oldImagePath != null && !oldImagePath.isEmpty()) {
                    try {
                        Files.deleteIfExists(Paths.get(oldImagePath));
                    } catch (IOException e) {
                        // Xử lý ngoại lệ xóa hình ảnh cũ
                        // Ví dụ: ghi log hoặc hiển thị thông báo lỗi cho người dùng
                        System.out.println("Error deleting old image: " + e.getMessage());
                    }
                    // Đặt đường dẫn hình ảnh của môn học thành null sau khi xóa
                    existingSubject.setImgLink(null);
                }
            } else {
                // Nếu người dùng tải lên hình ảnh mới, xử lý và lưu hình ảnh mới
                String fileName = StringUtils.cleanPath(newImage.getOriginalFilename());
                String uploadDir = "public/subject_images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = newImage.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    existingSubject.setImgLink("/subject_images/" + fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Could not save the file: " + e.getMessage());
                }
            }

            // Cập nhật thông tin môn học
            existingSubject.setSubjectName(subject.getSubjectName());
            existingSubject.setSlot(subject.getSlot());

            // Lưu thông tin môn học vào cơ sở dữ liệu
            subjectService.saveSubject(existingSubject);

            return "redirect:/subject/listSubjects";
        } catch (Exception ex) {
            // Xử lý lỗi nếu có
            // Ví dụ: ghi log hoặc hiển thị thông báo lỗi cho người dùng
            System.out.println("Error editing subject: " + ex.getMessage());
            return "error";
        }
    }



}