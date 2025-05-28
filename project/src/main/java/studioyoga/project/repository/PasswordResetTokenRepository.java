package studioyoga.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import studioyoga.project.model.PasswordResetToken;
import studioyoga.project.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
    Optional<PasswordResetToken> findByUser(User user);

}
