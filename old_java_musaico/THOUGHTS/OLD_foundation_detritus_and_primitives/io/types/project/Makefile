#
# Copyright (c) 2010, 2012 Johann Tienhaara <jtienhaara@yahoo.com>
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

DELIVERABLES = \
	roadmap \
	analysis \
	design \
	source \
	test \
	profile \
	examples \
	documentation

SUB_DIRS = $(DELIVERABLES)

PROJECT_DIR = .
include $(PROJECT_DIR)/Project.mk

# Toplevel rules for the user (source, test, design, and so on).
include $(MUSAICO_DIR)/Deliverables.mk
