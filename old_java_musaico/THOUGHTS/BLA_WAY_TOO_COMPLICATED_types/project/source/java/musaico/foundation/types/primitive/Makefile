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

PACKAGE = musaico/foundation/types/primitive


PACKAGE_DEPENDENCIES = \
	musaico/foundation/types


COMPILABLES = \
	NoNumber.class \
	PrimitiveType.class \
	PrimitiveTypeBuilder.class \
	CastBigDecimalToBigInteger.class \
	CastBigDecimalToDouble.class \
	CastBigDecimalToFloat.class \
	CastBigDecimalToInteger.class \
	CastBigDecimalToLong.class \
	CastBigDecimalToString.class \
	CastBigIntegerToBigDecimal.class \
	CastBigIntegerToDouble.class \
	CastBigIntegerToFloat.class \
	CastBigIntegerToInteger.class \
	CastBigIntegerToLong.class \
	CastBigIntegerToString.class \
	CastBytesToDouble.class \
	CastBytesToFloat.class \
	CastBytesToInteger.class \
	CastBytesToLong.class \
	CastBytesToMD5.class \
	CastBytesToSHA1.class \
	CastBytesToSHA256.class \
	CastBytesToSHA512.class \
	CastBytesToString.class \
	CastBytesToTime.class \
	CastCalendarToTime.class \
	CastDateToTime.class \
	CastDoubleToBigDecimal.class \
	CastDoubleToBigInteger.class \
	CastDoubleToBytes.class \
	CastDoubleToFloat.class \
	CastDoubleToInteger.class \
	CastDoubleToLong.class \
	CastDoubleToString.class \
	CastFloatToBigDecimal.class \
	CastFloatToBigInteger.class \
	CastFloatToBytes.class \
	CastFloatToDouble.class \
	CastFloatToInteger.class \
	CastFloatToLong.class \
	CastFloatToString.class \
	CastHashToBigInteger.class \
	CastHashToBytes.class \
	CastHashToString.class \
	CastIntegerToBigDecimal.class \
	CastIntegerToBigInteger.class \
	CastIntegerToBytes.class \
	CastIntegerToDouble.class \
	CastIntegerToFloat.class \
	CastIntegerToLong.class \
	CastIntegerToString.class \
	CastLongToBigDecimal.class \
	CastLongToBigInteger.class \
	CastLongToBytes.class \
	CastLongToDouble.class \
	CastLongToFloat.class \
	CastLongToInteger.class \
	CastLongToString.class \
	CastStringToBigDecimal.class \
	CastStringToBigInteger.class \
	CastStringToBytes.class \
	CastStringToDouble.class \
	CastStringToFloat.class \
	CastStringToInteger.class \
	CastStringToLong.class \
	CastStringToTime.class \
	CastTimeToCalendar.class \
	CastTimeToDate.class \
	CastTimeToBytes.class \
	CastTimeToString.class \
	CastableToPrimitiveTypeSystemBuilder.class \
	PrimitiveTypeSystemBuilder.class

RESOURCES = \
	Messages.properties

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
