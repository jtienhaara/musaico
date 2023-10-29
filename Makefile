.PHONY: all
all: install build test

.PHONY: create_settings
create_settings:
	./create_settings.sh ./runtime/root/settings.env

.PHONY: settings
settings:
	@if test ! -f runtime/root/settings.env; \
	then \
	    echo "Creating runtime/root/settings.env:"; \
	    make create_settings; \
	elif test `sha256sum ./create_settings.sh | awk '{ print $$1; }'` != `grep '^export MUSAICO_SETTINGS_SHA256=' runtime/root/settings.env | sed 's|^export MUSAICO_SETTINGS_SHA256=||'`; \
	then \
	    echo "Musaico settings have changed.  Re-exporting runtime/root/settings.env:"; \
	    make create_settings; \
	else \
	    echo "Musaico settings:" \
	    && cat runtime/root/settings.env \
	    | sed 's|^\(.*\)$$|    \1|'; \
	fi

.PHONY: install
install: settings
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make install
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make install
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make install

.PHONY: build
build: settings
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make build
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make build
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make build

.PHONY: clean
clean: settings
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make clean
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make clean
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make clean

.PHONY: test
test: settings
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make test
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make test
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make test

.PHONY: uninstall
uninstall: settings
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make uninstall
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make uninstall
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make uninstall


.PHONY: test_audio_app
test_audio_app: settings install build test
	@. ./runtime/root/settings.env \
	    && cd host/container/docker/guest \
	    && MUSAICO_APP_ID=audio MUSAICO_APP_DIR=$$MUSAICO_ROOT_DIR/app/audio \
	           make install build test
