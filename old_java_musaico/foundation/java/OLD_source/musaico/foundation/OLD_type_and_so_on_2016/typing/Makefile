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

PACKAGE = musaico/foundation/typing


PACKAGE_DEPENDENCIES = \
	musaico/foundation/value


COMPILABLES = \
	MODULE.class \
	\
	AbstractRegistration.class \
	Registered.class \
	Unregistered.class \
	Registration.class \
	CastAttempt.class \
	CastValue.class \
	OperationValue.class \
	Types.class \
	ValueBeforeAndAfter.class \
	AbstractTypingContract.class \
	CastSourceMustBeValid.class \
	CastTargetMustBeValid.class \
	OperationInputMustBeValid.class \
	OperationOutputMustBeValid.class \
	OperationMustNotAlwaysFail.class \
	OperationTypesMustMatch.class \
	SymbolMustBeRegistered.class \
	SymbolMustNotBeRegistered.class \
	TypeMustBeValid.class \
	ValueMustBeInDomain.class \
	ValueMustBeInstanceOfClass.class \
	\
	Visibility.class \
	SymbolID.class \
	Symbol.class \
	Retypable.class \
	\
	SymbolMustBeUnique.class \
	SymbolMustBeInTable.class \
	SymbolTable.class \
	\
	Constraint.class \
	Namespace.class \
	Operation.class \
	Tag.class \
	Term.class \
	Type.class \
	\
	Kind.class \
	RootNamespace.class \
	ToplevelNamespace.class \
	TagWithTypeConstraint.class \
	\
	AbstractTermID.class \
	\
	BlockingConstantTermID.class \
	ConstantTermID.class \
	ConstraintID.class \
	ExpressionTermID.class \
	NamespaceID.class \
	OperationID.class \
	TagID.class \
	TypeID.class \
	UnknownTermID.class \
	VariableTermID.class \
	\
	StandardType.class \
	\
	SymbolType.class \
	\
	AbstractSymbolType.class \
	NoSymbolType.class \
	AbstractTermType.class \
	\
	BlockingConstantTermType.class \
	ConstantTermType.class \
	ConstraintType.class \
	ExpressionTermType.class \
	NamespaceType.class \
	OperationType.class \
	OperationType1.class \
	OperationType2.class \
	OperationType3.class \
	OperationType4.class \
	OperationType5.class \
	OperationType6.class \
	TagType.class \
	TypeKind.class \
	UnknownTermType.class \
	VariableTermType.class \
	\
	OperationInputs1.class \
	OperationInputs2.class \
	OperationInputs3.class \
	OperationInputs4.class \
	OperationInputs5.class \
	OperationInputs6.class \
	\
	Operation1.class \
	Operation2.class \
	Operation3.class \
	Operation4.class \
	Operation5.class \
	Operation6.class \
	\
	CurryableOperation2.class \
	CurryableOperation3.class \
	CurryableOperation4.class \
	CurryableOperation5.class \
	CurryableOperation6.class \
	\
	Mutation.class \
	SubTypeWorkBench.class \
	\
	AbstractSymbol.class \
	\
	OperationBody1.class \
	OperationBody2.class \
	OperationBody3.class \
	OperationBody4.class \
	OperationBody5.class \
	OperationBody6.class \
	OperationBodyVarArgs.class \
	\
	AbstractConstraint.class \
	AbstractNamespace.class \
	AbstractTag.class \
	AbstractTagWithTypeConstraint.class \
	AbstractTerm.class \
	\
	NoCast.class \
	NoConstraint.class \
	NoNamespace.class \
	AlwaysFail.class \
	NoMutate.class \
	NoTag.class \
	Unknown.class \
	NoType.class \
	UnknownType.class \
	\
	TypeIDs.class \
	\
	ConstraintChecker.class \
	\
	OperationAlphabeticalComparator.class \
	TagAlphabeticalComparator.class \
	TypeAlphabeticalComparator.class \
	\
	StandardNamespace.class \
	StandardRootNamespace.class \
	\
	StandardOperation1.class \
	StandardOperation2.class \
	StandardOperation3.class \
	StandardOperation4.class \
	StandardOperation5.class \
	StandardOperation6.class \
	StandardOperationVarArgs.class \
	\
	CompositeOperation.class \
	\
	FilterOp.class \
	Identity.class \
	Pipe.class \
	Read.class \
	ReadBlocking.class \
	Write.class \
	\
	SubTypeRename.class \
	SubTypeSubstituteParentType.class \
	SubTypeTagsMutate.class \
	SubTypeUpdateNamespace.class \
	\
	StandardTag.class \
	\
	TypedValueBuilder.class \
	NoTypedValueBuilder.class \
	TypeBuilder.class \
	\
	MetadataType.class \
	\
	Processor.class \
	\
	ToplevelProcessor.class \
	\
	MetaBlockingTimeLimit.class \
	\
	TypesMustHaveSameValueClasses.class \
	TermMustNotBeUnknown.class \
	TypeMustNotBeUnknown.class \
	\
	BlockingConstant.class \
	Constant.class \
	Expression.class \
	Variable.class \
	\
	NamespaceVisitor.class \
	NamespaceWalker.class

RESOURCES = \
	Messages.properties

SUB_PACKAGES = \
	aspect \
	requirement \
	sideeffect \
	typeclass

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/rules.mk
