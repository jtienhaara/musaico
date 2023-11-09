package musaico.module;

import java.io.Serializable;


/**
 * <p>
 * Module settings for a Musaico module (library).  Provides boilerplate
 * code.
 * </p>
 *
 *
 * <p>
 * In Java every MusaicoModule must be Serializable in order
 * to play nicely over RMI.
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
 * Copyright (c) 2015 Johann Tienhaara
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *     (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer. 
 * 
 *     (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.  
 *     
 *     (3)The name of the author may not be used to
 *     endorse or promote products derived from this software without
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * </pre>
 */
public abstract class AbstractMusaicoModule
    implements MusaicoModule, Serializable
{
    private static final long serialVersionUID = 1L;


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            // Any AbstractMusaicoModule != null.
            return false;
        }
        else if ( object == this )
        {
            // Any AbstractMusaicoModule = itself.
            return true;
        }
        else if ( ! ( object instanceof AbstractMusaicoModule ) )
        {
            // Any AbstractMusaicoModule != any other class of object.
            return false;
        }

        final AbstractMusaicoModule that = (AbstractMusaicoModule) object;
        if ( ! this.name ().equals ( that.name () )
             || ! this.hash ().equals ( that.hash () )
             || this.version () != that.version ()
             || ! this.copyright ().equals ( that.copyright () )
             || ! this. license ().equals ( that.license () ) )
        {
            return false;
        }

        // As long as the important fields match, it doesn't matter
        // what description, developers, maintainers, etc are set to.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hash ().hashCode ();
    }


    /**
     * @see musaico.module.MusaicoModule#name()
     */
    @Override
    public final String name ()
    {
        return this.getClass ()
            .getPackage ()
            .getName ()
            .replaceAll ( "\\.", "/" );
    }


    /**
     * @see java.lang.Object.toString()
     */
    @Override
    public final String toString ()
    {
        return this.name ()
            + " [ version "
            + this.version ()
            + " "
            + this.copyright ()
            + " ] : "
            + this.hash ();
    }
}
