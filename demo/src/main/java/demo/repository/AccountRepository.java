package demo.repository;

import demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByEmail(String email);
    Account getAccountByEmail(String email);
    @Query(value = "select * from ACCOUNT where email = :user_email", nativeQuery = true)
    Optional<Account> findByEmail(@Param("user_email") String user_email);

    boolean existsAccountByEmail(String email);

    boolean existsByPassword(String password);
}
