package com.example.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.instana.opentracing.*;
import io.opentracing.util.*;


@SpringBootApplication
public class ProductApplication {

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
		SpringApplication.run(ProductApplication.class, args);
	}
}
