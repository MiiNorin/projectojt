package WebApplication.Repositories;
import WebApplication.Entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    // Các truy vấn tùy chọn có thể được thêm vào đây nếu cần
}
