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

# Debugging information for figuring out why a Makefile or .mk file
# is busted.

# ==================================================================
.PHONY: makedebug
makedebug: this_makedebug sub_makedebug


.PHONY: this_makedebug
this_makedebug:
	@echo "Make debug for Musaico:"
	@echo "    PWD = `pwd`"
	@echo "    MUSAICO_DIR = $(MUSAICO_DIR)"
	@echo "    PACKAGE = $(PACKAGE)"
	@echo "    VERSION = $(VERSION)"
	@echo "    PACKAGE_DIR = $(PACKAGE_DIR)"
	@echo "    PLATFORM = $(PLATFORM)"
	@echo "    OS = $(OS)"

.PHONY: sub_makedebug
sub_makedebug:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR makedebug; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make makedebug in $$DIR"; \
	    exit 1; \
	  fi; \
	done
