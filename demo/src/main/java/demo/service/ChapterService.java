package demo.service;

import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.SubjectsEntity;
import demo.repository.ChapterRepository;

import demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;

    private SubjectRepository subjectRepository;


}
