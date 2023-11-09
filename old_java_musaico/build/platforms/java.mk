#
# Copyright (c) 2009-2015 Johann Tienhaara
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

# platform.mk should include this AFTER sourcing musaico.mk,
# and AFTER defining the PLATFORM, PLATFORM_SUB_DIR,
# PLATFORM_DEPENDENCIES, SUB_PLATFORMS,
# THIRD_PARTY_DEPENDENCIES,
# and also the PACKAGE, SUB_PACKAGES, PACKAGE_DEPENDENCIES
# and COMPILABLES and RUNNABLES variables.
# Note that PACKAGE_DIR is defined in musaico.mk (from PACKAGE).

# ==================================================================
# Defaults for parameters:
BUILD_DIR ?= $(MUSAICO_DIR)/_build
JAVADOC_OVERVIEW ?= $(PLATFORM_DIR)/overview-none.html
JAR_MANIFEST ?= $(MUSAICO_DIR)/build/platforms/java/MANIFEST-template.MF

# Can be turned off from the commandline by setting env variables:
IS_TEST_SERIALIZABLES ?= 1
IS_MAKE_UML ?= 1

# ==================================================================
# Internally used variables:
JAVA_HOME ?= $(COMPILERS_BASE_DIR)

JAVA_PACKAGE = \
	$(subst /,.,$(PACKAGE))

JAVA_CLASSES = \
	$(patsubst %.class,$(JAVA_PACKAGE).%,$(COMPILABLES))

PACKAGE_DEPENDENCY_PATHS = \
	$(patsubst %,$(PLATFORM_DIR)%,$(PACKAGE_DEPENDENCIES))

PACKAGE_DEPENDENCY_PACKAGES = \
	$(subst /,.,$(PACKAGE_DEPENDENCIES))

DELIVERABLE_DEPENDENCY_PATHS = \
	$(patsubst %,$(PROJECT_DIR)/java,$(DELIVERABLE_DEPENDENCIES))

CLASSPATH_COMPILE = \
	$(BUILD_DIR)/compile:$(PLATFORM_DIR)

CLASSPATH_PACKAGE_DEPENDENCIES_SPACE_SEPARATED = \
	$(patsubst %,%/$(BUILD_DIR)/compile,$(PACKAGE_DEPENDENCY_PATHS))

# Classpath does not depend on other deliverables (analysis, design,
# roadmap, documentation and so on) for now.
CLASSPATH_DELIVERABLE_DEPENDENCIES_SPACE_SEPARATED =

CLASSPATH_DEPENDENCIES_SPACE_SEPARATED = \
	$(BUILD_DIR)/compile $(THIRD_PARTY_DEPENDENCIES)

CLASSPATH_DEPENDENCIES = \
	$(subst  $(SPACE),$(COLON),$(CLASSPATH_DEPENDENCIES_SPACE_SEPARATED))

CLASSPATH_RUN = \
	$(LIB)

CLASSPATH_RUN_DEPENDENCIES = \
	$(CLASSPATH_DEPENDENCIES)


# ==================================================================
# Variables needed for the platform_xxx rules:
EXTENSION_SOURCE = java
EXTENSION_TARGET = class
EXTENSION_LIB = jar

LIB_CLEAN = MANIFEST.MF
BIN_CLEAN = 

DOC_ROOT_SOURCE = $(JAVADOC_OVERVIEW)
DOC_ROOT_TARGET = $(BUILD_DIR)/doc/$(PACKAGE_DIR)/index.html


COMPILE_BIN = $(JAVA_HOME)/bin/javac
COMPILE_PRE = \
	-Xlint:all \
	-Werror \
	-classpath $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES) \
	-d $(BUILD_DIR)/compile
COMPILE = \
	$(patsubst %.$(EXTENSION_TARGET),%.$(EXTENSION_SOURCE),$(COMPILABLES))
COMPILE_POST = 

# Take raw .class, .o, etc filenames and prepend $(BUILD_DIR)/compile/...:
BUILD_DIR_COMPILES = \
	$(patsubst %.$(EXTENSION_TARGET),$(BUILD_DIR)/compile/$(PACKAGE_DIR)/%.$(EXTENSION_TARGET),$(COMPILABLES))


