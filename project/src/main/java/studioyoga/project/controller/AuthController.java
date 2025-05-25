package studioyoga.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    // cookies
    @PostMapping("/set-cookie")
public String setCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("miCookie", "valorEncriptado");
    cookie.setMaxAge(7 * 24 * 60 * 60); // 1 semana
    cookie.setPath("/");
    cookie.setHttpOnly(true); // Seguridad: solo accesible vía HTTP
    cookie.setSecure(true); // Solo en HTTPS (actívalo en producción)
    response.addCookie(cookie);
    return "redirect:/";
}
@GetMapping("/read-cookie")
public String readCookie(@CookieValue(name = "miCookie", defaultValue = "") String cookieValue) {
    System.out.println("Valor de la cookie: " + cookieValue);
    return "vista";
}

}


   