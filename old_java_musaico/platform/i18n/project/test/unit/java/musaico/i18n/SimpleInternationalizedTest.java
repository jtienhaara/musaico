package musaico.i18n;

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


import musaico.state.IntLabel;
import musaico.state.Label;
import musaico.state.SimpleArc;
import musaico.state.StringLabel;
import musaico.state.Traversal;
import musaico.state.TraversalException;
import musaico.state.Traverser;


/**
 * <p>
 * Unit test cases for SimpleInternationalized.
 * </p>
 *
 * <p>
 * @see musaico.i18n.SimpleInternationalized
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
 * Copyright (c) 2010, 2012 Johann Tienhaara
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
public class SimpleInternationalizedTest
{
    /** Simple Localizer for testing purposes. */
    private final Localizer<String,String> stringLocalizer =
        new Localizer<String,String> ()
        {
            public String localize (
                                    String international_input,
                                    Locale locale
                                    )
            throws LocalizationException
            {
                return "" + international_input + locale;
            }
        };
}
