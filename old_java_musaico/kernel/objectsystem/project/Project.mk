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

# PROJECT_DIR must be defined from the Makefile which includes this.
# For example:
#
#     PROJECT_DIR = ../..
#

PROJECT = musaico/kernel/objectsystem

MUSAICO_DIR = $(PROJECT_DIR)/../../..
include $(MUSAICO_DIR)/Musaico.mk

PROJECT_DEPENDENCIES = \
	musaico/i18n \
	musaico/time \
	musaico/hash \
	musaico/mutex \
	musaico/types \
	musaico/io \
	musaico/io/types \
	musaico/region \
	musaico/region/types \
	musaico/field \
	musaico/buffer \
	musaico/security \
	musaico/pubsub \
	musaico/state \
	musaico/kernel \
	musaico/kernel/memory \
	musaico/kernel/task
