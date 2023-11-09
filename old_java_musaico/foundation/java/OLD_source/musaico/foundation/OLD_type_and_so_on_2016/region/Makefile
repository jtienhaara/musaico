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

PACKAGE = musaico/foundation/region


PACKAGE_DEPENDENCIES = \
	musaico/foundation


COMPILABLES = \
	MODULE.class \
	\
	RegionViolation.class \
	RegionObligation.class \
	\
	SpatialElement.class \
	Position.class \
	PositionExpression.class \
	Size.class \
	SizeExpression.class \
	Region.class \
	SparseRegion.class \
	SparseRegionBuilder.class \
	RegionExpression.class \
	Space.class \
	\
	DomainElementBelongsToSpace.class \
	DomainPositionBelongsToRegion.class \
	DomainPositionMatchingCriteria.class \
	DomainRegionMatchingCriteria.class \
	DomainRegionNonEmpty.class \
	DomainRegionWithSizeOne.class \
	DomainSubRegionMustBeInBounds.class \
	\
	NoPosition.class \
	NoRegion.class \
	NoSparseRegion.class \
	NoSpace.class \
	\
	FailedPositionExpression.class \
	FailedSizeExpression.class \
	FailedRegionExpression.class \
	FailedSparseRegionBuilder.class \
	ZeroSizeExpression.class \
	EmptyRegionExpression.class \
	\
	Searcher.class \
	\
	FailedSearcher.class \
	\
	PositionStringOrder.class \
	RegionOrder.class \
	SizeStringOrder.class \
	\
	SpecificPosition.class \
	\
	RegionalPositionExpression.class \
	SparseRegionalPositionExpression.class \
	RegionalPositionIterator.class \
	StandardRegionExpression.class \
	StandardSparseRegion.class

RESOURCES = 

SUB_PACKAGES = \
	search

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk
