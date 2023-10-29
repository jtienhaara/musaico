.PHONY: all
all: install build test

.PHONY: create_settings
create_settings:
	./create_settings.sh ./runtime/root/settings.env

.PHONY: settings
settings:
	@if test ! -f runtime/root/settings.env; \
	then \
	    make create_settings; \
	else \
	    echo "Musaico settings:" \
	    && cat runtime/root/settings.env \
	    | sed 's|^\(.*\)$$|    \1|'; \
	fi

.PHONY: install
install: settings
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make install
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make install
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make install

.PHONY: build
build: settings
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make build
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make build
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make build

.PHONY: clean
clean: settings
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make clean
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make clean
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make clean

.PHONY: test
test: settings
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make test
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make test
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make test

.PHONY: uninstall
uninstall: settings
	@. ./runtime/root/settings.env \
	    && cd language \
	    && make uninstall
	@. ./runtime/root/settings.env \
	    && cd host \
	    && make uninstall
	@. ./runtime/root/settings.env \
	    && cd runtime \
	    && make uninstall
