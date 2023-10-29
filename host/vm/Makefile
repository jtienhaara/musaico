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
	@cd qemu \
	    && make install

.PHONY: build
build: check_settings
	@cd qemu \
	    && make build

.PHONY: clean
clean: check_settings
	@cd qemu \
	    && make clean

.PHONY: test
test: check_settings
	@cd qemu \
	    && make test

.PHONY: uninstall
uninstall: check_settings
	@cd qemu \
	    && make uninstall
