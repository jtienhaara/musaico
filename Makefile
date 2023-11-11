.PHONY: all
all: install build test test_language

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
	    && cd host \
	    && make install

.PHONY: build
build: settings
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make build

.PHONY: clean
clean: settings
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make clean

.PHONY: test
test: settings
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make test

.PHONY: uninstall
uninstall: settings
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make uninstall


.PHONY: test_audio_app
test_audio_app: settings install build test
	@. ./runtime/root/settings.env \
	    && cd host/container/docker/guest \
	    && MUSAICO_APP_ID=audio MUSAICO_APP_DIR=$$MUSAICO_ROOT_DIR/app/audio \
	           make install build test

.PHONY: test_language
test_language: settings install build test
	@. ./runtime/root/settings.env \
	    && cd host/container/docker/guest \
	    && MUSAICO_APP_ID=musaico MUSAICO_APP_DIR=$$MUSAICO_ROOT_DIR/language \
	           make install build test
	@echo "The Musaico language parser (such as it is...) can now be executed:"
	@. ./runtime/root/settings.env \
	    && export MUSAICO_BIN=$$MUSAICO_RUNTIME/app/musaico/work/musaico \
	    && ls -l $$MUSAICO_BIN \
	    && echo "For example: $$MUSAICO_BIN /path/to/my.musaico" \
	    && echo "         or: $$MUSAICO_BIN $$PWD/language/test.musaico"
	@echo "Running $$MUSAICO_BIN $$PWD/language/test.musaico:"
	@. ./runtime/root/settings.env \
	    && export MUSAICO_BIN=$$MUSAICO_RUNTIME/app/musaico/work/musaico \
	    && $$MUSAICO_BIN $$PWD/language/test.musaico \
	    && echo "SUCCESS parsing test.musaico"
