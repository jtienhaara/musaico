package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Creates SimplesInstances of Types, each with a specific value and
 * zero or more Tags to manage constraints and so on.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class NoInstanceBuilder
    implements InstanceBuilder, Serializable
{
    /**
     * <p>
     * Creates a new NoInstanceBuilder.
     * </p>
     *
     * <p>
     * Package private.
     * Use <code> InstanceBuilder.NONE </code> instead.
     * </p>
     */
    NoInstanceBuilder ()
    {
    }


    /**
     * @see musaico.types.InstanceBuilder#add(musaico.types.Tag)
     */
    public InstanceBuilder add (
                                Tag tag
                                )
        throws I18nIllegalArgumentException
    {
        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#addTags(musaico.types.Tag[])
     */
    public InstanceBuilder addTags (
                                    Tag... tags
                                    )
        throws I18nIllegalArgumentException
    {
        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#build
     */
    public Instance build ()
        throws TypeException
    {
        throw new TypeException ( "NoInstanceBuilder cannot build instances" );
    }


    /**
     * @see musaico.types.InstanceBuilder#copy(musaico.types.Instance)
     */
    public InstanceBuilder copy (
                                 Instance that
                                 )
        throws I18nIllegalArgumentException
    {
        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#environment()
     */
    public TypingEnvironment environment ()
    {
        return TypingEnvironment.NONE;
    }
 

    /**
     * @see musaico.types.InstanceBuilder#remove(musaico.types.Tag)
     */
    public InstanceBuilder remove (
                                   Tag tag
                                   )
        throws I18nIllegalArgumentException
    {
        return this;
    }
 

    /**
     * @see musaico.types.InstanceBuilder#removeTags(musaico.types.Tag[])
     */
    public InstanceBuilder removeTags (
                                       Tag... tag
                                       )
        throws I18nIllegalArgumentException
    {
        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#tags()
     */
    public Tag [] tags ()
    {
        return new Tag [ 0 ];
    }


    /**
     * @see musaico.types.InstanceBuilder#type()
     */
    public Type<Object> type ()
    {
        return Type.NONE;
    }


    /**
     * @see musaico.types.InstanceBuilder#validate()
     */
    public void validate ()
        throws TypeException
    {
        throw new TypeException ( "NoInstanceBuilder cannot build instances" );
    }


    /**
     * @see musaico.types.InstanceBuilder#value()
     */
    public Object value ()
    {
        return Type.NONE.none ();
    }
}
