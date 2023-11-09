#!/usr/bin/make

#
# Copyright (c) 2009, 2012 Johann Tienhaara
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

PACKAGE = musaico/foundation/types


PACKAGE_DEPENDENCIES = \
	musaico/foundation/condition


COMPILABLES = \
	Tag.class \
	TypeException.class \
	TypeCastException.class \
	TypeCaster.class \
	AbstractTypeCaster.class \
	NoTypeCaster.class \
	PassThroughTypeCaster.class \
	Type.class \
	TypeBuilder.class \
	Instance.class \
	InstanceBuilder.class \
	NoInstanceBuilder.class \
	TypeSystem.class \
	TypeSystemBuilder.class \
	TypingEnvironment.class \
	NoTypeSystem.class \
	NoTypesRegistry.class \
	NoTypeCastersRegistry.class \
	NoTypingEnvironment.class \
	ConstraintException.class \
	Constraint.class \
	TypesRegistry.class \
	TypeCastersFrom.class \
	TypeCastersTo.class \
	TypeCastersRegistry.class \
	SimpleType.class \
	SimpleTypeBuilder.class \
	SimpleTypeSystem.class \
	SimpleTypeSystemBuilder.class \
	SimpleTypesRegistry.class \
	SimpleTypeCastersFrom.class \
	SimpleTypeCastersTo.class \
	SimpleTypeCastersRegistry.class \
	ChainCaster.class

RESOURCES = \
	Messages.properties

SUB_PACKAGES = \
	environments \
	primitive \
	opaque \
	tag

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
