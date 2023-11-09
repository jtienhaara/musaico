package table;

import java.util.Comparator;

public class TagIDComparator
    implements Comparator<Tag>
{
    public final int compare (
            Tag left,
            Tag right
            )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return 0;
            }
            else
            {
                // null > any Tag.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // any Tag < null.
            return Integer.MIN_VALUE + 1;
        }

        final String left_id = left.id ();
        final String right_id = right.id ();
        final int comparison = left_id.compareToIgnoreCase ( right_id );
        if ( comparison != 0 )
        {
            return comparison;
        }

        return left_id.compareTo ( right_id );
    }
}
