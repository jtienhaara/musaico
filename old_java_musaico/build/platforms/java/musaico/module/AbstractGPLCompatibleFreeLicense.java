package musaico.module;

import java.io.Serializable;


/**
 * <p>
 * A Free license which is compatible with the Gnu Public Licenses.
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
public abstract class AbstractGPLCompatibleFreeLicense
    implements License, Serializable
{
    private static final long serialVersionUID = 1L;


    // Every AbstractGPLCompatibleFreeLicense must implement body ().


    /**
     * @see musaico.module.License#compatibility(musaico.module.License)
     */
    @Override
    public final Compatibility compatibility (
                                              License that
                                              )
    {
        if ( that == null )
        {
            // Any AbstractGPLCompatibleFreeLicense
            // is not compatible with null.
            return Compatibility.NONE;
        }
        else if ( that instanceof AbstractGPLCompatibleFreeLicense )
        {
            // Any AbstractGPLCompatibleFreeLicense
            // is compatible with any other AbstractGPLCompatibleFreeLicense.
            return Compatibility.FULL;
        }
        else
        {
            // Any AbstractGPLCompatibleFreeLicense
            // is not compatible with any other type of License.
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
            // Any AbstractGPLCompatibleFreeLicense != null.
            return false;
        }
        else if ( this == object )
        {
            // Any AbstractGPLCompatibleFreeLicense == itself.
            return true;
        }
        else if ( this.getClass () != object.getClass () )
        {
            // Any AbstractGPLCompatibleFreeLicense
            // != a license of some other class.
            return false;
        }

        final AbstractGPLCompatibleFreeLicense that =
            (AbstractGPLCompatibleFreeLicense) object;
        final boolean is_license_equal =
            this.equalsLicense ( that );

        return is_license_equal;
    }


    /**
     * @return True if this AbstractGPLCompatibleFreeLicense matches
     *         that one, false if not.  Before arriving at this method,
     *         the caller must have already verified that this's and that's
     *         classes are identical.
     */
    protected boolean equalsLicense (
                                     AbstractGPLCompatibleFreeLicense that
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
