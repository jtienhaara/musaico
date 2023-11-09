#!/usr/bin/make

#
# Copyright (c) 2015 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/module


PACKAGE_DEPENDENCIES =


COMPILABLES = \
	Compatibility.class \
	License.class \
	MusaicoModule.class \
	\
	AbstractGPLCompatibleFreeLicense.class \
	AbstractNonGPLCompatibleFreeLicense.class \
	AbstractProprietaryLicense.class \
	\
	BSDLicense.class \
	GPL3License.class \
	\
	AbstractMusaicoModule.class

RESOURCES = 

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = 


# How to build things:
PLATFORM_DIR = ../..

MUSAICO_DIR = $(PLATFORM_DIR)/../../..

include $(MUSAICO_DIR)/build/musaico.mk

PLATFORM = java

PLATFORM_DEPENDENCIES =

SUB_PLATFORMS =


# We can't testserializable here since the TestSerializable.class
# is built after this module.
IS_TEST_SERIALIZABLES = 0

# We can't make uml here since the DiaUML classes are built after this module.
IS_MAKE_UML = 0


# Now include the Java variables and rules.
include $(MUSAICO_DIR)/build/platforms/$(PLATFORM).mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk
