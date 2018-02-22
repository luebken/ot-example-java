#! /usr/bin/make -f

.DEFAULT_GOAL := help

build: build-customer build-product docker-build ## Build all (mvn & docker)

build-customer: ## Build customer service
	cd customer; ./mvnw clean install

build-product:  ## Build product service
	cd product; ./mvnw clean install

run-customer: ## Run customer service
	cd customer; java -jar target/customer-0.0.1-SNAPSHOT.jar

run-product: ## Run product service
	cd product; java -jar target/product-0.0.1-SNAPSHOT.jar --server.port=8090

run-jaeger: ## Run the Jaeger collector and UI
	@echo "open http://localhost:16686"
	docker run -p6831:6831/udp -p6832:6832/udp -p5778:5778 -p16686:16686 -p14268:14268 jaegertracing/all-in-one:latest

test: ## Run example requests
	curl localhost:8080/customer

docker-build: ## Build the Docker images
	docker build --build-arg JAR_FILE=customer/target/customer-0.0.1-SNAPSHOT.jar -t luebken/customer .
	docker build --build-arg JAR_FILE=product/target/product-0.0.1-SNAPSHOT.jar -t luebken/product .

docker-run-customer: ## Run the customer service as a Docker container
	docker run --link product -p 8080:8080 luebken/customer

docker-run-product: ## Run the product service as a Docker container
	docker run --name product -p 8090:8090 luebken/product --server.port=8090

run-instana-agent: ## Run the instana agent
	sudo docker run \
  --detach \
  --name instana-agent \
  --volume /var/run/docker.sock:/var/run/docker.sock \
  --volume /dev:/dev \
  --volume /sys:/sys \
  --volume /var/log:/var/log \
  --volume /Users/mdl/workspace/luebken/ot-example-java/configuration-ot.yaml:/opt/instana/agent/etc/instana/configuration-ot.yaml \
  --privileged \
  --net=host \
  --pid=host \
  --ipc=host \
  --env="INSTANA_AGENT_KEY=IYUKKimCQ-6qVTawqCHPEw" \
  --env="INSTANA_AGENT_ENDPOINT=saas-us-west-2.instana.io" \
  --env="INSTANA_AGENT_ENDPOINT_PORT=443" \
  --env="INSTANA_ZONE=LuebkenZone" \
  instana/agent

# via http://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
help: ##Shows help message
	@echo "Available make commands:"
	@grep -E '^[0-9a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
