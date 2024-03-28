package demo.service;

import demo.persistence.entity.SubjectsEntity;
import demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<SubjectsEntity> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<SubjectsEntity> getSubjectById(int id) {
        return subjectRepository.findById(id);
    }

    public SubjectsEntity saveSubject(SubjectsEntity subject) {
        return subjectRepository.save(subject);
    }

    public void deleteSubjectById(int id) {
        subjectRepository.deleteById(id);
    }

    public SubjectsEntity updateSubject(SubjectsEntity subject) {
        // Kiểm tra xem entity có tồn tại trong database không
        Optional<SubjectsEntity> existingSubject = subjectRepository.findById(subject.getSubjectId());

        if (existingSubject.isPresent()) {
            // Nếu tồn tại, cập nhật thông tin và lưu lại
            SubjectsEntity updatedSubject = existingSubject.get();
            updatedSubject.setSubjectName(subject.getSubjectName());
            updatedSubject.setImgLink(subject.getImgLink());
            updatedSubject.setCreateDate(subject.getCreateDate());
            updatedSubject.setSlot(subject.getSlot());
            return subjectRepository.save(updatedSubject);
        } else {
            // Nếu không tồn tại, có thể xử lý tùy ý, ví dụ ném một exception hoặc trả về null
            return null;
        }
    }
}