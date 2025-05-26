package studioyoga.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Studio Yoga.
 * Inicia la aplicación Spring Boot.
 */
@SpringBootApplication
public class ProjectApplication {

    /**
     * Método principal que lanza la aplicación Spring Boot.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}