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
	@echo "Ensuring language components are installed:"
	apt-get update --yes \
	    && apt-get install --no-install-recommends --yes \
	       bison \
	    && apt-get clean

.PHONY: build
build: check_settings

.PHONY: clean
clean: check_settings

.PHONY: run
run: check_settings
	@echo "Hello, world Musaico language!"

.PHONY: test
test: check_settings
	@echo "Testing Musaico language!"
	@if test -d ./work; \
	then \
	    rm -rf ./work/*; \
	fi
	mkdir -p ./work
	bison musaico.y \
	    --output ./work/musaico_parse.c \
	    -Wcounterexamples \
	    -Werror
	cp -f \
	    ./musaico.c \
	    ./musaico.h \
	    ./work/
	gcc -c ./work/musaico_parse.c -o ./work/musaico_parse.o
	gcc -c ./work/musaico.c -o ./work/musaico.o
	gcc \
	    ./work/musaico_parse.o \
	    ./work/musaico.o \
	    -o ./work/musaico
	@echo "Trying to compile test.musaico:"
	./work/musaico test.musaico

.PHONY: uninstall
uninstall: check_settings
