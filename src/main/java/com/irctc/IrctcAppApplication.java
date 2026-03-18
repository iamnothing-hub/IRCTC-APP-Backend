package com.irctc;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SpringBootApplication
@RestController
public class IrctcAppApplication {

	public static void main(String[] args) {

        SpringApplication app = new SpringApplication(IrctcAppApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setBanner(((environment, sourceClass, out) -> {
            out.println("🚉 IRCTC SYSTEM STARTING... ");
            out.println("Environment: " + environment.getProperty("spring.profile.active"));
        }));
		SpringApplication.run(IrctcAppApplication.class, args);


	}

    @GetMapping
    public String get(){
        return "<h1 style='text-align: center; '>************************ Welcome to IRCTC 🚉 *************************</h1>";
    }
}
