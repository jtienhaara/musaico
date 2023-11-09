package musaico.region;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Order;


/**
 * <p>
 * Manipulates positions and creates sizes and
 * regions from them.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class RegionalPositionExpression
    implements PositionExpression, Serializable
{
    /** The position from which the expression started. */
    private final Position position;

    /** The region in which to perform the operations.
     *  Can be a Space.all () region (everything). */
    private final Region region;


    /**
     * <p>
     * Creates a new PositionExpression, taking the
     * specified term as the start of the expression.
     * </p>
     *
     * @param position The position from which to start
     *                 the expression.  Must not be null.
     *
     * @param region The region over which to perform
     *               calculations for this expression,
     *               such as a Space.all () region,
     *               or some smaller Region.
     *               Must not be null.  Must have the
     *               same Space as the specified Position.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified are invalid
     *                                      (see above).
     */
    public RegionalPositionExpression (
                                       Position position,
                                       Region region
                                       )
        throws I18nIllegalArgumentException
    {
        if ( position == null
             || region == null
             || ! position.space ().equals ( region.space () ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create an expression out of position [%position%] in space [%position_space%] relative to region [%region%] in space [%region_space%]",
                                                     "position", position,
                                                     "position_space", ( position == null ? "?" : position.space () ),
                                                     "region", region,
                                                     "region_space", ( region == null ? "?" : region.space () ) );
        }

        this.position = position;
        this.region = region;
    }


    /**
     * @see musaico.region.PositionExpression#add(musaico.region.Size)
     */
    @Override
    public PositionExpression add (
                                   Size size
                                   )
    {
        if ( ! this.position.space ().equals ( size.space () ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().outOfBounds () );
        }

        PositionExpression added =
            this.position.space ().expr ( position ).add ( size );
        if ( this.region.contains ( added.position () ) )
        {
            return added;
        }
        else
        {
            return this.position.space ()
                .expr ( this.position.space ().outOfBounds () );
        }
    }


    /**
     * @see musaico.region.PositionExpression#modulo(musaico.region.Size)
     */
    @Override
    public SizeExpression modulo (
                                  Size size
                                  )
    {
        if ( ! this.position.space ().equals ( size.space () ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().none () );
        }

        // Just use the regular (non-regional) modulo operation.
        return this.position.space ().expr ( position ).modulo ( size );
    }


    /**
     * @see musaico.region.PositionExpression#next()
     */
    @Override
    public Position next ()
    {
        Position stepped_position =
            this.position.space ().expr ( this.position ).next ();
        if ( this.region.contains ( stepped_position ) )
        {
            return stepped_position;
        }
        else
        {
            return this.position.space ().outOfBounds ();
        }
    }


    /**
     * @see musaico.region.PositionExpression#position()
     */
    @Override
    public Position position ()
    {
        return this.position;
    }


    /**
     * @see musaico.region.PositionExpression#previous()
     */
    @Override
    public Position previous ()
    {
        Position stepped_position =
            this.position.space ().expr ( this.position ).previous ();
        if ( this.region.contains ( stepped_position ) )
        {
            return stepped_position;
        }
        else
        {
            return this.position.space ().outOfBounds ();
        }
    }


    /**
     * @see musaico.region.PositionExpression#subtract(musaico.region.Position)
     */
    @Override
    public SizeExpression subtract (
                                    Position that
                                    )
    {
        if ( ! this.position.space ().equals ( that.space () ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().none () );
        }
        else if ( ! this.region.contains ( that )
                  || ! this.region.contains ( this.position ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().none () );
        }

        // Just use the regular (non-regional) subtract operation.
        return this.position.space ().expr ( this.position ).subtract ( that );
    }


    /**
     * @see musaico.region.PositionExpression#subtract(musaico.region.Size)
     */
    @Override
    public PositionExpression subtract (
                                        Size size
                                        )
    {
        if ( ! this.position.space ().equals ( size.space () ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().outOfBounds () );
        }

        PositionExpression subtracted =
            this.position.space ().expr ( this.position ).subtract ( size );
        if ( this.region.contains ( subtracted.position () ) )
        {
            return subtracted;
        }
        else
        {
            return this.position.space ()
                .expr ( this.position.space ().outOfBounds () );
        }
    }

    /**
     * @see musaico.region.PositionExpression#to(musaico.region.Position)
     */
    @Override
    public RegionExpression to (
                                Position that
                                )
    {
        if ( ! this.position.space ().equals ( that.space () ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().empty () );
        }
        else if ( ! this.region.contains ( that ) )
        {
            return this.position.space ()
                .expr ( this.position.space ().empty () );
        }
        else if ( ! this.region.contains ( this.position ) )
        {
            // There is no guarantee at constructor time that the
            // position is actually inside the desired Region.
            // However in order to create a sub-region, it must
            // be.
            return this.position.space ()
                .expr ( this.position.space ().empty () );
        }

        // Just rely on the regular (non-regional) region operation.
        return this.position.space ().expr ( this.position ).to ( that );
    }
}
