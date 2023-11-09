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

# MUSAICO_DIR must be defined from the Makefile which includes this.
# For example:
#
#     MUSAICO_DIR = ../..
#

# SPACE and COLON must also be defined (include Musaico.mk).
#

THIRD_PARTY_DIR = $(MUSAICO_DIR)/third_party


#
# The macro MUSAICO_DIR must point to the toplevel directory
# from wherever this makefile is included.
#
# For example, if this file is included from the directory
# kernel_modules/drivers/foo/source/intercal/musaico/drivers/foo, then the
# MUSAICO_DIR macro is set to "../../../../../../../..".
#

#
# Platform-specific macros (defined in ../platforms/xyz.mk) must
# also be set before using platform-specific third party tools.
# For example, in order to run a third party Java program, macros
# for Java RUN_BIN, RUN_PRE and RUN_POST mut be defined.
#

#
# General macros:
#
# - All third party software "FOO" must have defined:
#     - FOO_VERSION : e.g. 1.0.0
#                     Allows remaining macros which point to zips and
#                     directories and so on to be version-independent.
#     - FOO_ZIPS : e.g. "foo-$(VERSION).zip foo-$(VERSION)-source.zip"
#                  List of .zip files which must be opened in the
#                  "third_party" directory before the build system can
#                  use the software.
#     - FOO_TAR_GZS : e.g. "foo-$(VERSION).tar.gz foo-$(VERSION)-source.tar.gz"
#                     List of .tar.gz files which must be opened in
#                     the "third_party" directory before the build system
#                     can use the software.
#     - FOO_DIR : e.g. "foo-$(VERSION)"
#                 The single directory created when the .zip and .tar.gz
#                 files for the software have all been opened.
#
#
# - Library "FOO" must also have defined:
#     - FOO_LIB : e.g. "foo-base.so foo-magic-wand.so foo-sparkly-cape.so"
#                 List of the libraries (archives, shared objects,
#                 jars, and so on) which must be in the library path
#                 in order for tools to use the library.
#     - FOO_DEPENDENCIES : e.g. "$(patsubst %,$(FOO_DIR)/%,$(FOO_LIB))"
#                          List of the library files with complete paths,
#                          so that makefiles can fail quickly if the
#                          library dependencies have not been properly
#                          set up prior to building.
#     - FOO_LD_LIBRARY_PATH : e.g. "$(subst  $(SPACE),$(COLON),$(FOO_DEPENDENCIES))"
#                       For C libraries.  Used to run C programs
#                       which use the library.
#     - FOO_CLASSPATH : e.g. "$(subst  $(SPACE),$(COLON),$(FOO_DEPENDENCIES))"
#                       For Java libraries.  Used to run Java programs
#                       which use the library.
#
#
# - Executable "FOO" must also have defined:
#     - FOO_BIN : e.g. "$(FOO_DIR)/bin/foo"
#                 The full path to the command to run.
#     - FOO_PRE : e.g. "-x -y -z"
#                 The first arguments of the command to run, before any
#                 file arguments.
#     - FOO_POST : e.g. "--log-to /dev/null"
#                  The parameters to pass to the end of the command,
#                  after any file arguments.
#
#   The above example, if combined with file arguments a.txt and b.txt,
#   would be assembled into the following command:
#
#       $(FOO_DIR)/bin/foo -x -y -z a.txt b.txt --log-to /dev/null
#
#   Note that if there are multiple executables for the FOO software,
#   they can have arbitrary name prefixes.  For example:
#   FOO_START_SERVER_BIN, FOO_START_SERVER_PRE, FOO_START_SERVER_POST,
#   FOO_STOP_SERVER_BIN, FOO_STOP_SERVER_PRE, FOO_STOP_SERVER_POST.
#


# ==================================================================
# Ant is used by findbugs.
ANT_VERSION = 1.7.1
ANT_ZIPS = 
ANT_TAR_GZS = \
	$(THIRD_PARTY_DIR)/apache-ant-$(ANT_VERSION)-src.tar.gz \
	$(THIRD_PARTY_DIR)/apache-ant-$(ANT_VERSION)-bin.tar.gz
