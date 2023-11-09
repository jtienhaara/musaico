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

PACKAGE = musaico/foundation/domains


PACKAGE_DEPENDENCIES = \
	musaico/foundation/filters


COMPILABLES = \
	MODULE.class \
	\
	ClassName.class \
	Seconds.class \
	StringRepresentation.class \
	\
	AllEncompassingDomain.class \
	NoDomain.class \
	SpecificDomain.class \
	\
	BloomFilter.class \
	HashedObject.class \
	\
	Generator.class \
	\
	InstanceOfClass.class \
	NotNull.class


RESOURCES = 

SUB_PACKAGES = \
	generators \
	iterators \
	elements \
	comparability \
	equality \
	number \
	array \
	string \
	time


SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk


# Quickie Bloom filter tests.
test_bloom_filter:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.domains.BloomFilter
