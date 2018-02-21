package com.example.customer;

import java.util.HashMap;
import java.util.Iterator;
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
import io.opentracing.tag.Tags;
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

        // Dummy work
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            //ignore
        }
        String result = "Hello from Customer Service!";

        // Create root span
        Span span = tracer.buildSpan("/customer").withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                .withTag(Tags.COMPONENT.getKey(), "springboot app").startActive(false).span();

        // Inject span context
        Map<String, String> parameters = new HashMap<>();
        tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS, new TextMapInjectAdapter(parameters));
        HttpHeaders headers = new HttpHeaders();
        Iterator<String> iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            headers.set(key, parameters.get(key));
        }
        LOG.info("headers: " + headers);

        // Call product service
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/product", HttpMethod.GET, entity,
                String.class);

        result += " + " + response.getBody();

        span.finish();
        return result;
    }

}