ANT_DIR = $(THIRD_PARTY_DIR)/apache-ant-$(ANT_VERSION)
ANT_LIB = \
    ant-antlr.jar \
    ant-apache-bcel.jar \
    ant-apache-bsf.jar \
    ant-apache-log4j.jar \
    ant-apache-oro.jar \
    ant-apache-regexp.jar \
    ant-apache-resolver.jar \
    ant-commons-logging.jar \
    ant-commons-net.jar \
    ant-jai.jar \
    ant.jar \
    ant-javamail.jar \
    ant-jdepend.jar \
    ant-jmf.jar \
    ant-jsch.jar \
    ant-junit.jar \
    ant-launcher.jar \
    ant-netrexx.jar \
    ant-nodeps.jar \
    ant-starteam.jar \
    ant-stylebook.jar \
    ant-swing.jar \
    ant-testutil.jar \
    ant-trax.jar \
    ant-weblogic.jar \
    xercesImpl.jar \
    lib/xml-apis.jar
ANT_DEPENDENCIES = \
	$(patsubst %,$(ANT_DIR)/%,$(ANT_LIB))
ANT_CLASSPATH = \
	$(subst  $(SPACE),$(COLON),$(ANT_DEPENDENCIES))


# ==================================================================
# Emma for static Java source code coverage analysis.
EMMA_VERSION = 2.0.5312
EMMA_ZIPS = \
	$(THIRD_PARTY_DIR)/emma-$(EMMA_VERSION)-src.zip \
	$(THIRD_PARTY_DIR)/emma-$(EMMA_VERSION).zip
EMMA_TAR_GZS = 
EMMA_DIR = $(THIRD_PARTY_DIR)/emma-$(EMMA_VERSION)
EMMA_LIB = \
	lib/emma.jar
EMMA_DEPENDENCIES = \
	$(patsubst %,$(EMMA_DIR)/%,$(EMMA_LIB))
EMMA_CLASSPATH = \
	$(subst  $(SPACE),$(COLON),$(EMMA_DEPENDENCIES))
EMMA_COVERAGE_METADATA = \
	$(BUILD_DIR)/test/coverage.em
EMMA_PROPERTIES = \
	-Dmetadata.out.file=$(EMMA_COVERAGE_METADATA)
EMMA_BIN = $(RUN_BIN) -cp $(EMMA_CLASSPATH) emma
EMMA_INSTRUMENT_BIN = $(EMMA_BIN)
EMMA_INSTRUMENT_PRE = instr -d $(BUILD_DIR)/.. $(EMMA_PROPERTIES) $(RUN_PRE)
EMMA_INSTRUMENT_POST = $(RUN_POST)
EMMA_REPORT_BIN = $(EMMA_BIN)
EMMA_REPORT_PRE = '!!!'
EMMA_REPORT_POST = '!!!'


# ==================================================================
# Findbugs for static Java source code analysis.
FINDBUGS_VERSION = 1.3.8
FINDBUGS_ZIPS = \
	$(THIRD_PARTY_DIR)/findbugs-$(FINDBUGS_VERSION)-source.zip
FINDBUGS_TAR_GZS = \
	$(THIRD_PARTY_DIR)/findbugs-$(FINDBUGS_VERSION).tar.gz
FINDBUGS_DIR = $(THIRD_PARTY_DIR)/findbugs-$(FINDBUGS_VERSION)
FINDBUGS_BIN = \
	export CLASSPATH=$(ANT_CLASSPATH):$$CLASSPATH; \
	$(FINDBUGS_DIR)/bin/findbugs
FINDBUGS_PRE = 
FINDBUGS_POST = -textui -medium


# ==================================================================
# Jlint for static Java source code analysis.
JLINT_VERSION = 3.1
JLINT_ZIPS = 
JLINT_TAR_GZS = \
	jlint-$(JLINT_VERSION).tar.gz
