package sk.stuba.fei.dp.maly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class CwaRetrieverWebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CwaRetrieverWebApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CwaRetrieverWebApplication.class);
	}
}
