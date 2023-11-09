package musaico.module;

import java.io.Serializable;


/**
 * <p>
 * A Free license which is incompatible with the Gnu Public Licenses.
 * </p>
 *
 * <p>
 * The bodies and compatibilities of licenses are taken from
 * <a href="https://gnu.org/licenses/">https://gnu.org/licenses/</a>.
 * </p>
 *
 *
 * <p>
 * In Java every License must be Serializable in order to play nicely
 * over RMI.
 * </p>
 *
 * <p>
 * In Java every License must implement <code>equals()</code>,
 * <code>hashCode()</code> and <code>toString()</code>.
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
public abstract class AbstractNonGPLCompatibleFreeLicense
    implements License, Serializable
{
    private static final long serialVersionUID = 1L;


    // Every AbstractNonGPLCompatibleFreeLicense must implement body ().


    /**
     * @see musaico.module.License#compatibility(musaico.module.License)
     */
    @Override
    public Compatibility compatibility (
                                        License that
                                        )
    {
        if ( that == null )
        {
            // Any AbstractNonGPLCompatibleFreeLicense
            // is not compatible with null.
            return Compatibility.NONE;
        }
        else if ( ! ( that instanceof AbstractNonGPLCompatibleFreeLicense ) )
        {
            // Any AbstractNonGPLCompatibleFreeLicense
            // is not compatible with any
            // non-AbstractNonGPLCompatibleFreeLicense.
            return Compatibility.NONE;
        }

        final AbstractNonGPLCompatibleFreeLicense non_gpl_compatible =
            (AbstractNonGPLCompatibleFreeLicense) that;
        return this.compatibilityNonGPLCompatibleFree ( non_gpl_compatible );
    }


    /**
     * <p>
     * Returns the compatibility of this AbstractNonGPLCompatibleFreeLicense
     * with that AbstractNonGPLCompatibleFreeLicense.
     * </p>
     *
     * <p>
     * Only exactly equal Licenses are compatible by default.
     * Can be overridden.
     * </p>
     *
     * @param that The AbstractNonGPLCompatibleFreeLicense with which this
     *             AbstractNonGPLCompatibleFreeLicense may or may not be
     *             compatible.  Must not be null.
     *
     * @return The Compatibility of this license with that license.
     *         Never null.
     */
    protected Compatibility compatibilityNonGPLCompatibleFree (
                                                               AbstractNonGPLCompatibleFreeLicense that
                                                               )
    {
        if ( this.equals ( that ) )
        {
            return Compatibility.FULL;
        }
        else
        {
            return Compatibility.NONE;
        }
    }


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
            // Any AbstractNonGPLCompatibleFreeLicense != null.
            return false;
        }
        else if ( this == object )
        {
            // Any AbstractNonGPLCompatibleFreeLicense == itself.
            return true;
        }
        else if ( this.getClass () != object.getClass () )
        {
            // Any AbstractNonGPLCompatibleFreeLicense
            // != a license of some other class.
            return false;
        }

        final AbstractNonGPLCompatibleFreeLicense that =
            (AbstractNonGPLCompatibleFreeLicense) object;
        final boolean is_license_equal =
            this.equalsLicense ( that );

        return is_license_equal;
    }


    /**
     * @return True if this AbstractNonGPLCompatibleFreeLicense matches
     *         that one, false if not.  Before arriving at this method,
     *         the caller must have already verified that this's and that's
     *         classes are identical.
     */
    protected boolean equalsLicense (
                                     AbstractNonGPLCompatibleFreeLicense that
                                     )
    {
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.module.License#name()
     *
     * Defaults to the class's simple name.  Can be overridden.
     */
    @Override
    public String name ()
    {
        return this.getClass ().getSimpleName ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.name ();
    }
}
