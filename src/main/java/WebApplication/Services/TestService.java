package WebApplication.Services;


import WebApplication.Entity.QuestiontestsEntity;
import WebApplication.Repositories.TestRepository;
import org.apache.jasper.tagplugins.jstl.core.If;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public List<QuestiontestsEntity> getAllTests() {
        return testRepository.findAll();
    }

    public Optional<QuestiontestsEntity> getTestById(int id) {
        return testRepository.findById(id);
    }

    public QuestiontestsEntity saveTests(QuestiontestsEntity test) {
        return testRepository.save(test);
    }

    public void deleteTestById(int id) {
        testRepository.deleteById(id);
    }

    public QuestiontestsEntity updateTest(QuestiontestsEntity test) {
        Optional<QuestiontestsEntity> existingTest = testRepository.findById(test.getTestId());

        return null;
    }
}


//package WebApplication.Services;
//
//import WebApplication.Entity.SubjectsEntity;
//import WebApplication.Repositories.SubjectRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class SubjectService {
//
//    public SubjectsEntity saveSubject(SubjectsEntity subject) {
//        return subjectRepository.save(subject);
//    }
//
//    public void deleteSubjectById(int id) {
//        subjectRepository.deleteById(id);
//    }
//
//    public SubjectsEntity updateSubject(SubjectsEntity subject) {
//        // Kiểm tra xem entity có tồn tại trong database không
//        Optional<SubjectsEntity> existingSubject = subjectRepository.findById(subject.getSubjectId());
//
//        if (existingSubject.isPresent()) {
//            // Nếu tồn tại, cập nhật thông tin và lưu lại
//            SubjectsEntity updatedSubject = existingSubject.get();
//            updatedSubject.setSubjectName(subject.getSubjectName());
//            updatedSubject.setImgLink(subject.getImgLink());
//            updatedSubject.setCreateDate(subject.getCreateDate());
//            updatedSubject.setSlot(subject.getSlot());
//            return subjectRepository.save(updatedSubject);
//        } else {
//            // Nếu không tồn tại, có thể xử lý tùy ý, ví dụ ném một exception hoặc trả về null
//            return null;
//        }
//    }
//}
