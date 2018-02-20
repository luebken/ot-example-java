#! /usr/bin/make -f


.DEFAULT_GOAL := help

build: build-customer build-product ## Build all

build-customer: ## Build customer service
	cd customer; ./mvnw clean install

build-product:  ## Build product service
	cd product; ./mvnw clean install

run-customer: ## Run customer service
	cd customer; java -jar target/customer-0.0.1-SNAPSHOT.jar

run-product: ## Run product service
	cd product; java -jar target/product-0.0.1-SNAPSHOT.jar --server.port=8090

run-jaeger:
	@echo "open http://localhost:16686"
	docker run -p6831:6831/udp -p6832:6832/udp -p5778:5778 -p16686:16686 -p14268:14268 jaegertracing/all-in-one:latest

# via http://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
help: ##Shows help message
	@echo "Available make commands:"
	@grep -E '^[0-9a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
