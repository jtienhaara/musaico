package musaico.types.environments;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.Instance;
import musaico.types.InstanceBuilder;
import musaico.types.Tag;
import musaico.types.Type;
import musaico.types.TypeException;
import musaico.types.TypingEnvironment;


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
public class SimpleInstanceBuilder<STORAGE_VALUE extends Object>
    implements InstanceBuilder, Serializable
{
    /** Synchronize critical sections on this token: */
    private final Serializable lock = new String ();

    /** The typing environment which provides all types and
     *  type checking for the instance-to-be. */
    private final TypingEnvironment environment;

    /** The type of instance to be built, such as a text type
     *  or a number type and so on. */
    private final Type<STORAGE_VALUE> type;

    /** The raw value of the instance to be built, such as
     *  "Hello, world" for a text instance, or 42 for a number instance. */
    /* !!! CURRENTLY THIS BUILDER WILL NOT BE SERIALIZABLE IF THE RAW
     * !!! VALUE ISN'T!!! */
    private final STORAGE_VALUE rawValue;

    /** Tags for the instance being built, such as value constraints. */
    private final List<Tag> tagsList = new ArrayList<Tag> ();


    /**
     * <p>
     * Creates a new SimpleInstanceBuilder with the specified
     * type, typing environment, and raw value.
     * </p>
     *
     * @param environment The typing environment in which
     *                    to build an instance.  Contains
     *                    all types and tags used to
     *                    validate the raw value of the
     *                    instance.  Must not be null.
     *
     * @param type The type of instance to build, such as text type
     *             or number type and so on.  Must not be null.
     *
     * @param raw_value The raw value of the instance to build, such
     *                  as "Hello, world" for a text instance,
     *                  or 42 for a number instance, and so on.
     *                  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleInstanceBuilder (
                                  TypingEnvironment environment,
                                  Type<STORAGE_VALUE> type,
                                  STORAGE_VALUE raw_value
                                  )
        throws I18nIllegalArgumentException
    {
        if ( environment == null
             || type == null
             || raw_value == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleInstanceBuilder with environment [%environment%] type [%type%] raw value [%raw_value%]",
                                                     "environment", environment,
                                                     "type", type,
                                                     "raw_value", raw_value );
        }

        this.environment = environment;
        this.type = type;
        this.rawValue = raw_value;
    }


    /**
     * @see musaico.types.InstanceBuilder#add(musaico.types.Tag)
     */
    public InstanceBuilder add (
                                Tag tag
                                )
        throws I18nIllegalArgumentException
    {
        if ( tag == null )
        {
            throw new I18nIllegalArgumentException ( "Instance builder [%instance_builder%] cannot add tag [%tag%]",
                                                     "instance_builder", this,
                                                     "tag", tag );
        }

        synchronized ( this.lock )
        {
            this.tagsList.add ( tag );
        }

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
        boolean is_parameters_ok = true;
        if ( tags == null )
        {
            is_parameters_ok = false;
        }
        else
        {
            for ( Tag tag : tags )
            {
                if ( tag == null )
                {
                    is_parameters_ok = false;
                    break;
                }
            }
        }

        if ( ! is_parameters_ok )
        {
            throw new I18nIllegalArgumentException ( "Instance builder [%instance_builder%] cannot add tag [%tag%]",
                                                     "instance_builder", this,
                                                     "tags", tags );
        }

        synchronized ( this.lock )
        {
            for ( Tag tag : tags )
            {
                this.tagsList.add ( tag );
            }
        }

        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#build
     */
    public Instance build ()
        throws TypeException
    {
        Tag [] tags = this.tags ();
        return new SimpleInstance (
                                   this.environment,
                                   this.type,
                                   this.rawValue,
                                   tags
                                   );
    }


    /**
     * @see musaico.types.InstanceBuilder#copy(musaico.types.Instance)
     */
    public InstanceBuilder copy (
                                 Instance that
                                 )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            this.addTags ( that.tags () );
        }

        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#environment()
     */
    public TypingEnvironment environment ()
    {
        return this.environment;
    }
 

    /**
     * @see musaico.types.InstanceBuilder#remove(musaico.types.Tag)
     */
    public InstanceBuilder remove (
                                   Tag tag
                                   )
        throws I18nIllegalArgumentException
    {
        if ( tag == null )
        {
            throw new I18nIllegalArgumentException ( "Instance builder [%instance_builder%] cannot remove tag [%tag%]",
                                                     "instance_builder", this,
                                                     "tag", tag );
        }

        synchronized ( this.lock )
        {
            this.tagsList.remove ( tag );
        }

        return this;
    }
 

    /**
     * @see musaico.types.InstanceBuilder#removeTags(musaico.types.Tag[])
     */
    public InstanceBuilder removeTags (
                                       Tag... tags
                                       )
        throws I18nIllegalArgumentException
    {
        boolean is_parameters_ok = true;
        if ( tags == null )
        {
            is_parameters_ok = false;
        }
        else
        {
            for ( Tag tag : tags )
            {
                if ( tag == null )
                {
                    is_parameters_ok = false;
                    break;
                }
            }
        }

        if ( ! is_parameters_ok )
        {
            throw new I18nIllegalArgumentException ( "Instance builder [%instance_builder%] cannot remove tags [%tags%]",
                                                     "instance_builder", this,
                                                     "tags", tags );
        }

        synchronized ( this.lock )
        {
            for ( Tag tag : tags )
            {
                this.tagsList.remove ( tag );
            }
        }

        return this;
    }


    /**
     * @see musaico.types.InstanceBuilder#tags()
     */
    public Tag [] tags ()
    {
        final Tag [] tags;
        synchronized ( this.lock )
        {
            Tag [] template = new Tag [ this.tagsList.size () ];
            tags = this.tagsList.toArray ( template );
        }

        return tags;
    }


    /**
     * @see musaico.types.InstanceBuilder#type()
     */
    public Type<STORAGE_VALUE> type ()
    {
        return this.type;
    }


    /**
     * @see musaico.types.InstanceBuilder#validate()
     */
    public void validate ()
        throws TypeException
    {
        // Dumb approach for now, if constructing an instance
        // turns out to be expensive then we'll need to re-think
        // the dumb approach.
        try
        {
            Instance instance = this.build ();
            instance.validate ();
        }
        catch ( I18nIllegalArgumentException e )
        {
            throw new TypeException ( "Instance is invalid",
                                      "cause", e );
        }
    }


    /**
     * @see musaico.types.InstanceBuilder#value()
     */
    public STORAGE_VALUE value ()
    {
        return this.rawValue;
    }
}
