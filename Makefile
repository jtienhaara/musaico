.PHONY: all
all: install build test

.PHONY: install
install:
	@. ./settings.env \
	    && cd language \
	    && make install
	@. ./settings.env \
	    && cd host \
	    && make install
	@. ./settings.env \
	    && cd runtime \
	    && make install

.PHONY: build
build:
	@. ./settings.env \
	    && cd language \
	    && make build
	@. ./settings.env \
	    && cd host \
	    && make build
	@. ./settings.env \
	    && cd runtime \
	    && make build

.PHONY: clean
clean:
	@. ./settings.env \
	    && cd language \
	    && make clean
	@. ./settings.env \
	    && cd host \
	    && make clean
	@. ./settings.env \
	    && cd runtime \
	    && make clean

.PHONY: test
test:
	@. ./settings.env \
	    && cd language \
	    && make test
	@. ./settings.env \
	    && cd host \
	    && make test
	@. ./settings.env \
	    && cd runtime \
	    && make test

.PHONY: uninstall
uninstall:
	@. ./settings.env \
	    && cd language \
	    && make uninstall
	@. ./settings.env \
	    && cd host \
	    && make uninstall
	@. ./settings.env \
	    && cd runtime \
	    && make uninstall
