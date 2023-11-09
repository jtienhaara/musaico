#!/usr/bin/make

#
# Copyright (c) 2011-2018 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/topology


PACKAGE_DEPENDENCIES = \
	musaico/foundation/value \
	musaico/foundation/typing


COMPILABLES = \
	MODULE.class \
	\
	\
	TopologyOperations.class \
	\
	Points.class \
	Measure.class \
	Region.class \
	\
	PointsExpression.class \
	MeasureExpression.class \
	RegionExpression.class \
	\
	Difference.class \
	Exclude.class \
	Group.class \
	Intersection.class \
	Invert.class \
	Mid.class \
	Scale.class \
	SplitBy.class \
	SplitInto.class \
	SubRegion.class \
	Union.class \
	\
	TopologyTypeClass.class \
	TopologyBuilder.class \
	Topology.class \
	\
	TestTopology.class


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


# Quickie Topology tests.
test_topology:
	$(BIN_BIN) -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.foundation.topology.TestTopology
