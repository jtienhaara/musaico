package musaico.module;

import java.io.Serializable;


/**
 * <p>
 * A distribution license, defining the terms by which you may
 * distribute binary libraries and/or executables and/or source code
 * for a module.
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
public interface License
    extends Serializable
{
    /** Modified BSD license. */
    public static final BSDLicense BSD = new BSDLicense ();

    /** GPL3 license. */
    public static final GPL3License GPL3 = new GPL3License ();


    /**
     * @return The full text of this license.  Never null.
     */
    public abstract String body ();


    /**
     * <p>
     * Determines whether this license and the specified license are
     * compatible such that the binaries and / or source can be combined
     * and distributed together; or if they are incompatible and
     * software distributed under these two licenses may only be
     * combined for personal use; and so on.
     * </p>
     *
     * @return The (in)compatibility of this License and that License.
     *         Never null.
     */
    public abstract Compatibility compatibility (
                                                 License that
                                                 );


    /**
     * @return This License's name, such as "GPL3" or "BSD" and so on.
     *         Can include product-specific information, such as
     *         an instance of class ProprietarytXYZLicense which return the
     *         name "ProprietaryXYZLicense assigned to Johann Tienhaara".
     *         Never null.
     */
    public abstract String name ();
}
