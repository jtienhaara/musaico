#!/usr/bin/make

#
# Copyright (c) 2009-2016 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/value/blocking


PACKAGE_DEPENDENCIES = \
	musaico/foundation/filter \
	musaico/foundation/domains \
	musaico/foundation/contract \
	musaico/foundation/value


COMPILABLES = \
	MODULE.class \
	\
	Result.class \
	\
	AsynchronousResult.class \
	MultipleBlockingValuesCollector.class \
	\
	Blocking.class \
	Cancelled.class \
	Timeout.class \
	\
	FutureBlockingValues.class \
	\
	BlockingMustComplete.class \
	CancellableMustBeCancelled.class \
	CancellableMustNotBeCancelled.class \
	ValueMustBeBlocking.class \
	ValueMustNotBeBlocking.class \
	ValueMustBeTimeout.class \
	ValueMustNotBeTimeout.class \
	\
	\
	TestBlocking.class


RESOURCES = 

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk


# Quickie blocking tests.
test_blocking:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.value.blocking.TestBlocking
