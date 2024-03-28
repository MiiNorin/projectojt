package demo.service;

import demo.persistence.entity.ChaptersEntity;

import demo.repository.ChapterRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;

    public ChaptersEntity saveChapter(ChaptersEntity chapter) {
        return chapterRepository.save(chapter);
    }

}
