#!/usr/bin/make

#
# Copyright (c) 2009-2018 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/order


PACKAGE_DEPENDENCIES = \
	musaico/foundation

COMPILABLES = \
	MODULE.class \
	\
	Comparison.class \
	Rank.class \
	RankFactory.class \
	\
	Order.class \
	OrderedFilter.class \
	\
	AbstractOrder.class \
	\
	ArbitraryOrder.class \
	CompositeOrder.class \
	NaturalOrder.class \
	ReverseOrder.class \
	Sequence.class \
	SequenceIterator.class


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


# Quickie dictionary order tests.
test_dictionary1:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.io.DictionaryOrder "hello1world!" "hello1world."
test_dictionary2:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.io.DictionaryOrder "hello1world!" "hello2world."
test_dictionary3:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.io.DictionaryOrder "hello2world!" "hello1world."
test_dictionary4:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.io.DictionaryOrder "hello10world!" "hello295world."
test_dictionary5:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.io.DictionaryOrder "hello100world!" "hello19world."
