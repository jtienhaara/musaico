#!/usr/bin/make

#
# Copyright (c) 2012, 2013 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/foundation/contract


PACKAGE_DEPENDENCIES =


COMPILABLES = \
	Contract.class \
	ContractViolation.class \
	ContractUncheckedViolation.class \
	ContractCheckedViolation.class \
	AbstractContract.class \
	Obligation.class \
	NoObligation.class \
	ObligationUncheckedViolation.class \
	ObligationCheckedViolation.class \
	Guarantee.class \
	NoGuarantee.class \
	GuaranteeUncheckedViolation.class \
	GuaranteeCheckedViolation.class \
	Arbiter.class \
	\
	Domain.class \
	ArrayDomain.class \
	NoDomain.class \
	DomainObligation.class \
	NoDomainObligation.class \
	DomainObligationUncheckedViolation.class \
	DomainObligationCheckedViolation.class \
	DomainGuarantee.class \
	NoDomainGuarantee.class \
	DomainGuaranteeUncheckedViolation.class \
	DomainGuaranteeCheckedViolation.class \
	\
	ArbiterThrowsAllViolations.class \
	\
	Contracts.class \
	ObjectContracts.class

RESOURCES = \
	Messages.properties

SUB_PACKAGES = \
	domains \
	obligations \
	guarantees \
	arbiters

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
