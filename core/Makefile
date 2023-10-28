.PHONY: all
all: build test

.PHONY: build
build:
	docker build . \
	    --tag musaico-core:0.0.1

.PHONY: test
test: build
	docker run \
	    -i --tty \
	    --rm \
	    musaico-core:0.0.1 \
	    bash
