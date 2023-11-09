package musaico.region;

import java.io.Serializable;


/**
 * <p>
 * !!!
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
public interface SizeExpression
    extends Serializable
{
    public abstract SizeExpression add (
                                        Size that
                                        );

    public abstract SizeExpression divide (
                                           double divisor
                                           );

    public abstract SizeExpression modulo (
                                           Size size
                                           );

    public abstract SizeExpression multiply (
                                             double factor
                                             );

    public abstract double ratio (
                                  Size that
                                  );

    public abstract Size size ();

    public abstract SizeExpression subtract (
                                             Size that
                                             );
}
