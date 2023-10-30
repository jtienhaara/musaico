.PHONY: all
all: install build test

.PHONY: check_settings
check_settings:

.PHONY: install
install: check_settings
	@echo "Ensuring audio components are installed:"
	apt-get update --yes \
	    && apt-get install --no-install-recommends --yes \
	       jackd2 \
	    && apt-get clean

.PHONY: build
build: check_settings

.PHONY: clean
clean: check_settings

.PHONY: run
run: check_settings
	@echo "Hello, world running audio app!"

.PHONY: test
test: check_settings
	echo "Testing audio app!"

.PHONY: uninstall
uninstall: check_settings
