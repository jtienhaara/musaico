#!/usr/bin/make

#
# Copyright (c) 2010 Johann Tienhaara <jtienhaara@yahoo.com>
# All rights reserved.
#
# This file is part of Musaico.
#
# Musaico is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Musaico is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Musaico.  If not, see <http://www.gnu.org/licenses/>.
#

PACKAGE = musaico/i18n/exceptions


PACKAGE_DEPENDENCIES = \
	musaico/i18n/localizers \
	musaico/i18n/message


COMPILABLES = \
	L10n.class \
	I18nException.class \
	I18nRuntimeException.class \
	I18nIOException.class \
	I18nIllegalArgumentException.class \
	I18nIllegalStateException.class \
	I18nNullPointerException.class \
	\
	TimeoutException.class \
	\
	TestExceptions.class

RESOURCES = \
	Messages.properties

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk

test_locale_en:
	cp -f *.properties $(BUILD_DIR)/compile/$(PACKAGE)
	cd $(BUILD_DIR)/compile; java -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.i18n.exceptions.TestExceptions \
		en; cd -

test_locale_fr:
	cp -f *.properties $(BUILD_DIR)/compile/$(PACKAGE)
	cd $(BUILD_DIR)/compile; java -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.i18n.exceptions.TestExceptions \
		fr; cd -

test_locale_tmp:
	mkdir -p /tmp/foo/bar
	cp -f TestExceptionsFromTmp.java /tmp/foo/bar
	cp -f Messages_fr.properties /tmp/foo/bar/Messages_en.properties
	javac \
		-d /tmp \
		-classpath "$(CLASSPATH_COMPILE):$(CLASSPATH_RUN_DEPENDENCIES)" \
		TestExceptionsFromTmp.java
	@echo "Should be in French:"
	cd $(BUILD_DIR)/compile; java -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES):/tmp" \
		foo.bar.TestExceptionsFromTmp \
		en; cd -
	rm -rf /tmp/foo
