#!/usr/bin/make

#
# Copyright (c) 2009 Johann Tienhaara
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

PACKAGE = musaico/kernel/driver


PACKAGE_DEPENDENCIES = \
	musaco/time \
	musaico/types \
	musaico/kernel


COMPILABLES = \
	DriverException.class \
	DriverIdentifier.class \
	Driver.class \
	BlockDriver.class \
	EjectableDriver.class \
	SchedulableDriver.class \
	SpecialDriver.class \
	StreamDriver.class \
	SuspendableDriver.class \
	TransactionException.class \
	TransactionalDriver.class \
	DriverGraph.class

RUNNABLES = 

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


.PHONY: classpath
classpath:
	@echo "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)"
