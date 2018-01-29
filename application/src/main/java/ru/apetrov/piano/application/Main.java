package ru.apetrov.piano.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.apetrov.piano.backend.config.BackendConfig;

/**
 * @author a.petrov
 */
@SpringBootApplication
@Import({
        BackendConfig.class
})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
