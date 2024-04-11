package demo.service;


import demo.persistence.entity.TestDetailsEntity;
import demo.repository.TestDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class TestDetailService {

    @Autowired
    private TestDetailRepository testDetailRepository;

    public int createNewTestDetail(int topicId) {

        TestDetailsEntity testDetail = new TestDetailsEntity();

        testDetail.setTopicId(topicId);
        testDetail.setSubmitted(Boolean.FALSE);
        testDetail.setCreateDate(Date.valueOf(LocalDate.now()));

        TestDetailsEntity savedTestDetail = testDetailRepository.save(testDetail);

        return savedTestDetail.getTestDetailid();

    }

    public void updateSubmittedStatus(int testDetailId) {
        testDetailRepository.findById((int) testDetailId).ifPresent(testDetail -> {
            testDetail.setSubmitted(Boolean.TRUE);
            testDetailRepository.save(testDetail);
        });
    }

    public TestDetailsEntity getTestDetailById(int testDetailId) {
        return testDetailRepository.findById((int) testDetailId).orElse(null);
    }

    public TestDetailsEntity saveTestDetail(TestDetailsEntity testDetail) {
        return testDetailRepository.save(testDetail);
    }
}
