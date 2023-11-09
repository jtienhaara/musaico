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

PACKAGE = musaico/foundation/wiring


PACKAGE_DEPENDENCIES = \
	musaico/foundation/structure \
	musaico/foundation/filter \
	musaico/foundation/contract \
	musaico/foundation/contract/obligations \
	musaico/foundation/contract/guarantees \
	musaico/foundation/container


COMPILABLES = \
	MODULE.class \
	\
	Board.class \
	Carrier.class \
	Conductor.class \
	Schematic.class \
	Selector.class \
	Tap.class \
	Wire.class \
	\
	Tag.class \
	TagLibrary.class \
	TagNamespace.class \
	TagPathOrder.class \
	Tags.class \
	\
	Carriers.class \
	NoBoard.class \
	NoCarrier.class \
	NoConductor.class \
	NoSchematic.class \
	NoSelector.class \
	NoTap.class \
	NoWire.class

RESOURCES = 

SUB_PACKAGES = \
	components


SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk
