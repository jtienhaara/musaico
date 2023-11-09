package musaico.module;

import java.io.Serializable;


/**
 * <p>
 * Module settings for a Musaico module (library).
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
public interface MusaicoModule
    extends Serializable
{
    /**
     * @return Human-readable information about where this module may be
     *         downloaded and/or purchased, bug tracked, and so on,
     *         such as a world wide web HTTP(S) address, or a mailing
     *         address, and so on.  Never null.
     */
    public abstract String address ();


    /**
     * @return Human-readable contact information about the person(s)
     *         to contact for information, bug reports, contributions,
     *         payments, and so on.  For example, the developer(s)
     *         and/or maintainers of this module's source code,
     *         and/or the maintainers and/or project manager(s) responsible
     *         for roadmapping, version control, binary distributions,
     *         testing, and/or the entities responsible for selling this
     *         module, and so on.  Can take the form of email address(es)
     *         or mailing address(es) and so on.  Never null.
     */
    public abstract String contact ();


    /**
     * @return Human-readable copyright information for this module.
     *         Never null.
     */
    public abstract String copyright ();


    /**
     * @return Human-readable description of the purpose of this module.
     *         Never null.
     */
    public abstract String description ();


    /**
     * @return A secure, unique hash of the particular version of this
     *         module, such as a SHA-1 hash.  If two modules have equal
     *         hashes then they are almost guaranteed to be the same
     *         version of the same module.  Never null.
     */
    public abstract String hash ();


    /**
     * @return The license under which binaries and/or source code of this
     *         module may be distributed.  Never null.
     */
    public abstract License license ();


    /**
     * @return The human-readable name of this module, such as
     *         "musaico/foundation/foobar".  Never null.
     */
    public abstract String name ();


    /**
     * @return The version of this module.  Also the serialVersionUID
     *         for all Serializable classes in this module.
     *         Formatted as the Gregorian calendar date on which
     *         a developer edited any class in this module.
     *         YYYYMMDD format.
     */
    public abstract long version ();
}
