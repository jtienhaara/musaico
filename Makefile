.PHONY: all
all: build test_audio

.PHONY: build
build: build_core build_audio

.PHONY: build_core
build_core:
	cd core \
	    && make build; \
	    cd ..

.PHONY: build_audio
build_audio:
	cd audio \
	    && make build; \
	    cd ..

.PHONY: test_audio
test_audio:
	cd audio \
	    && make test; \
	    cd ..
