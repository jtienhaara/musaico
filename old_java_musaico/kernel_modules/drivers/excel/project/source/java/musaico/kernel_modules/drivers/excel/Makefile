#!/usr/bin/make

#
# Copyright (c) 2011 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/kernel_modules/drivers/excel


POI_VERSION = 3.8-beta3-20110606
POI_BINARY = poi-bin-$(POI_VERSION).tar.gz
POI_JARS = \
	poi-$(POI_VERSION).jar \
	poi-examples-$(POI_VERSION).jar \
	poi-excelant-$(POI_VERSION).jar \
	poi-ooxml-$(POI_VERSION).jar \
	poi-ooxml-schemas-$(POI_VERSION).jar \
	poi-scratchpad-$(POI_VERSION).jar
# !!! need to fix up third party library copying + dependencies:
POI_DEPENDENCY_JARS = \
	$(BUILD_DIR)/compile/musaico/kernel_modules/drivers/excel/poi-$(POI_VERSION).jar \
	$(BUILD_DIR)/compile/musaico/kernel_modules/drivers/excel/poi-excelant-$(POI_VERSION).jar \
	$(BUILD_DIR)/compile/musaico/kernel_modules/drivers/excel/poi-ooxml-$(POI_VERSION).jar \
	$(BUILD_DIR)/compile/musaico/kernel_modules/drivers/excel/poi-ooxml-schemas-$(POI_VERSION).jar
POI_SOURCE = poi-src-$(POI_VERSION).tar.gz


PACKAGE_DEPENDENCIES =

THIRD_PARTY_DEPENDENCIES = \
	$(POI_DEPENDENCY_JARS)


COMPILABLES = \
	ExcelDriver.class \
	ExcelModule.class

RESOURCES = \
	Messages.properties \
	$(POI_BINARY) \
	$(POI_JARS) \
	$(POI_SOURCE)

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