DOC_BIN = $(JAVA_HOME)/bin/javadoc
DOC_PRE = \
	-overview $(JAVADOC_OVERVIEW) \
	-sourcepath $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES):$(PLATFORM_DIR) \
	-classpath $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES) \
	-d $(BUILD_DIR)/doc \
	-windowtitle "Musaico $(PACKAGE)" \
	-doctitle "Musaico $(VERSION)" \
	-quiet
DOC = \
	$(DEPENDENCY_PACKAGES) \
	$(JAVA_PACKAGE)
DOC_POST = 

LIB_BIN = $(JAVA_HOME)/bin/jar
LIB_PRE = \
	cmf $(BUILD_DIR)/lib/$(PACKAGE_DIR)/MANIFEST.MF
LIB = $(BUILD_DIR)/lib/$(PACKAGE_FILENAME).$(VERSION).jar
LIB_POST = \
	-C $(BUILD_DIR)/compile .

BIN_BIN = $(JAVA_HOME)/bin/java
BIN_PRE = \
	-classpath $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES)
BIN = \
	$(patsubst %.class,$(JAVA_PACKAGE).%,$(RUNNABLES))
BIN_POST = 

RUN_BIN = $(JAVA_HOME)/bin/java
RUN_PRE = -cp $(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)
RUN = $(BIN)
RUN_POST = 

DIST_BIN = 
DIST_PRE = 
DIST = \
	$(PACKAGE_FILENAME).$(VERSION).$(PLATFORM).tar.gz
PROFILE_POST = 

PROFILE_BIN = /usr/bin/time
PROFILE_PRE = $(JAVA_HOME)/bin/java \
	-classpath $(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)
PROFILE = $(RUN)
PROFILE_POST = 


#
# Prevent default from being our first rule:
#
# ==================================================================
.PHONY: all
all:


#
# Special Java platform-specific rules:
#

#
# Compiles UnicastRemoteObjects and anything implementing the Remote
# interface, then checks to make sure no Remote classes were left out:
#
.PHONY: rmi
rmic: rmi_compile rmi_check

REMOTE_CLASSES = $(JAVA_CLASSES)

.PHONY: rmi_compile
rmi_compile:
	$(JAVA_HOME)/bin/rmic \
		-classpath $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES) \
		-d $(BUILD_DIR)/compile \
		$(REMOTE_CLASSES)

.PHONY: rmi_check
rmi_check:


#
# Generates serialVersionUIDs for Serializable classes which implement
# a serialVersionUIDObject () method:
#
.PHONY: serializable
serializable: testserializable

#
# Generates serialVersionUIDs for Serializable classes which implement
# a serialVersionUIDObject () method:
#
.PHONY: serialVersionUID
serialVersionUID:
	$(RUN_BIN) \
		-cp $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES) \
		musaico.build.ModuleSerialVersionUIDGenerator $(JAVA_CLASSES)


#
# Checks that the current serialVersionUID is up-to-date with the
# generated hash for the class.
#
.PHONY: testserializable
testserializable:
	if test $(IS_TEST_SERIALIZABLES) -eq 1; \
	then \
		$(RUN_BIN) \
			-cp $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES) \
			musaico.build.TestSerializable $(JAVA_CLASSES) ; \
	fi


# ==================================================================
.PHONY: platform_clean
platform_clean: remove_build_dirs remove_lib_file remove_bin_file \
		remove_dist_file \
		platform_clean_check

