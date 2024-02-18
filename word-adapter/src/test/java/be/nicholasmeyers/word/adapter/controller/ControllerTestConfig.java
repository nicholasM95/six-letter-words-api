package be.nicholasmeyers.word.adapter.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "be.nicholasmeyers.word.adapter.controller",
        "be.nicholasmeyers.word.adapter.advisor"
})
public class ControllerTestConfig {
}
