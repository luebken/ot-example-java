package com.example.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.samplers.ProbabilisticSampler;

@SpringBootApplication
public class ProductApplication {

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
			Configuration config = new Configuration("productService", new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1), null);
			return config.getTracer();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
}