.PHONY: remove_build_dirs
remove_build_dirs:
	@$(REMOVE_DIRS) \
		$(BUILD_DIR)/*/$(PACKAGE_DIR); \
	if test $$? -ne 0; \
	then \
	  echo "Failed to remove build directories"; \
	  exit 1; \
	fi

.PHONY: remove_lib_file
remove_lib_file:
	@$(REMOVE) $(LIB); \
	if test $$? -ne 0; \
	then \
	  echo "Failed to remove LIB"; \
	  exit 1; \
	fi

.PHONY: remove_bin_file
remove_bin_file:
	@$(REMOVE) $(BIN); \
	if test $$? -ne 0; \
	then \
	  echo "Failed to remove BIN"; \
	  exit 1; \
	fi

.PHONY: remove_dist_file
remove_dist_file:
	@$(REMOVE) $(DIST); \
	if test $$? -ne 0; \
	then \
	  echo "Failed to remove DIST"; \
	  exit 1; \
	fi

	@for FILE_OR_DIR in $(LIB_CLEAN); \
	do \
	  if test ! -f $(BUILD_DIR)/lib/$(PLATFORM_DIR)/$$FILE_OR_DIR; \
	  then \
	    continue; \
	  fi; \
	  $(REMOVE_DIRS) $(BUILD_DIR)/lib/$(PLATFORM_DIR)/$$FILE_OR_DIR; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to remove LIB_CLEAN file/directory"; \
	    exit 1; \
	  fi; \
	done
	@for FILE_OR_DIR in $(BIN_CLEAN); \
	do \
	  if test ! -f $(BUILD_DIR)/bin/$(PLATFORM_DIR)/$$FILE_OR_DIR; \
	  then \
	    continue; \
	  fi; \
	  $(REMOVE_DIRS) $(BUILD_DIR)/bin/$(PLATFORM_DIR)/$$FILE_OR_DIR; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to remove BIN_CLEAN file/directory"; \
	    exit 1; \
	  fi; \
	done

.PHONY: platform_clean_check
platform_clean_check:
	@IS_BUILD_DIR_OK="true"; \
	if test -z "$(BUILD_DIR)"; \
	then \
	  IS_BUILD_DIR_OK="false"; \
	elif test ! -d $(BUILD_DIR); \
	then \
	  IS_BUILD_DIR_OK="true"; \
	elif test ! -r $(BUILD_DIR) \
		-o ! -w $(BUILD_DIR) \
		-o ! -x $(BUILD_DIR); \
	then \
	  IS_BUILD_DIR_OK="false"; \
	fi; \
	if test "$$IS_BUILD_DIR_OK" != "true"; \
	then \
	  echo "Cannot clean build directory '$(BUILD_DIR)'."; \
	  echo "  Directory exists?  `test -d $(BUILD_DIR); echo $$?`"; \
	  echo "  Readable?          `test -r $(BUILD_DIR); echo $$?`"; \
	  echo "  Writable?          `test -w $(BUILD_DIR); echo $$?`"; \
	  echo "  Executable?        `test -x $(BUILD_DIR); echo $$?`"; \
	  exit 1; \
	fi

# ==================================================================
.PHONY: platform_prebuild
platform_prebuild:
	@if test ! -d $(BUILD_DIR); \
	then \
	  echo "Pre-build: making $(BUILD_DIR)"; \
	  $(MKDIR) $(BUILD_DIR); \
	  if test $$? -ne 0; \
	  then \
	    echo "Pre-build failed"; \
	    exit 1; \
	  fi; \
	fi

	@for BUILD_SUB_DIR in \
	    clean \
	    prebuild \
	    dependsinfo \
	    dependscheck \
	    compile \
	    optimize \
	    doc \
	    examples \
	    lib \
	    bin \
	    dist \
	    install \
	    uninstall \
	    run \
	    profile \
	    test \
	    help; \
	do \
	  if test ! -d $(BUILD_DIR)/$$BUILD_SUB_DIR; \
	  then \
	    echo "Pre-build: making $(BUILD_DIR)/$$BUILD_SUB_DIR"; \
	    mkdir $(BUILD_DIR)/$$BUILD_SUB_DIR; \
	    if test $$? -ne 0; \
	    then \
	      echo "Pre-build failed at $$BUILD_SUB_DIR"; \
	      exit 1; \
	    fi; \
	  fi; \
	done

# ==================================================================
.PHONY: platform_dependsinfo
platform_dependsinfo:
	@echo "Package dependencies within this project:"
	@echo "$(PACKAGE_DEPENDENCIES)" | sed 's| |\n|g'
	@echo ""
	@echo "Java Classpath dependencies:"
	@echo "$(CLASSPATH_DEPENDENCIES_SPACE_SEPARATED)" | sed 's| |\n|g'

# ==================================================================
.PHONY: platform_dependscheck
platform_dependscheck: platform_prebuild
	@for DEPENDENCY in $(PACKAGE_DEPENDENCIES); \
	do \
	  echo "Checking dependency '$$DEPENDENCY':"; \
	  DEPENDENCY_PATH="$(PLATFORM_DIR)/$$DEPENDENCY"; \
	  cd $$DEPENDENCY_PATH; \
	  if test $$? -ne 0; \
	  then \
	    echo "Dependency $$DEPENDENCY failed"; \
	    exit 1; \
	  fi; \
	  make lib; \
	  if test $$? -ne 0; \
	  then \
	    echo "Dependency $$DEPENDENCY failed"; \
	    exit 1; \
	  fi; \
	  cd -; \
	done

# ==================================================================
.PHONY: platform_compile
platform_compile: platform_prebuild java_compile java_post_compile \
	compile_uml

# ==================================================================
.PHONY: java_compile
java_compile:
	@if test ! -z "$(RESOURCES)"; \
	then \
	  for RESOURCE in $(RESOURCES) ; \
	  do \
	    RESOURCE_DESTINATION=`$(PARENT_DIR) $(BUILD_DIR)/compile/$(PACKAGE_DIR)/$${RESOURCE}`; \
	    if test ! -d $${RESOURCE_DESTINATION} ; \
	    then \
	      echo $(MKDIR) $${RESOURCE_DESTINATION} ; \
	      $(MKDIR) $${RESOURCE_DESTINATION} ; \
	    fi; \
	    echo $(COPY) $${RESOURCE} $${RESOURCE_DESTINATION} ; \
	    $(COPY) $${RESOURCE} $${RESOURCE_DESTINATION} ; \
	  done; \
	fi

	@if test -z "$(COMPILE)"; \
	then \
	  echo "Nothing to compile."; \
	  exit 0; \
	else \
	  echo $(COMPILE_BIN) $(COMPILE_PRE) $(COMPILE) $(COMPILE_POST); \
	  $(COMPILE_BIN) $(COMPILE_PRE) $(COMPILE) $(COMPILE_POST); \
	fi

# Too slow for platform_compile to depend on $(BUILD_DIR_COMPILES)

# Compile each .java file into a .class file:
$(BUILD_DIR)/compile/$(PACKAGE_DIR)/%.$(EXTENSION_TARGET) : %.$(EXTENSION_SOURCE)
	$(COMPILE_BIN) $(COMPILE_PRE) $< $(COMPILE_POST)
	@$(REMOVE) $(DOC_ROOT_TARGET)
	@$(REMOVE) $(LIB)

# ==================================================================
.PHONY: java_post_compile
java_post_compile: rmi serializable

# ==================================================================
.PHONY: platform_optimize
platform_optimize: platform_compile

# ==================================================================
# Include design diagrams (class, sequence, and so on) in the javadoc.
# We just plunk it in the classpath, include the classpath in
# the sourcepath, and then let javadoc do its annoying magic to pull
# it out of the doc-files directory.  That way we don't have to have
# an extra doc-files in the source directory containing generated files.
.PHONY: platform_doc
platform_doc: platform_compile \
		javadoc_clean_doc_files_dir javadoc_analysis_design \
		javadoc_from_source

# javadoc refuses to copy updated doc-files contents from sourcepath to
# output directory.  So we force clean it every time we make doc.
.PHONY: javadoc_clean_doc_files_dir
javadoc_clean_doc_files_dir:
	$(REMOVE_DIRS) $(BUILD_DIR)/doc/$(PACKAGE_DIR)/doc-files

.PHONY: javadoc_analysis_design
javadoc_analysis_design: musaico_analysis musaico_design \
		javadoc_doc_files_dir javadoc_copy_analysis_design

.PHONY: musaico_analysis
musaico_analysis:
	@if test ! -f $(PROJECT_DIR)/analysis/Makefile; \
	then \
	    echo "No Makefile in $(PROJECT_DIR)/analysis"; \
	else \
	    make -C $(PROJECT_DIR)/analysis analysis; \
	    if test $$? -ne 0; \
	    then \
	        exit 1; \
	    fi; \
	fi

.PHONY: musaico_design
musaico_design:
	@if test ! -f $(PROJECT_DIR)/design/Makefile; \
	then \
	    echo "No Makefile in $(PROJECT_DIR)/design"; \
	else \
	    make -C $(PROJECT_DIR)/design design; \
	    if test $$? -ne 0; \
	    then \
	        exit 1; \
	    fi; \
	fi

.PHONY: javadoc_doc_files_dir
javadoc_doc_files_dir: $(BUILD_DIR)/compile/$(PACKAGE_DIR)/doc-files

$(BUILD_DIR)/compile/$(PACKAGE_DIR)/doc-files:
	$(MKDIR) $(BUILD_DIR)/compile/$(PACKAGE_DIR)/doc-files


.PHONY: javadoc_copy_analysis_design
javadoc_copy_analysis_design:
	@for DELIVERABLE in analysis design; \
	do \
	  echo "Copying $(PACKAGE) $$DELIVERABLE documents."; \
	  for FILE in $(MUSAICO_DIR)/_build/compile/*; \
	  do \
	    if test ! -f $$FILE; \
	    then \
	      continue; \
	    fi; \
	    echo "  Copying $$FILE"; \
	    $(COPY_DIRS) $$FILE \
	    	$(BUILD_DIR)/compile/$(PACKAGE_DIR)/doc-files; \
	    if test $$? -ne 0; \
	    then \
	      echo "Failed to copy javadoc documents from analysis/design"; \
	      exit 1; \
	    fi; \
	  done; \
	done

# Compile Javadoc tree from the source code:
.PHONY: javadoc_from_source
javadoc_from_source: platform_compile $(DOC_ROOT_TARGET)

# Build the Javadoc documentation:
$(DOC_ROOT_TARGET) : $(DOC_ROOT_SOURCE)
	@echo $(DOC_BIN) $(DOC_PRE) $(DOC) $(DOC_POST)
	@$(DOC_BIN) $(DOC_PRE) $(DOC) $(DOC_POST); \
	echo $$?; \
	if test $$? -ne 0; \
	then \
	  echo "Failed to make javadocs from $(DOC_ROOT_SOURCE)"; \
	  exit 1; \
	fi


# ==================================================================
.PHONY: platform_examples
platform_examples: platform_compile

# ==================================================================
# Create the jar MANIFEST.MF before building the jar file.
.PHONY: platform_lib
platform_lib: platform_optimize pre_lib jar_manifest $(LIB)

.PHONY: pre_lib
pre_lib:
	@if test ! -d "${BUILD_DIR}/lib/${PACKAGE}"; \
	then \
	  mkdir -p "${BUILD_DIR}/lib/${PACKAGE}"; \
	fi

.PHONY: jar_manifest
jar_manifest:
	@echo "Creating MANIFEST.MF for package $(PACKAGE) version $(VERSION)"
	@if test ! -z "$(JAR_MANIFEST)" -a ! -f "$(JAR_MANIFEST)"; \
	then \
	  echo "$(JAR_MANIFEST) does not exist!"; \
	  exit 1; \
	fi
	@cat $(JAR_MANIFEST) \
		| sed 's:\[% package %\]:$(PACKAGE_FILENAME):g' \
		| sed 's:\[% version %\]:$(VERSION):g' \
		> $(BUILD_DIR)/lib/$(PACKAGE_DIR)/MANIFEST.MF; \
	if test $$? -ne 0; \
	then \
	  echo "Failed to create MANIFEST.MF"; \
	  exit 1; \
	fi

# Build .jar file for the package:
%.$(EXTENSION_LIB)::
	$(LIB_BIN) $(LIB_PRE) $@ $(LIB_POST)

# ==================================================================
.PHONY: platform_bin
platform_bin: platform_lib

# ==================================================================
.PHONY: platform_dist
platform_dist: platform_bin

# ==================================================================
.PHONY: platform_install
platform_install:

# ==================================================================
.PHONY: platform_uninstall
platform_uninstall:

# ==================================================================
.PHONY: platform_run
platform_run:
	@if test -z "$(BIN)"; \
	then \
	  echo "Nothing to be done to make run."; \
	  exit 0; \
	fi; \
	echo $(BIN_BIN) $(BIN_PRE) $(BIN) $(BIN_POST); \
	$(BIN_BIN) $(BIN_PRE) $(BIN) $(BIN_POST); \
	if test $$? -ne 0; \
	then \
	  echo "Failed to make run"; \
	  exit 1; \
	fi

# ==================================================================
.PHONY: platform_profile
platform_profile:

# ==================================================================
.PHONY: platform_test
platform_test:

# ==================================================================
.PHONY: platform_help
platform_help:


# ==================================================================
# Alias rules:
.PHONY: classes
classes: platform_compile

.PHONY: javadoc
javadoc: platform_doc

.PHONY: jar
jar: platform_lib


# ==================================================================
.PHONY: compile_uml
compile_uml:
	if test $(IS_MAKE_UML) -eq 1; \
	then \
		make uml; \
	fi

.PHONY: uml
uml:
	$(RUN_BIN) \
		-cp $(CLASSPATH_COMPILE):$(CLASSPATH_DEPENDENCIES) \
		musaico.build.classweb.ClassWeb \
			-uml UML_$(PACKAGE_FILENAME).dia \
			$(JAVA_CLASSES)
