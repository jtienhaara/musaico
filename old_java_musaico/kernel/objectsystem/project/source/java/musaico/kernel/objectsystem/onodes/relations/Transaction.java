package musaico.kernel.objectsystem.onodes.relations;


import java.io.Serializable;


import musaico.kernel.objectsystem.onodes.Relation;
import musaico.kernel.objectsystem.onodes.RelationTypeIdentifier;


/**
 * <p>
 * Represents a transaction on one or more Driver ONodes.
 * </p>
 *
 * <p>
 * Each child is a driver ONode, to which requests can be sent.
 * The requests are made as part of this transaction.  If this
 * transaction is later rolled back, then each driver will
 * execute its transaction rollback code.  If it is committed,
 * then each driver will prepare and then commit.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ONode
 * must be Serializable in order to play nicely over RMI.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <pre>
 * Copyright (c) 2009, 2011, 2012 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public interface Transaction
    extends Relation, Serializable
{
    /** The type identifier representing all transaction ONodes. */
    public static final RelationTypeIdentifier TYPE_ID =
        new RelationTypeIdentifier ( "transaction" );
}
