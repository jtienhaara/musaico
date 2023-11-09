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

PACKAGE = musaico/foundation/term/contracts


PACKAGE_DEPENDENCIES = \
	musaico/foundation/filter \
	musaico/foundation/domains \
	musaico/foundation/contract \
	musaico/foundation/order \
	musaico/foundation/capability \
	musaico/foundation/pipeline \
	musaico/foundation/term \
	musaico/foundation/term/pipeline \
	musaico/foundation/term/multiplicities


COMPILABLES = \
	MODULE.class \
	\
	CountableLengthMustBe.class \
	CountableMustContainOnlySpecificClasses.class \
	CountableMustContainElements.class \
	CountableMustIncludeIndices.class \
	CountableMustExcludeSpecificClasses.class \
	CountableMustExcludeElements.class \
	ElementsMustBeInstancesOfClass.class \
	ElementsMustBelongToDomain.class \
	ElementsMustMatchFilters.class \
	ElementsMustNotBelongToDomain.class \
	ElementsMustNotMatchFilters.class \
	\
	TermFilterStateMustBeKept.class \
	TermFilterStateMustNotBeKept.class \
	\
	TermMustBelongToDomain.class \
	TermMustNotBelongToDomain.class \
	TermMustDeclareType.class \
	TermMustNotDeclareType.class \
	TermMustEqual.class \
	TermMustNotEqual.class \
	TermMustMatchFilters.class \
	TermMustNotMatchFilters.class \
	TermMustMatchType.class \
	TermMustNotMatchType.class \
	\
	TermMustMeetAllContracts.class \
	TermMustMeetAtLeastOneContract.class \
	TermMustMeetExactlyOneContract.class \
	TermMustNotMeetAnyContract.class \
	\
	ValueMustBeCountable.class \
	ValueMustNotBeCountable.class \
	ValueMustBeDefinite.class \
	ValueMustNotBeDefinite.class \
	ValueMustBeEmpty.class \
	ValueMustNotBeEmpty.class \
	ValueMustBeFinite.class \
	ValueMustNotBeFinite.class \
	ValueMustBeIndefinite.class \
	ValueMustNotBeIndefinite.class \
	ValueMustBeInfinite.class \
	ValueMustNotBeInfinite.class \
	ValueMustBeJust.class \
	ValueMustNotBeJust.class \
	ValueMustBeMaybe.class \
	ValueMustNotBeMaybe.class \
	ValueMustBeMultiple.class \
	ValueMustNotBeMultiple.class \
	ValueMustBeOneOrMore.class \
	ValueMustNotBeOneOrMore.class \
	ValueMustBeUnjust.class \
	ValueMustNotBeUnjust.class \
	\
	ValueMustBelongToDomain.class \
	ValueMustNotBelongToDomain.class \
	ValueMustEqual.class \
	ValueMustNotEqual.class \
	ValueMustMatchFilters.class \
	ValueMustNotMatchFilters.class \
	\
	TypeMustBeInstanceOfClass.class \
	TypeMustNotBeInstanceOfClass.class \
	TypeMustBelongToDomain.class \
	TypeMustNotBelongToDomain.class \
	TypeMustEqual.class \
	TypeMustNotEqual.class \
	TypeMustMatchFilters.class \
	TypeMustNotMatchFilters.class \
	TypeMustMeetAllContracts.class \
	TypeMustMeetAtLeastOneContract.class \
	TypeMustMeetExactlyOneContract.class \
	TypeMustNotMeetAnyContract.class \
	\
	TypesMustMatch.class \
	TypesMustNotMatch.class

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
