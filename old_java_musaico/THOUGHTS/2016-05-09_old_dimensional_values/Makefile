#!/usr/bin/make

#
# Copyright (c) 2009-2016 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/value/dimensional


PACKAGE_DEPENDENCIES = \
	musaico/foundation/filter \
	musaico/foundation/domains \
	musaico/foundation/contract \
	musaico/foundation/value


COMPILABLES = \
	MODULE.class \
	\
	ValueProcessor.class \
	UncheckedValueViolation.class \
	ValueViolation.class \
	\
	ValueIterator.class \
	UnchangingIterator.class \
	CyclicalIterator.class \
	\
	Value.class \
	\
	NoneGenerator.class \
	NoneConstant.class \
	NoneGuesser.class \
	\
	DefaultValues.class \
	DefaultValuesRegistry.class \
	\
	GuessingDefaultValues.class \
	\
	\
	Abnormal.class \
	Countable.class \
	Dimensional.class \
	Empty.class \
	Finite.class \
	Idempotent.class \
	IdempotentAndCountable.class \
	Immutable.class \
	Incomplete.class \
	Infinite.class \
	Just.class \
	Multiple.class \
	Mutable.class \
	NonBlocking.class \
	NotOne.class \
	ReadOnceThenImmutable.class \
	ReadOnceThenNo.class \
	Unidempotent.class \
	Unjust.class \
	ZeroOrOne.class \
	\
	AbstractMultiple.class \
	\
	ValueBuilder.class \
	\
	AndValues.class \
	OrValues.class \
	\
	AsynchronousResult.class \
	MultipleBlockingValuesCollector.class \
	\
	Acyclical.class \
	Blocking.class \
	Cyclical.class \
	Error.class \
	Indefinite.class \
	Many.class \
	No.class \
	One.class \
	Partial.class \
	PseudoRandom.class \
	Timeout.class \
	Warning.class \
	\
	CompositeValue.class \
	\
	Synchronicity.class \
	Synchronous.class \
	Asynchronous.class \
	\
	BlockingMustComplete.class \
	DefaultValuesMustOnlyBeSetOnce.class \
	DimensionalMustHaveAtLeastNDimensions.class \
	DimensionalMustHaveExactlyNDimensions.class \
	DimensionalMustHaveLessThanNDimensions.class \
	DimensionalMustHaveIndices.class \
	ValueMustBeAbnormal.class \
	ValueMustNotBeAbnormal.class \
	ValueMustBeAcyclical.class \
	ValueMustNotBeAcyclical.class \
	ValueMustBeBlocking.class \
	ValueMustNotBeBlocking.class \
	ValueMustBeCountable.class \
	ValueMustNotBeCountable.class \
	ValueMustBeCyclical.class \
	ValueMustNotBeCyclical.class \
	ValueMustBeDimensional.class \
	ValueMustNotBeDimensional.class \
	ValueMustBeEmpty.class \
	ValueMustNotBeEmpty.class \
	ValueMustBeFinite.class \
	ValueMustNotBeFinite.class \
	ValueMustBeIncomplete.class \
	ValueMustNotBeIncomplete.class \
	ValueMustBeInfinite.class \
	ValueMustNotBeInfinite.class \
	ValueMustBeJust.class \
	ValueMustNotBeJust.class \
	ValueMustBeMany.class \
	ValueMustNotBeMany.class \
	ValueMustBeMultiple.class \
	ValueMustNotBeMultiple.class \
	ValueMustBeNone.class \
	ValueMustNotBeNone.class \
	ValueMustBeNonBlocking.class \
	ValueMustNotBeNonBlocking.class \
	ValueMustBeOne.class \
	ValueMustNotBeOne.class \
	ValueMustBePartial.class \
	ValueMustNotBePartial.class \
	ValueMustBeTimeout.class \
	ValueMustNotBeTimeout.class \
	ValueMustBeUnjust.class \
	ValueMustNotBeUnjust.class \


RESOURCES = 

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
