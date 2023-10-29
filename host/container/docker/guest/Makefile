.PHONY: all
all: install build test

.PHONY: check_settings
check_settings:
	@if test -z "$(MUSAICO_VERSION)"; \
	then \
	    echo "Please make sure the MUSAICO_VERSION environment variable is set before invoking make rules in `pwd`" >&2; \
	    exit 1; \
	fi
	@if test -z "$(MUSAICO_RUNTIME)" \
	    -o ! -d "$(MUSAICO_RUNTIME)"; \
	then \
	    echo "Please make sure the MUSAICO_RUNTIME environment variable is set to an absolute path before invoking make rules in `pwd`: $(MUSAICO_RUNTIME)" >&2; \
	    exit 1; \
	fi
	@if test -z "$(MUSAICO_APP_ID)"; \
	then \
	    echo "Please make sure the MUSAICO_APP_ID environment variable is set before invoking make rules in `pwd`" >&2; \
	    exit 1; \
	fi
	@if test -z "$(MUSAICO_APP_DIR)" \
	    -o ! -d "$(MUSAICO_APP_DIR)"; \
	then \
	    echo "Please make sure the MUSAICO_APP_DIR environment variable is set to an existing directory accessible from $$PWD before invoking make rules: $(MUSAICO_APP_DIR)" >&2; \
	    exit 1; \
	fi

.PHONY: install
install: check_settings
	@echo "Checking that $(MUSAICO_APP_DIR) files are installed in $(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)/guest:"
	./is_app_installed.sh \
	    || ( mkdir -p "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)" \
	             && rm -rf "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)/guest" \
	             && cp -rf "$(MUSAICO_APP_DIR)" "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)/guest" ) \

.PHONY: build
build: check_settings
	@echo "Building Docker container $(MUSAICO_APP_ID):$(MUSAICO_VERSION):"
	@rm -rf "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)/docker"
	mkdir "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)/docker"
	cp -rf \
	    ./Dockerfile \
	    "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)/docker"
	cd "$(MUSAICO_RUNTIME)/app/$(MUSAICO_APP_ID)" \
	    && docker build . \
	        --file docker/Dockerfile \
	        --tag "$(MUSAICO_APP_ID):$(MUSAICO_VERSION)"

.PHONY: clean
clean: check_settings
	@echo "Cleaning Docker container $(MUSAICO_APP_ID)::$(MUSAICO_VERSION):"
	docker rmi "$(MUSAICO_APP_ID)::$(MUSAICO_VERSION)" \
	    || echo "Already clean."

.PHONY: test
test: check_settings
	docker run \
	    -i --tty \
	    --rm \
	    --env "MUSAICO_DOMAIN=" \
	    --env "MUSAICO_EMAIL=musaico@localhost" \
	    --env "MUSAICO_HOST=0.0.0.0" \
	    "$(MUSAICO_APP_ID):$(MUSAICO_VERSION)" \
	    make test

.PHONY: uninstall
uninstall: check_settings
# TODO Not sure what to do here for uninstall...
#      Not a good idea to remove the guest directory since it
#      might still be used for other hosts (e.g. qemu etc).
#      Probably needs redesign.  TBD later.
