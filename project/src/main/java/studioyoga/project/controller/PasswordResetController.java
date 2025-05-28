
package studioyoga.project.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import studioyoga.project.model.User;
import studioyoga.project.repository.UserRepository;
import studioyoga.project.service.PasswordResetService;

@Controller
public class PasswordResetController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "user/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            String token = UUID.randomUUID().toString();
            passwordResetService.createPasswordResetTokenForUser(userOpt.get(), token);
            String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            passwordResetService.sendResetEmail(userOpt.get(), token, appUrl);
        }
        redirectAttributes.addFlashAttribute("success",
                "Si el correo está registrado, recibirás instrucciones para restablecer tu contraseña.");
        return "redirect:/forgot-password";

    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        if (!passwordResetService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "El enlace es inválido o ha expirado.");
            return "user/resetPassword";
        }
        model.addAttribute("token", token);
        return "user/resetPassword";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            redirectAttributes.addAttribute("token", token);
            return "redirect:/reset-password?token=" + token;

        }
        passwordResetService.updatePassword(token, newPassword);
        redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente.");
        return "redirect:/login";
    }
}
