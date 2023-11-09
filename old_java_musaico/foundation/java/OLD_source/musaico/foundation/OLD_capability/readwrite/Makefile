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

PACKAGE = musaico/foundation/capability/administrative


PACKAGE_DEPENDENCIES = \
	musaico/foundation/filter \
	musaico/foundation/domains \
	musaico/foundation/contract \
	musaico/foundation/order \
	musaico/foundation/capability


COMPILABLES = \
	MODULE.class \
	\

# !!!  \
# !!! 	\
# !!! 	AdministrativeCapability.class \
# !!! 	OperatingCapability.class \
# !!! 	PublishingCapability.class \
# !!! 	ReadCapability.class \
# !!! 	SubscribingCapability.class \
# !!! 	WriteCapability.class \
# !!! 	\
# !!! 	OperatingMode.class \
# !!! 	ReadMode.class \
# !!! 	UniqueID.class \
# !!! 	\
# !!! 	Grant.class \
# !!! 	Request.class \
# !!! 	Revoke.class \
# !!! 	\
# !!! 	Start.class \
# !!! 	Pause.class \
# !!! 	Resume.class \
# !!! 	Stop.class \
# !!! 	\
# !!! 	Publish.class \
# !!! 	\
# !!! 	Read.class \
# !!! 	\
# !!! 	Subscribe.class \
# !!! 	Unsubscribe.class \
# !!! 	\
# !!! 	Write.class \
# !!! 	\
# !!! 	Processor.class \
# !!! 	AdministerOnly.class \
# !!! 	ReadOnly.class \
# !!! 	ReadWriteOnly.class \
# !!! 	WriteOnly.class


RESOURCES = 

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk
