#!/usr/bin/make

#
# Copyright (c) 2009 Johann Tienhaara
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

PACKAGE = musaico/kernel_modules/drivers/sql


PACKAGE_DEPENDENCIES = \
	musaico/kernel/types

COMPILABLES = \
	I18nSQLException.class \
	SQL.class

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


.PHONY: classpath
classpath:
	@echo "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)"

.PHONY: test_sql
test_sql: lib
	java -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES):/home/johann/downloads/mysql-connector-java-3.1.14/mysql-connector-java-3.1.14-bin.jar:/home/johann/Downloads/mysql-connector-java-5.1.12/mysql-connector-java-5.1.12-bin.jar" \
		musaico.kernel.drivers.TestDrive \
		musaico.kernel_modules.drivers.sql.SQL \
		--configure /dev/sql \
			driver mysql \
			user musaico \
			password ociasum \
		--initialize /dev/sql \
		--read /dev/sql \
			num_fields 256 \
		--seek /dev/sql \
			position /test/start \
		--read /dev/sql \
			num_fields 256 \
		--seek /dev/sql \
			position /test/role/start \
		--read /dev/sql \
			num_fields 256 \
		--seek /dev/sql \
			position /test/role/end \
		--sector_search /dev/sql \
			position /test/manufacturer/start \
			search_field_id address \
			search_field_glob '*bla*'
