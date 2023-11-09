package musaico.platform.time;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.TimeZone;


import junit.framework.JUnit4TestAdapter;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * <p>
 * A few random experiments with time.
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class ExperimentsWithTime
{
    /**
     * <p>
     * Experiments with Time objects.
     * </p>
     */
    @Test
    public void experiments1 ()
    {
        System.out.println ( "" );

        final TimeZone atlantic_daylight_savings_time =
            TimeZone.getTimeZone ( "ADT" );
        final Calendar calendar =
            Calendar.getInstance ( atlantic_daylight_savings_time);
        final SimpleDateFormat formatter =
            new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss z" );

        // Tuesday Oct 1 18:23:54 ADT
        AbsoluteTime now = new AbsoluteTime ( 1380662634000L );
        Time one_day = new Days ( 1, calendar );
        AbsoluteTime one_day_from_now = (AbsoluteTime) now.add ( one_day );
        AbsoluteTime one_day_ago = (AbsoluteTime) now.subtract ( one_day );

        System.out.println ( "Now              = "
                             + formatter.format ( now.date () ) );
        System.out.println ( "One day from now = "
                             + formatter.format ( one_day_from_now.date () ) );
        System.out.println ( "One day ago      = "
                             + formatter.format ( one_day_ago.date () ) );

        assertEquals ( "Incorrect now",
                       "2013-10-01 18:23:54 ADT",
                       formatter.format ( now.date () ) );
        assertEquals ( "Incorrect one day from now",
                       "2013-10-02 18:23:54 ADT",
                       formatter.format ( one_day_from_now.date () ) );
        assertEquals ( "Incorrect one day from now",
                       "2013-09-30 18:23:54 ADT",
                       formatter.format ( one_day_ago.date () ) );
    }
}
