package studioyoga.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import studioyoga.project.model.PasswordResetToken;
import studioyoga.project.model.User;
import studioyoga.project.repository.PasswordResetTokenRepository;
import studioyoga.project.repository.UserRepository;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

   public void createPasswordResetTokenForUser(User user, String token) {
    Optional<PasswordResetToken> existingTokenOpt = tokenRepository.findByUser(user);
    PasswordResetToken myToken;
    if (existingTokenOpt.isPresent()) {
        // Actualiza el token y la fecha de expiración
        myToken = existingTokenOpt.get();
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(1));
    } else {
        // Crea uno nuevo
        myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(1));
    }
    tokenRepository.save(myToken);
}


    public void sendResetEmail(User user, String token, String appUrl) {
        String url = appUrl + "/reset-password?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Restablece tu contraseña");
        email.setText("Haz clic en el siguiente enlace para restablecer tu contraseña: " + url);
        mailSender.send(email);
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        return resetToken.isPresent() && resetToken.get().getExpiryDate().isAfter(LocalDateTime.now());
    }

    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}