//package demo.controller;
//
//
//import demo.entity.Account;
//import demo.entity.ChaptersEntity;
//import demo.entity.Questions;
//import demo.entity.SubjectsEntity;
//import demo.repository.AccountRepository;
//import demo.repository.SubjectRepository;
//import demo.service.SubjectService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@RequestMapping("/subject")
//@Controller
//public class SubjectController {
//
//    @Autowired
//    private SubjectService subjectService;
//    @Autowired
//    private SubjectRepository subjectRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @GetMapping("/listSubjects/{userId}")
//    public String getSubjects(Model model, @PathVariable("userId") int userId, @RequestParam(defaultValue = "0") int page, HttpSession session) {
//        Integer loggedInUserId = (Integer) session.getAttribute("user_id");
//        if (loggedInUserId != null && loggedInUserId.equals(userId)) {
//            List<SubjectsEntity> allSubjects = subjectRepository.findSubjectsEntitiesByAccountUserId(userId);
//
//            if (allSubjects.isEmpty()) {
//                model.addAttribute("subjects", allSubjects);
//                model.addAttribute("userId", userId);
//                model.addAttribute("currentPage", 0);
//                model.addAttribute("totalPages", 0);
//                return "showListSubject";
//            }
//            int pageSize = 5;
//            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createDate").descending());
//            Page<SubjectsEntity> subjectsPage = subjectRepository.findByAccount_UserId(userId, pageable);
//            model.addAttribute("subjects", subjectsPage.getContent());
//            model.addAttribute("userId", userId);
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", subjectsPage.getTotalPages());
//            return "showListSubject";
//        } else {
//            return "redirect:/home/homePage";
//        }
//    }
//    @GetMapping("/chooseYourCourses")
//    public String chooseSubjects(Model model) {
//        List<SubjectsEntity> subjects = subjectService.getAllSubjects();
//        model.addAttribute("subjects", subjects);
//        return "showListSubjectForStudent";
//
//    }
//
//
//    @PostMapping("/addSubject")
//    public String addSubject(@ModelAttribute SubjectsEntity newSubject,
//                             @RequestParam("imageFile") MultipartFile imageFile,
//                             @RequestParam("userId") int userId,
//                             Model model,
//                             HttpSession session) {
//        Integer loggedInUserId = (Integer) session.getAttribute("user_id");
//        if (loggedInUserId != null && userId != loggedInUserId) {
//            return "/home/homePage";
//        }
//        try {
//            Account account = accountRepository.findById(userId).orElse(null);
//
//            if (!imageFile.isEmpty()) {
//                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
//                String uploadDir = "public/subject_images/";
//                Path uploadPath = Paths.get(uploadDir);
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//                try (InputStream inputStream = imageFile.getInputStream()) {
//                    Path filePath = uploadPath.resolve(fileName);
//                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//                    newSubject.setImgLink(fileName);
//                } catch (IOException e) {
//                    throw new RuntimeException("Could not save the file: " + e.getMessage());
//                }
//            }
//            SubjectsEntity subjectsEntity = new SubjectsEntity();
//            subjectsEntity.setSubjectName(newSubject.getSubjectName());
//            subjectsEntity.setImgLink(newSubject.getImgLink());
//            LocalDateTime createDate = LocalDateTime.now();
//            subjectsEntity.setCreateDate(createDate);
//            subjectsEntity.setSlot(newSubject.getSlot());
//            newSubject.setCreateDate(LocalDateTime.now());
//            newSubject.setAccount(account);
//            model.addAttribute("userId", userId);
//            subjectRepository.save(newSubject);
//            return "addSubjectSuccess";
//        } catch (Exception ex) {
//            return "error";
//        }
//    }
//
//    @GetMapping("/deleteSubject")
//    public String deleteSubject(@RequestParam("id") int id, HttpSession session) {
//        Integer userId = (Integer) session.getAttribute("user_id");
//        subjectRepository.deleteById(id);
//        return "redirect:/subject/listSubjects/" + userId;
//    }
//
//    @GetMapping("/editSubject")
//    public String showEditSubjectForm(@RequestParam("subjectId") int subjectId, Model model, HttpSession session) {
//        Integer loggedInUserId = (Integer) session.getAttribute("user_id");
//        if(loggedInUserId==null){
//            return "redirect:/home/homePage";
//        }
//        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
//        Integer checkedId = subjectCheck.getAccount().getUserId();
//        if(loggedInUserId!=null&&loggedInUserId!=checkedId){
//            return "redirect:/home/homePage";
//        }
//        Optional<SubjectsEntity> subjectOptional = subjectService.getSubjectById(subjectId);
//        Integer userId = (Integer) session.getAttribute("user_id");
//        if (subjectOptional.isPresent()) {
//            model.addAttribute("subject", subjectOptional.get());
//            model.addAttribute("userId", userId);
//            return "editSubject";
//        } else {
//            return "redirect:/subject/listSubjects/" + userId;
//        }
//    }
//
//    @PostMapping("/editSubject")
//    public String editSubject(@RequestParam("subjectId") int subjectId,
//                              @RequestParam(value = "newImage", required = false) MultipartFile newImage,
//                              @ModelAttribute SubjectsEntity subject,
//                              @RequestParam("userId") int userId,
//                              HttpSession session) {
//        Integer loggedInUserId = (Integer) session.getAttribute("user_id");
//        if(loggedInUserId==null){
//            return "redirect:/home/homePage";
//        }
//        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
//        Integer checkedId = subjectCheck.getAccount().getUserId();
//        if(loggedInUserId!=null&&loggedInUserId!=checkedId){
//            return "redirect:/home/homePage";
//        }
//        try {
//            Optional<SubjectsEntity> optionalExistingSubject = subjectService.getSubjectById(subjectId);
//            if (!optionalExistingSubject.isPresent()) {
//                return "redirect:/subject/listSubjects";
//            }
//            SubjectsEntity existingSubject = optionalExistingSubject.get();
//            if (newImage == null || newImage.isEmpty()) {
//                String oldImagePath = existingSubject.getImgLink();
//                if (oldImagePath != null && !oldImagePath.isEmpty()) {
//                    try {
//                        Files.deleteIfExists(Paths.get("public/subject_images/" + oldImagePath));
//                    } catch (IOException e) {
//                        System.out.println("Error deleting old image: " + e.getMessage());
//                    }
//                    existingSubject.setImgLink(null);
//                }
//            } else {
//                // Nếu người dùng tải lên hình ảnh mới, xử lý và lưu hình ảnh mới
//                String fileName = StringUtils.cleanPath(newImage.getOriginalFilename());
//                String uploadDir = "public/subject_images/";
//                Path uploadPath = Paths.get(uploadDir);
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//                try (InputStream inputStream = newImage.getInputStream()) {
//                    Path filePath = uploadPath.resolve(fileName);
//                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//                    existingSubject.setImgLink(fileName);
//                } catch (IOException e) {
//                    throw new RuntimeException("Could not save the file: " + e.getMessage());
//                }
//            }
//
//            existingSubject.setSubjectName(subject.getSubjectName());
//            existingSubject.setSlot(subject.getSlot());
//            subjectService.saveSubject(existingSubject);
//
//            return "redirect:/subject/listSubjects/" + userId;
//        } catch (Exception ex) {
//            System.out.println("Error editing subject: " + ex.getMessage());
//            return "error";
//        }
//
//    }
//
//    @GetMapping("/searchByName")
//    public String searchByName(Model model,
//                               @RequestParam("searchName") String searchName,
//                               @RequestParam(defaultValue = "0") int page,
//                               @RequestParam("userId") int userId) {
//        int pageSize = 5;
//        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createDate").descending());
//        Page<SubjectsEntity> subjectPage = subjectRepository.findBySubjectNameContaining(searchName, pageable);
//        model.addAttribute("subjects", subjectPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", subjectPage.getTotalPages());
//        model.addAttribute("userId", userId);
//        model.addAttribute("searchName", searchName);
//        return "searchSubjectResultPage";
//    }
//
//
//
//
//}