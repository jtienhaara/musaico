package musaico.module;

import java.io.Serializable;


/**
 * <p>
 * The compatibility between two Licenses, defining whether the binary
 * libraries and/or executables and/or the source code for two pieces
 * of software (or documentation and so on) distributed under two
 * different licenses may be combined and distributed, or may only
 * be combined for personal use, and so on.
 * </p>
 *
 * <p>
 * The bodies and compatibilities of licenses are taken from
 * <a href="https://gnu.org/licenses/">https://gnu.org/licenses/</a>.
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
public enum Compatibility
{
    /** These Licenses are fully compatible.  Products may be combined
     *  and distributed together under these two Licenses. */
    FULL ( true ),

    /** These Licenses are not at all compatible.  Products may only
     *  be combined for personal use. */
    NONE ( false );


    // Can two pieces of software with this Compatibility be distributed
    // together?
    private final boolean isDistributableTogether;


    /**
     * <p>
     * Creates a new Compatibility with the specified flags.
     * </p>
     */
    private Compatibility (
                           boolean is_distributable_together
                           )
    {
        this.isDistributableTogether = is_distributable_together;
    }


    /**
     * <p>
     * Returns the logical AND of this Compatibility with that Compatibility.
     * </p>
     *
     * <pre>
     *     Compatibility.FULL.and ( Compatibility.FULL )
     *         = Compatibility.FULL
     *
     *     Compatibility.FULL.and ( Compatibility.NONE )
     *         = Compatibility.NONE
     *
     *     Compatibility.NONE.and ( Compatibility.FULL )
     *         = Compatibility.NONE
     *
     *     Compatibility.NONE.and ( Compatibility.NONE )
     *         = Compatibility.NONE
     *
     *     ...And so on...
     * </pre>
     *
     * @param that The Compatibility with which to AND this Compatibility.
     *             Must not be null.
     *
     * @return The logically ANDed Compatibility.  Never null.
     */
    public Compatibility and (
                              Compatibility that
                              )
    {
        if ( that == null )
        {
            return Compatibility.NONE;
        }
        else if ( this.isDistributableTogether ()
                  && that.isDistributableTogether () )
        {
            return Compatibility.FULL;
        }
        else
        {
            return Compatibility.NONE;
        }
    }


    /**
     * @return True if two pieces products whose licenses have this
     *         Compatibility with one another can be combined and
     *         distributed together, false if they may only be combined
     *         for personal use.
     */
    public boolean isDistributableTogether ()
    {
        return this.isDistributableTogether;
    }


    /**
     * <p>
     * Returns the logical NOT of this Compatibility.
     * </p>
     *
     * <pre>
     *     Compatibility.FULL.not () = Compatibility.NONE
     *
     *     Compatibility.NONE.not () = Compatibility.FULL
     *
     *     ...And so on...
     * </pre>
     *
     * @return The logically NOTed Compatibility.  Never null.
     */
    public Compatibility not ()
    {
        if ( this.isDistributableTogether () )
        {
            return Compatibility.NONE;
        }
        else
        {
            return Compatibility.FULL;
        }
    }


    /**
     * <p>
     * Returns the logical OR of this Compatibility with that Compatibility.
     * </p>
     *
     * <pre>
     *     Compatibility.FULL.or ( Compatibility.FULL )
     *         = Compatibility.FULL
     *
     *     Compatibility.FULL.or ( Compatibility.NONE )
     *         = Compatibility.FULL
     *
     *     Compatibility.NONE.or ( Compatibility.FULL )
     *         = Compatibility.FULL
     *
     *     Compatibility.NONE.or ( Compatibility.NONE )
     *         = Compatibility.NONE
     *
     *     ...And so on...
     * </pre>
     *
     * @param that The Compatibility with which to OR this Compatibility.
     *             Must not be null.
     *
     * @return The logically ORed Compatibility.  Never null.
     */
    public Compatibility or (
                             Compatibility that
                             )
    {
        if ( that == null )
        {
            return Compatibility.NONE;
        }
        else if ( this.isDistributableTogether ()
                  || that.isDistributableTogether () )
        {
            return Compatibility.FULL;
        }
        else
        {
            return Compatibility.NONE;
        }
    }


    /**
     * <p>
     * Returns the logical XOR of this Compatibility with that Compatibility.
     * </p>
     *
     * <pre>
     *     Compatibility.FULL.xor ( Compatibility.FULL )
     *         = Compatibility.NONE
     *
     *     Compatibility.FULL.xor ( Compatibility.NONE )
     *         = Compatibility.FULL
     *
     *     Compatibility.NONE.xor ( Compatibility.FULL )
     *         = Compatibility.FULL
     *
     *     Compatibility.NONE.xor ( Compatibility.NONE )
     *         = Compatibility.NONE
     *
     *     ...And so on...
     * </pre>
     *
     * @param that The Compatibility with which to XOR this Compatibility.
     *             Must not be null.
     *
     * @return The logically XORed Compatibility.  Never null.
     */
    public final Compatibility xor (
                                    Compatibility that
                                    )
    {
        return this.or ( that ).and ( this.and ( that ).not () );
    }
}
