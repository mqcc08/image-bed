package mc.image.bed;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("mc.image")
@EnableScheduling
public class ImageBedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageBedApplication.class , args);
    }
}
