package com.example.customer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.opentracing.Span;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapInjectAdapter;


@RestController
public class CustomerController {

    @Autowired
    private io.opentracing.Tracer tracer;

    static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/customer")
    public String hello() {

        LOG.info("entry /customer");

        // Dummy result
        String result = "Hello from Customer Service!";
        
        // Create root span
        Span span = tracer.buildSpan("/customer").startActive(false).span();

        // Inject span context
        // TODO FIXME
        Map<String, String> parameters = new HashMap<>();
        tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS, new TextMapInjectAdapter(parameters));
        LOG.info("parameters: " + parameters);
        String[] keys = parameters.keySet().toArray(new String[parameters.size()]);
        HttpHeaders headers = new HttpHeaders();
        headers.set(keys[0], parameters.get(keys[0]));

        // Call product service
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/product", HttpMethod.GET, entity, String.class);

        result += " + " + response.getBody();
        
        span.finish();
        return result;
    }

}
