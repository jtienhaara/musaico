package musaico.kernel.driver;


import musaico.io.Path;
import musaico.io.Reference;

import musaico.buffer.Buffer;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.objectsystem.Cursor;


/**
 * <p>
 * A Driver with transaction capabilities, allowing atomic
 * reads and writes with two-phase commits and rollback capabilities.
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
 * Copyright (c) 2009, 2010 Johann Tienhaara
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
public interface TransactionalDriver
    extends Driver
{
    /** A Cursor on a TransactionalDriver has started a
     *  transaction. */
    public static final Reference STARTED =
        new Path ( "/state/driver/transaction/started" );

    /** A Cursor on a TransactionalDriver has prepared a
     *  transaction. */
    public static final Reference PREPARED =
        new Path ( "/state/driver/transaction/prepared" );

    /** A Cursor on a TransactionalDriver has finished (committed) a
     *  transaction. */
    public static final Reference COMMITTED =
        new Path ( "/state/driver/transaction/committed" );

    /** A Cursor on a TransactionalDriver has aborted (rolled back) a
     *  transaction. */
    public static final Reference ROLLED_BACK =
        new Path ( "/state/driver/transaction/rolled_back" );


    /**
     * <p>
     * Commits the transaction identified by the specified id.
     * </p>
     *
     * @param cursor The Cursor which owns the transaction to commit.
     *               The top transaction on the stack for this
     *               cursor will be committed.  Must not be null.
     *               Must be the cursor which started a transaction
     *               with this driver by a call to
     *               <code> transactionStart () </code>.
     *               The transaction must be in the "prepared"
     *               state after a call to
     *               <code> transactionPrepareCommit () </code>.
     *
     * @throws TransactionException If the specified cursor has no
     *                              open transactions on this driver.
     */
    public abstract void transactionCommit (
                                            Cursor cursor
                                            )
        throws TransactionException;


    /**
     * <p>
     * Prepares the specified transaction for committing.
     * </p>
     *
     * @param cursor The Cursor which owns the transaction to commit.
     *               The top transaction on the stack for this
     *               cursor will be committed.  Must not be null.
     *               Must be the cursor which started a transaction
     *               with this driver by a call to
     *               <code> transactionStart () </code>.
     *
     * @throws TransactionException if the specified transaction does not
     *                              exist for this driver,
     *                              or if it has already been prepared.
     */
    public abstract void transactionPrepareCommit (
                                                   Cursor cursor
                                                   )
        throws TransactionException;


    /**
     * <p>
     * Rolls back the specified transaction.
     * </p>
     *
     * @param cursor The Cursor which owns the transaction to commit.
     *               The top transaction on the stack for this
     *               cursor will be committed.  Must not be null.
     *               Must be the cursor which started a transaction
     *               with this driver by a call to
     *               <code> transactionStart () </code>.
     *
     * @throws TransactionException if the specified transaction is
     *                              not open for this driver.
     */
    public abstract void transactionRollback (
                                              Cursor cursor
                                              )
        throws TransactionException;


    /**
     * <p>
     * Starts a transaction for this driver.
     * </p>
     *
     * <p>
     * The transaction may be configured via the transaction_configuration
     * Buffer parameter.  For example, a driver which supports transaction
     * retries might allow a "retries" parameter with value of 1, 2, 3, ...
     * </p>
     *
     * <p>
     * If no transaction_configuration Buffer is provided, then the
     * driver must use its own default configuration to create the
     * new transaction.
     * </p>
     *
     * <p>
     * If any field(s) is/are left out of the transaction_configuration
     * Buffer then the driver must also choose defaults for those fields.
     * </p>
     *
     * <p>
     * If the driver does not recognize a given configuration field id,
     * then it should log a message but must otherwise ignore the field.
     * </p>
     *
     * <p>
     * If any configuration field contains an invalid value, then an
     * exception must be thrown by the driver.
     * </p>
     *
     * @param cursor The cursor which owns the transaction.
     *               Any subsequent reads and writes using the Cursor
     *               will be executed in the context of a transaction,
     *               either until the transaction is prepared or rolled
     *               back or until another transaction is started ("pushed"
     *               onto the transaction stack).  If a transaction higher
     *               on the stack for a cursor with multiple transactions
     *               is rolled back, then the lower transactions on the
     *               stack will be rolled back as well (unless their
     *               transaction configurations explicitly override this
     *               property).  Must not be null.
     *
     * @param transaction_configuration The configuration settings
     *                                  for the new transaction.
     *                                  May be null to use default settings.
     *
     * @throws TransactionException If the transaction configuration Buffer
     *                              contains a field with an invalid value,
     *                              or if for some reason beyond the power
     *                              of this driver the transaction
     *                              cannot be started (for example, low-level
     *                              I/O failures such as network outages).
     */
    public abstract void transactionStart (
                                           Cursor cursor,
                                           Buffer transaction_configuration
                                           )
        throws TransactionException;
}
