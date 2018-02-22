package com.example.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import com.instana.opentracing.*;
import io.opentracing.util.*;

/*
import com.uber.jaeger.Configuration;
import com.uber.jaeger.samplers.ProbabilisticSampler;
*/

@SpringBootApplication
public class CustomerApplication {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	public io.opentracing.Tracer tracer() {
		io.opentracing.util.ThreadLocalScopeManager scopeManager = new ThreadLocalScopeManager();

		return new InstanaTracer(scopeManager);

		/*
			Configuration config = new Configuration("customerService", new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1), null);
			return config.getTracer();
			*/
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
}
