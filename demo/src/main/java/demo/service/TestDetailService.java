package demo.service;


import demo.persistence.entity.Account;
import demo.persistence.entity.TestDetailsEntity;
import demo.repository.AccountRepository;
import demo.repository.TestDetailRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class TestDetailService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TestDetailRepository testDetailRepository;

    public int createNewTestDetail(int topicId, HttpSession session) {
        Integer loggedInUserId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findById(loggedInUserId).orElse(null);
        TestDetailsEntity testDetail = new TestDetailsEntity();
        testDetail.setTopicId(topicId);
        testDetail.setSubmitted(Boolean.FALSE);
        testDetail.setCreateDate(Date.valueOf(LocalDate.now()));
        testDetail.setAccounts(account);
        TestDetailsEntity savedTestDetail = testDetailRepository.save(testDetail);
        return savedTestDetail.getTestDetailid();

    }

    public void updateSubmittedStatus(int testDetailId, double scoreCount) {
        testDetailRepository.findById((int) testDetailId).ifPresent(testDetail -> {
            testDetail.setSubmitted(Boolean.TRUE);
            testDetail.setScore(scoreCount);
            testDetailRepository.save(testDetail);
        });
    }

    public TestDetailsEntity getTestDetailById(int testDetailId) {
        return testDetailRepository.findById((int) testDetailId).orElse(null);
    }

    public TestDetailsEntity saveTestDetail(TestDetailsEntity testDetail) {
        return testDetailRepository.saveAndFlush(testDetail);
    }

}
