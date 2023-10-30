.PHONY: all
all: install build test

.PHONY: check_settings
check_settings:
	@if test -z "$(MUSAICO_VERSION)"; \
	then \
	    echo "Please make sure the MUSAICO_VERSION environment variable is set before invoking make rules in `pwd`" >&2; \
	    exit 1; \
	fi

.PHONY: install
install: check_settings
	@echo "Checking that Docker is installed:"
	DOCKER=`which docker` \
	    || ( echo "Installing docker.io:" \
	             && apt-get install docker.io \
	             && echo "Setting suid bit on docker:" \
	             && chmod a+s `which docker` )

.PHONY: build
build: check_settings
	@echo "Building Docker container musaico-core:$(MUSAICO_VERSION):"
	docker build . \
	    --build-arg "MUSAICO_VERSION=$(MUSAICO_VERSION)" \
	    --tag musaico-core:$(MUSAICO_VERSION)

.PHONY: clean
clean: check_settings
	@echo "Cleaning Docker container musaico-core:$(MUSAICO_VERSION):"
	docker rmi musaico-core:$(MUSAICO_VERSION) \
	    || echo "Already clean."

.PHONY: test
test: check_settings

.PHONY: uninstall
uninstall: check_settings
	DOCKER=`which docker` \
	    && ( echo "Uninstalling docker.io:" \
	             && apt-get remove docker.io )
