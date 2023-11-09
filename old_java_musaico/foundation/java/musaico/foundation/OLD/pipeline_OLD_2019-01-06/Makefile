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

PACKAGE = musaico/foundation/pipeline


PACKAGE_DEPENDENCIES = \
	musaico/foundation/filter \
	musaico/foundation/domains \
	musaico/foundation/contract \
	musaico/foundation/order \
	musaico/foundation/capability


COMPILABLES = \
	MODULE.class \
	\
	Branch.class \
	Construct.class \
	Parent.class \
	Pipeline.class \
	Sink.class \
	Source.class \
	Subsidiary.class \
	\
	Choice.class \
	Choose.class \
	Construction.class \
	Constructor.class \
	\
	Decision.class \
	Edit.class \
	EditInsert.class \
	EditMove.class \
	EditSequence.class \
	EditSet.class \
	EditVarious.class \
	Fork.class \
	Join.class \
	OrderBy.class \
	Select.class \
	When.class \
	Where.class \
	\
	SubsidiaryMustBeEnded.class \
	SubsidiaryMustNotBeEnded.class \
	\
	\
	Tests.class


RESOURCES = 


SUB_PACKAGES = 


SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk


# Quickie constructor and choose tests.
test_constructor:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.pipeline.Tests
