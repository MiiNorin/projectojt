package demo.service;


import demo.entity.Subjects;
import demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectsService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subjects> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public void deleteSubjectById(Long id) {
        Optional<Subjects> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()) {
            Subjects subject = optionalSubject.get();
            // Xóa hình ảnh nếu có
            if (subject.getImage() != null) {
                String imagePath = "public/images/subjects/" + subject.getImage();
                try {
                    Path path = Paths.get(imagePath);
                    Files.deleteIfExists(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            subjectRepository.deleteById(id);
        }
    }

    public void updateSubject(Subjects subject) {
        subjectRepository.save(subject);
    }
}