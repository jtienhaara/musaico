#!/usr/bin/make

#
# Copyright (c) 2009-2020 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/wiring/components


PACKAGE_DEPENDENCIES = \
	musaico/foundation/structure \
	musaico/foundation/filter \
	musaico/foundation/contract \
	musaico/foundation/contract/obligations \
	musaico/foundation/contract/guarantees \
	musaico/foundation/container \
	musaico/foundation/wiring


COMPILABLES = \
	MODULE.class \
	\
	AbstractInOut.class \
	Cable.class \
	Constant.class \
	Queue.class \
	SpecificConductorsSelector.class \
	Stack.class \
	StandardBoard.class \
	StandardCarrier.class \
	StandardSchematic.class \
	StandardSelector.class \
	StandardTap.class \
	StandardWire.class \
	StandardWireFactory.class \
	StreamIn.class \
	StreamOut.class \
	TapSelector.class \
	Terminal.class \
	Trace.class \
	Variable.class \
	Wait.class \
	\
	SchematicPrinter.class \
	\
	TestTap.class \
	TestWiring.class

RESOURCES = 

SUB_PACKAGES = 


SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk

test_tap:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.wiring.components.TestTap

test_wiring:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.wiring.components.TestWiring
