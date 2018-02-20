package com.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.opentracing.*;
import io.opentracing.tag.Tags;
import io.opentracing.propagation.*;
import java.util.*;

@RestController
public class ProductController {

    @Autowired
    private io.opentracing.Tracer tracer;

    @RequestMapping("/product")
    public String hello(@RequestHeader Map<String, String> headers) {
        System.out.println("entry /product");

        SpanContext parentSpan = tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headers));

        Span span = tracer.buildSpan("/product").asChildOf(parentSpan).start();

        // doing some work
        span.finish();

        return "Hello from Product Service!";
    }

}
