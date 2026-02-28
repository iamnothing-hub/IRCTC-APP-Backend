package com.irctc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class IrctcAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrctcAppApplication.class, args);
	}

    @GetMapping
    public String get(){
        return "<h1 style='text-align: center; '>************************ Welcome to IRCTC *************************</h1>";
    }
}
