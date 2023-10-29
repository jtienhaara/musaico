VERSION=0.0.1

.PHONY: all
all: build test

.PHONY: build
build:
	docker build . \
	    --build-arg VERSION=$(VERSION) \
	    --tag musaico-audio:$(VERSION)

.PHONY: test
test: build
	docker run \
	    -i --tty \
	    --rm \
	    musaico-audio:$(VERSION) \
	    bash
