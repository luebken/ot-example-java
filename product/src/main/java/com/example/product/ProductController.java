package com.example.product;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.log.Fields;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.opentracing.tag.Tags;

@RestController
public class ProductController {

    @Autowired
    private io.opentracing.Tracer tracer;

    @RequestMapping("/product")
    public String hello(@RequestHeader Map<String, String> headers) {
        System.out.println("entry /product");

        SpanContext parentSpan = tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headers));

        // Create a child span as an error.
        Span span = tracer.buildSpan("/product").asChildOf(parentSpan).start();
        span.setTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER);
        span.setTag(Tags.ERROR.getKey(), true);
        
        // Additionally log that error.
        Map<String, String> logs = new HashMap<>();
        logs.put(Fields.EVENT, "error");
        logs.put("message", "this is bad");
        span.log(logs);
        // Doing some dummy work
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            //ignore
        }
        span.finish();

        return "Hello from Product Service!";
    }

}
