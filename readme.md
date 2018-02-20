# OT & Java

An OpenTracing example with Java.

## Init & Dependencies

* Spring bootstrap: https://start.spring.io/ include Web
* Add jaeger dependency:
```
<dependency>
  <groupId>com.uber.jaeger</groupId>
  <artifactId>jaeger-core</artifactId>
  <version>0.24.0</version>
</dependency>

```
https://mvnrepository.com/artifact/com.uber.jaeger/jaeger-core/0.24.0
https://github.com/jaegertracing/jaeger-client-java

## Setup Tracer

* init: jaeger specific: as a bean into the Springboot application (see `CustomerApplication` and `ProductApplication`):
```
@Bean
public io.opentracing.Tracer jaegerTracer() {
        Configuration config = new Configuration("myServiceName", new Configuration.                            SamplerConfiguration(ProbabilisticSampler.TYPE, 1), null);
        return config.getTracer();
}
```

* Get the tracer into the controllers via dependency injection (see `CustomerController` and `ProductController`:
```
@Autowired
private io.opentracing.Tracer tracer; 
```

## Usage create span

In this example we have two services: `Customer -> Product`:

* In CustomerController:
  * Create root span
  * Inject span context in http headers
  * Call product service

* In ProductController
  * Extract span context
  * Create a span as child of root span

## Run

See Makefile