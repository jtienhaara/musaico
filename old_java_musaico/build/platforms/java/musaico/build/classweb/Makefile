#!/usr/bin/make

#
# Copyright (c) 2012-2015 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/build/classweb


PACKAGE_DEPENDENCIES = \
	musaico/module \
	musaico/build


COMPILABLES = \
	MODULE.class \
	\
	MemberFilter.class \
	MemberListProcessor.class \
	\
	ModifierFilter.class \
	NoMemberFilter.class \
	\
	AlphaConstructorComparator.class \
	AlphaFieldComparator.class \
	AlphaMethodComparator.class \
	CompositeMemberComparator.class \
	StaticMemberComparator.class \
	\
	MemberOrder.class \
	MemberPrinter.class \
	\
	Order.class \
	\
	ClassDetail.class \
	ClassDetailPrinter.class \
	\
	ClassDecoration.class \
	ClassEnumeratedValues.class \
	ClassIf.class \
	ClassInheritance.class \
	ClassMembers.class \
	ClassName.class \
	ClassPackage.class \
	ClassStereotype.class \
	\
	ClassDetails.class \
	\
	ClassDetailsFilter.class \
	ClassDetailsListProcessor.class \
	\
	ClassDetailsOrder.class \
	ClassDetailsPrinter.class \
	\
	ClassFilter.class \
	ClassListProcessor.class \
	ClassListProcessorFactory.class \
	\
	AlphaClassComparator.class \
	\
	ClassExtractDetails.class \
	ClassHierarchy.class \
	ClassListFilter.class \
	\
	DiaChunk.class \
	DiaObject.class \
	DiaLinear.class \
	\
	DiaBox.class \
	DiaLine.class \
	DiaText.class \
	\
	DiaGeneralization.class \
	\
	DiaRepositioner.class \
	DiaStandardRepositioner.class \
	\
	DiaUML.class \
	\
	ClassWeb.class

RESOURCES = 

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..

MUSAICO_DIR = $(PLATFORM_DIR)/../../..

include $(MUSAICO_DIR)/build/musaico.mk

PLATFORM = java

PLATFORM_DEPENDENCIES =

SUB_PLATFORMS =

# Now include the Java variables and rules.
include $(MUSAICO_DIR)/build/platforms/$(PLATFORM).mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk
