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
	@cd container \
	    && make install
	@cd vm \
	    && make install
	@cd cluster \
	    && make install

.PHONY: build
build: check_settings
	@cd container \
	    && make build
	@cd vm \
	    && make build
	@cd cluster \
	    && make build

.PHONY: clean
clean: check_settings
	@cd container \
	    && make clean
	@cd vm \
	    && make clean
	@cd cluster \
	    && make clean

.PHONY: test
test: check_settings
	@cd container \
	    && make test
	@cd vm \
	    && make test
	@cd cluster \
	    && make test

.PHONY: uninstall
uninstall: check_settings
	@cd container \
	    && make uninstall
	@cd vm \
	    && make uninstall
	@cd cluster \
	    && make uninstall
