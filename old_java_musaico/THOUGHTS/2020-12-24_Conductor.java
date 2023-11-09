public interface Carrier
{
    public Class<?> dataType ();
    public Object [] data ();
    public <DATA> DATA [] data ( Class<DATA> data_type );
}

public interface Conductor
{
    public abstract Carrier [] pull ( Conductor ... sinks );
    public abstract void push ( Conductor [] sources, Carrier ... data );
}

public interface Wire
    extends Conductor // Maybe not!!!???  Makes it confusing for sink/source to pull/push methods...
                      // But yes -- so that the Wire can be used to send the data to a remote implementation of the Conductor.
{
    public Conductor [] ends ();
}

public interface Tap
    extends Wire
{
    public Conductor [] taps ();
    public Wire [] wires ();
}

public interface Schematic
{
    public Conductor [] terminals (); or conductors ()?;
    public Schematic [] slots ();
    public Selector [] circuit ();
}

public interface Selector
{
    public Filter<Conductor> [] ends ();
}

public interface Board
{
    public Schematic [] schematic ();
    public Board [] motherboards ();
    public Board [] daughterboards ();
    public Wire [] wires ();
    public Wire [] wiresFrom ( Conductor end );
}

Selector filters:
- ConductorsFilter;
- MotherboardConductorsFilter;
- DaughterboardConductorsFilter;
- WiresFilter;

Conductors:
- StandardWire (push/pull to/from all other ends);
- StandardTap (add tags, push to taps / pull from taps, route according to tags);
- Terminal (same as Wire);
- Constant (always pull fixed value, send pushes to ground);
- Variable (pull variable value, push overwrites the value);
- Filter (push from "in" through filter to "kept"/"discarded" outputs; pull requested by "k"/"d" outputs, pull from "in" through filter, either return kept/discarded value or push to the other output and return []);

Schematics:
- Type ...;
- Machine ...;
- Processor ...;
- Tags ...;


// Probably better written in language, but here's a try in Java:
// Actually, this is a reusable data type -- not just Tags -- can
// also be used for tag names, but it's really just a hash table:
Schematic tags = new Schematic ()
    {
        private final Conductor [] terminals = new Conductor ()
            {
                new Terminal ( "clear" ),
                new Terminal ( "discard_by_name" ),
                new Terminal ( "discard_by_tag" ),
                new Terminal ( "discard_by_filter" ),
                new Terminal ( "keep_by_name" ),
                new Terminal ( "keep_by_tag" ),
                new Terminal ( "keep_by_filter" ),
                new Terminal ( "tag" ),
    public abstract TAGGED tag (
            Tag ... tags
            );

    public abstract String [] tagNames (
            Filter<Tag> filter
            );

    public abstract String [] tagNames (
            String ... names
            );

    public abstract String [] tagNamesFrom (
            Tag ... exact_tags
            );

    public abstract Tag [] tags (
            Filter<Tag> filter
            );

    public abstract Tag [] tags (
            String ... names
            );

    public abstract Tag [] tagsFrom (
            };
    public Conductor [] terminals ();
    public Schematic [] slots ();
    public Selector [] circuit ();
    };
