package com.example.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;

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
        // Dummy work
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            //ignore
        }
        span.finish();

        return "Hello from Product Service!";
    }

}
