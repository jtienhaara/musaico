package musaico.foundation.wiring;

import java.io.Serializable;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter5;


/**
 * <p>
 * The context or state of a push () or pull () request to a
 * Conductor.
 * </p>
 *
 * <p>
 * Each Pulse carries information that might be required by
 * a Conductor to handle a push () / pull () request:
 * </p>
 *
 * <ul>
 *   <li> The Board in which the Conductor receiving the
 *        Pulse resides. </li>
 *   <li> The source of the Pulse, including the Wire on
 *        which the target Conductor received the Pulse
 *        (if any), and the Conductor that sent the Pulse
 *        to that Wire (if any). </li>
 *   <li> Any extra information required for the Conductor
 *        to handle the Pulse, such as Tag metadata. </li>
 * </ul>
 *
 * <p>
 * When a Conductor handles the push () / pull () request
 * by making further push () / pull () requests to other
 * Wires or Conductors, it uses Pulse.next ( ... ) to create
 * a new Pulse from the source Pulse for the new requests.
 * This ensures all metadata (such as Tags) are forwarded
 * with each new request.
 * </p>
 *
 * <p>
 * Depending on its needs, a Conductor can create one Pulse
 * for each and every push () / pull () request it sends
 * out; or it can create one Pulse for all of the push ()
 * and pull () requests it sends out; or anywhere in between.
 * The key is to ensure that a new Pulse is created for
 * each source Conductor.
 * </p>
 *
 *
 * <p>
 * In Java, every Pulse must be Serializable in order to play
 * nicely over RMI.
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public class Pulse
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /** No Pulse at all. */
    public static final Pulse NONE = new Pulse ();


    /**
     * <p>
     * The direction of a Pulse: PUSH to send data, PULL to retrieve data.
     * </p>
     */
    public static enum Direction
    {
        PULL,
        PUSH;
    }


    private static final Advocate classAdvocate =
        new Advocate ( Pulse.class );


    private final Pulse.Direction direction;
    private final Board board;
    private final Conductor sourceConductor;
    private final Wire sourceWire;
    private final Tags tags;

    private final int version;
    private final int hashCode;

    private Pulse ()
    {
        // No pulse.
        this.direction = Pulse.Direction.PUSH;
        this.board = Board.NONE;
        this.sourceConductor = Conductor.NONE;
        this.sourceWire = Wire.NONE;
        this.tags = Tags.NONE;
        this.version = 0;
        this.hashCode = 0;
    }

    public Pulse (
            Pulse.Direction direction,
            Board board,
            Conductor source_conductor,
            Wire source_wire,
            Tags tags
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        this ( direction,
               board,
               source_conductor,
               source_wire,
               tags,
               0 ); // version
    }

    protected Pulse (
            Pulse.Direction direction,
            Board board,
            Conductor source_conductor,
            Wire source_wire,
            Tags tags,
            int version
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter5.MustBeGreaterThanOrEqualToZero.Violation
    {
        // TODO !!! These checks are brutally slow.  Need to make Advocate fast:
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              direction, board, source_conductor, source_wire, tags );
        classAdvocate.check ( Parameter5.MustBeGreaterThanOrEqualToZero.CONTRACT,
                              version );

        this.direction = direction;
        this.board = board;
        this.sourceConductor = source_conductor;
        this.sourceWire = source_wire;
        this.tags = tags;
        this.version = version;

        this.hashCode =
            this.direction.hashCode ()
            + this.board.hashCode ()
            + this.sourceConductor.hashCode ()
            + this.sourceWire.hashCode ()
            + this.tags.hashCode ()
            + this.version;
    }

    /**
     * @return The Board in which the target Conductor resides.
     *         Never null.
     */
    public final Board board ()
        throws Return.NeverNull.Violation
    {
        return this.board;
    }

    /**
     * @return The direction of this pulse: PUSH to send data,
     *         PULL to retrieve data.  Never null.
     */
    public final Pulse.Direction direction ()
    {
        return this.direction;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }

    /**
     * <p>
     * Creates a new Pulse, from the specified source, with the
     * same direction and Board, and a copy of the same set of Tags.
     * </p>
     *
     * @param source_conductor The Conductor from which push ()
     *                         and / or pull () requests originate.
     *                         <code> sourceConductor --&gt; sourceWire --&gt; target Conductor </code>.
     *                         Must not be null.
     *
     * @param source_wire The Wire source from which push ()
     *                    and / or pull () requests originate.
     *                    <code> sourceConductor --&gt; sourceWire --&gt; target Conductor </code>.
     *                    Must not be null.
     *
     * @return A new Pulse with the same direction and Board,
     *         the specified source chain, and a copy of
     *         the same set of Tags as this Pulse.  Never null.
     */
    public final Pulse next (
            Conductor source_conductor,
            Wire source_wire
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation
    {
        final Pulse next_pulse = new Pulse ( this.direction,
                                             this.board,
                                             source_conductor,
                                             source_wire,
                                             new Tags ( this.tags ),
                                             this.version + 1 );
        return next_pulse;
    }

    /**
     * <p>
     * Creates a new Pulse, from the specified source,
     * to Conductor(s) residing on the specified Board,
     * with the same direction and a copy of the same set of Tags.
     * </p>
     *
     * @param board The Board housing the Conductor(s) to which
     *              push () / pull () request(s) will be sent.
     *              Must not be null.
     *
     * @param source_conductor The Conductor from which push ()
     *                         and / or pull () requests originate.
     *                         <code> sourceConductor --&gt; sourceWire --&gt; target Conductor </code>.
     *                         Must not be null.
     *
     * @param source_wire The Wire source from which push ()
     *                    and / or pull () requests originate.
     *                    <code> sourceConductor --&gt; sourceWire --&gt; target Conductor </code>.
     *                    Must not be null.
     *
     * @return A new Pulse to the specified Board, the specified
     *         source chain, with the same direction and a copy
     *         of the same set of Tags as this Pulse.  Never null.
     */
    public final Pulse next (
            Board board,
            Conductor source_conductor,
            Wire source_wire
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation
    {
        final Pulse next_pulse = new Pulse ( this.direction,
                                             board,
                                             source_conductor,
                                             source_wire,
                                             new Tags ( this.tags ),
                                             this.version + 1 );
        return next_pulse;
    }

    /**
     * @return The Conductor from which this Pulse originated.
     *         <code> sourceConductor --&gt; sourceWire --&gt; target Conductor </code>.
     *         Never null.
     */
    public final Conductor sourceConductor ()
        throws Return.NeverNull.Violation
    {
        return this.sourceConductor;
    }

    /**
     * @return The Wire from which this Pulse originated.
     *         <code> sourceConductor --&gt; sourceWire --&gt; target Conductor </code>.
     *         Never null.
     */
    public final Wire sourceWire ()
        throws Return.NeverNull.Violation
    {
        return this.sourceWire;
    }

    /**
     * @return The Tags metadata which the target Conductor might
     *         need to process a pull () / push () request, or that
     *         might be required by other Conductors downstream.
     *         Never null.
     */
    public final Tags tags ()
        throws Return.NeverNull.Violation
    {
        return this.tags;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "pulse" );
        sbuf.append ( " [ direction = " + this.direction + " ]" );
        sbuf.append ( " [ board = " + this.board + " ]" );
        sbuf.append ( " [ source_conductor = " + this.sourceConductor + " ]" );
        sbuf.append ( " [ source_wire = " + this.sourceWire + " ]" );
        sbuf.append ( " [ tags = " + this.tags + " ]" );

        final String as_string = sbuf.toString ();
        return as_string;
    }
}