JLINT_DIR = $(THIRD_PARTY_DIR)/jlint-$(JLINT_VERSION)
JLINT_BIN = \
	$(JLINT_DIR)/jlint
JLINT_PRE = 
JLINT_POST = 


# ==================================================================
# JMock is used in conjunction with JUnit to create mock objects
# for Java unit testing.
#
# Typically when running unit tests we want to use both classpaths:
#
#     ... -cp $(JUNIT_CLASSPATH):$(JMOCK_CLASSPATH)
#
JMOCK_VERSION = 2.5.1
JMOCK_ZIPS = \
	jmock-$(JMOCK_VERSION)-jars.zip \
	jmock-$(JMOCK_VERSION)-javadoc.zip
JMOCK_TAR_GZS = 
JMOCK_DIR = $(THIRD_PARTY_DIR)/jmock-$(JMOCK_VERSION)
JMOCK_LIB = \
	bsh-core-2.0b4.jar \
	cglib-2.1_3-src.jar \
	cglib-nodep-2.1_3.jar \
	hamcrest-core-1.1.jar \
	hamcrest-library-1.1.jar \
	jmock-$(JMOCK_VERSION).jar \
	jmock-junit3-$(JMOCK_VERSION).jar \
	jmock-junit4-$(JMOCK_VERSION).jar \
	jmock-legacy-$(JMOCK_VERSION).jar \
	jmock-script-$(JMOCK_VERSION).jar \
	objenesis-1.0.jar
JMOCK_DEPENDENCIES = \
	$(patsubst %,$(JMOCK_DIR)/%,$(JMOCK_LIB))
JMOCK_CLASSPATH = \
	$(subst  $(SPACE),$(COLON),$(JMOCK_DEPENDENCIES))


# ==================================================================
# JUnit for running Java unit tests.
#
# Often used in conjunction with JMock.
#
# Typically when running unit tests we want to use both classpaths:
#
#     ... -cp $(JUNIT_CLASSPATH):$(JMOCK_CLASSPATH)
#
#
JUNIT_VERSION = 4.12
HAMCREST_VERSION = 1.3
JUNIT_ZIPS = \
	junit-$(JUNIT_VERSION).zip
JUNIT_TAR_GZS = 
JUNIT_DIR = $(THIRD_PARTY_DIR)/junit-$(JUNIT_VERSION)
JUNIT_LIB = \
	junit-$(JUNIT_VERSION).jar \
	hamcrest-core-$(HAMCREST_VERSION).jar
JUNIT_DEPENDENCIES = \
	$(patsubst %,$(JUNIT_DIR)/%,$(JUNIT_LIB))
JUNIT_CLASSPATH = \
	$(subst  $(SPACE),$(COLON),$(JUNIT_DEPENDENCIES))


# ==================================================================
# All Java third-party software.
THIRD_PARTY_JAVA_ZIPS = \
	$(ANT_ZIPS) \
	$(EMMA_ZIPS) \
	$(FINDBUGS_ZIPS) \
	$(JLINT_ZIPS) \
	$(JMOCK_ZIPS) \
	$(JUNIT_ZIPS)

THIRD_PARTY_JAVA_TAR_GZS = \
	$(ANT_TAR_GZS) \
	$(EMMA_TAR_GZS) \
	$(FINDBUGS_TAR_GZS) \
	$(JLINT_TAR_GZS) \
	$(JMOCK_TAR_GZS) \
	$(JUNIT_TAR_GZS)

THIRD_PARTY_JAVA_DIRS = \
	$(ANT_DIR) \
	$(EMMA_DIR) \
	$(FINDBUGS_DIR) \
	$(JLINT_DIR) \
	$(JMOCK_DIR) \
	$(JUNIT_DIR)


# ==================================================================
# All third-party software for all platforms.
THIRD_PARTY_ZIPS = \
	$(THIRD_PARTY_JAVA_ZIPS)

THIRD_PARTY_TAR_GZS = \
	$(THIRD_PARTY_JAVA_TAR_GZS)

THIRD_PARTY_DIRS = \
	$(THIRD_PARTY_JAVA_DIRS)
