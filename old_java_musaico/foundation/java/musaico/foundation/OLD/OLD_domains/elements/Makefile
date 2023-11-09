#!/usr/bin/make

#
# Copyright (c) 2013-2018 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/domains/elements


PACKAGE_DEPENDENCIES = \
	musaico/foundation/domains \
	musaico/foundation/domains/generators \
	musaico/foundation/domains/iterators


COMPILABLES = \
	MODULE.class \
	\
	ElementFlags.class \
	\
	Elements.class \
	\
	ElementsFlagsException.class \
	ElementsLengthException.class \
	\
	AbstractElements.class \
	\
	ArrayElements.class \
	GeneratedElements.class \
	ListElements.class \
	\
	\
	TestElements.class


# !!!  \
# !!! 	\
# !!! 	ElementsIndexComparator.class \
# !!! 	\


# !!! 	StandardElements.class \


# !!! 	Indices.class \
# !!! 	\
# !!! 	RangeOfElements.class


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


# Quickie elements tests.
test_elements:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.domains.elements.TestElements
