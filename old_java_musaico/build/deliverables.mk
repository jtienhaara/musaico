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

# Should be included last (after musaico.mk and so on).

# ==================================================================
.PHONY: all
all: $(DELIVERABLES)

include $(MUSAICO_DIR)/build/rules/deliverable_dependencies.mk
include $(MUSAICO_DIR)/build/rules/deliverable_main.mk
include $(MUSAICO_DIR)/build/rules/deliverable_no_recursion.mk

# Most of the time, people just want to compile the source,
# or build a library, or install, and so on.
# So we provide some default rules from the "source"
# sub-directory.
include $(MUSAICO_DIR)/build/rules/source_toplevel_no_recursion.mk


# ==================================================================
.PHONY: main_clean
main_clean:
	@for DELIVERABLE in $(DELIVERABLES); \
	do \
	  echo make -C $$DELIVERABLE clean; \
	  make -C $$DELIVERABLE clean; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make clean in $$DELIVERABLE"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: main_roadmap
main_roadmap:
	@make -C $(PROJECT_DIR)/roadmap roadmap

# ==================================================================
.PHONY: main_analysis
main_analysis:
	@make -C $(PROJECT_DIR)/analysis analysis

# ==================================================================
.PHONY: main_design
main_design:
	@make -C $(PROJECT_DIR)/design design

# ==================================================================
.PHONY: main_source
main_source:
	@make -C $(PROJECT_DIR)/java dist
# !!! todo: c, etc.

# ==================================================================
.PHONY: main_test
main_test:
	@make -C $(PROJECT_DIR)/test test

# ==================================================================
.PHONY: main_documentation
main_documentation:
	@make -C $(PROJECT_DIR)/documentation documentation

# ==================================================================
.PHONY: main_examples
main_examples:
	@make -C $(PROJECT_DIR)/examples examples

# ==================================================================
.PHONY: main_profile
main_profile:
	@make -C $(PROJECT_DIR)/profile profile
