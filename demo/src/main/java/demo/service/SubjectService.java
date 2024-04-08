package demo.service;

import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import demo.repository.QuestionRepository;
import demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    // Trong SubjectService
    public Page<SubjectsEntity> findSubjectWithPaginationSortByDate(int page, int size, int userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return subjectRepository.findByAccount_UserId(userId, pageable);
    }

    // Trong hàm findSubjectWithPaginationSortByDate của SubjectService


    public void deleteSubjectById(int id) {
        subjectRepository.deleteById(id);
    }

    public SubjectsEntity updateSubject(SubjectsEntity subject) {
        Optional<SubjectsEntity> existingSubject = subjectRepository.findById(subject.getSubjectId());

        if (existingSubject.isPresent()) {
            SubjectsEntity updatedSubject = existingSubject.get();
            updatedSubject.setSubjectName(subject.getSubjectName());
            updatedSubject.setImgLink(subject.getImgLink());
            updatedSubject.setCreateDate(subject.getCreateDate());
            updatedSubject.setSlot(subject.getSlot());
            return subjectRepository.save(updatedSubject);
        } else {
            return null;
        }
    }

    public SubjectsEntity addChapterToSubject(int subjectId, String chapterName) {
        Optional<SubjectsEntity> existingSubject = subjectRepository.findById(subjectId);

        if (existingSubject.isPresent()) {
            SubjectsEntity subject = existingSubject.get();
            // Thêm logic để thêm chapter vào subject ở đây
            return subjectRepository.save(subject);
        } else {
            return null;
        }
    }

    @Autowired
    QuestionRepository questionRepository;

    public List<Questions> findRandQuestionByChapterId(Integer chapterId, Integer totalQ){
        return questionRepository.findRandomQuestionsByChapterId(chapterId, totalQ);
    }

}
