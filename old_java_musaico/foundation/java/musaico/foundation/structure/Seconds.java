package musaico.foundation.structure;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


/**
 * <p>
 * Constants for converting units of time to and from seconds,
 * such as nanoseconds, milliseconds, minutes, years, and so on.
 * </p>
 *
 * <p>
 * WARNING: These constants should be treated as deliberately imprecise.
 * There are many common tasks, when developing software, which require
 * time intervals such as "roughly 1 day" or "roughly 10 years" and so on,
 * for which the nuances of calendars and time-keeping are irrelevant.
 * However, in any case where leap years or Daylight Savings or leap seconds
 * and so on are important, these are NOT the constants to use!
 * </p>
 *
 * <p>
 *   The following source was used:
 *   <a href="http://www.convertunits.com/info/seconds">
 *     http://www.convertunits.com/info/seconds
 *   </a>
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
 * @see musaico.foundation.structure.MODULE#COPYRIGHT
 * @see musaico.foundation.structure.MODULE#LICENSE
 */
public class Seconds
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Rounds to 18 decimal places, more than double precision
     *  floating point, for BigDecimal division for time. */
    private static final MathContext PRECISION =
        new MathContext ( 18, RoundingMode.HALF_UP );


    private static final BigDecimal TWENTY_EIGHT = new BigDecimal ( "28" );
    private static final BigDecimal TWENTY_NINE = new BigDecimal ( "29" );
    private static final BigDecimal THIRTY = new BigDecimal ( "30" );
    private static final BigDecimal THIRTY_ONE = new BigDecimal ( "31" );
    private static final BigDecimal HUNDRED = new BigDecimal ( "100" );
    private static final BigDecimal THOUSAND = new BigDecimal ( "1000" );


    /** The number of seconds in 1 Gregorian calendar year
     *  (not a leap year). */
    public static final BigDecimal PER_YEAR_GREGORIAN =
        new BigDecimal ( "31556952" );
    public static final BigDecimal TO_YEARS_GREGORIAN =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_GREGORIAN, PRECISION );
    public static final BigDecimal PER_YEAR =
        Seconds.PER_YEAR_GREGORIAN;
    public static final BigDecimal TO_YEARS =
        Seconds.TO_YEARS_GREGORIAN;

    /** The number of seconds in 10 Gregorian calendar years. */
    public static final BigDecimal PER_DECADE_GREGORIAN =
        Seconds.PER_YEAR_GREGORIAN.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_GREGORIAN =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_GREGORIAN, PRECISION );
    public static final BigDecimal PER_DECADE =
        Seconds.PER_DECADE_GREGORIAN;
    public static final BigDecimal TO_DECADES =
        Seconds.TO_DECADES_GREGORIAN;

    /** The number of seconds in 100 Gregorian calendar years. */
    public static final BigDecimal PER_CENTURY_GREGORIAN =
        Seconds.PER_YEAR_GREGORIAN.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_GREGORIAN =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_GREGORIAN, PRECISION );
    public static final BigDecimal PER_CENTURY =
        Seconds.PER_CENTURY_GREGORIAN;
    public static final BigDecimal TO_CENTURIES =
        Seconds.TO_CENTURIES_GREGORIAN;

    /** The number of seconds in 1000 Gregorian calendar years. */
    public static final BigDecimal PER_MILLENIUM_GREGORIAN =
        Seconds.PER_YEAR_GREGORIAN.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_GREGORIAN =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_GREGORIAN, PRECISION );
    public static final BigDecimal PER_MILLENIUM =
        Seconds.PER_MILLENIUM_GREGORIAN;
    public static final BigDecimal TO_MILLENIA =
        Seconds.TO_MILLENIA_GREGORIAN;


    /** The number of seconds in 1 Julian calendar year. */
    public static final BigDecimal PER_YEAR_JULIAN =
        new BigDecimal ( "31557600" );
    public static final BigDecimal TO_YEARS_JULIAN =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_JULIAN, PRECISION );

    /** The number of seconds in 10 Julian calendar years. */
    public static final BigDecimal PER_DECADE_JULIAN =
        Seconds.PER_YEAR_JULIAN.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_JULIAN =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_JULIAN, PRECISION );

    /** The number of seconds in 100 Julian calendar years. */
    public static final BigDecimal PER_CENTURY_JULIAN =
        Seconds.PER_YEAR_JULIAN.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_JULIAN =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_JULIAN, PRECISION );

    /** The number of seconds in 1000 Julian calendar years. */
    public static final BigDecimal PER_MILLENIUM_JULIAN =
        Seconds.PER_YEAR_JULIAN.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_JULIAN =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_JULIAN, PRECISION );


    /** The number of seconds in 1 tropical year. */
    public static final BigDecimal PER_YEAR_TROPICAL =
        new BigDecimal ( "31556925" );
    public static final BigDecimal TO_YEARS_TROPICAL =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_TROPICAL, PRECISION );

    /** The number of seconds in 10 tropical years. */
    public static final BigDecimal PER_DECADE_TROPICAL =
        Seconds.PER_YEAR_TROPICAL.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_TROPICAL =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_TROPICAL, PRECISION );

    /** The number of seconds in 100 tropical years. */
    public static final BigDecimal PER_CENTURY_TROPICAL =
        Seconds.PER_YEAR_TROPICAL.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_TROPICAL =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_TROPICAL, PRECISION );

    /** The number of seconds in 1000 tropical years. */
    public static final BigDecimal PER_MILLENIUM_TROPICAL =
        Seconds.PER_YEAR_TROPICAL.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_TROPICAL =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_TROPICAL, PRECISION );


    /** The number of seconds in 1 sidereal year. */
    public static final BigDecimal PER_YEAR_SIDEREAL =
        new BigDecimal ( "31558149.504" );
    public static final BigDecimal TO_YEARS_SIDEREAL =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_SIDEREAL, PRECISION );

    /** The number of seconds in 10 sidereal years. */
    public static final BigDecimal PER_DECADE_SIDEREAL =
        Seconds.PER_YEAR_SIDEREAL.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_SIDEREAL =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_SIDEREAL, PRECISION );

    /** The number of seconds in 100 sidereal years. */
    public static final BigDecimal PER_CENTURY_SIDEREAL =
        Seconds.PER_YEAR_SIDEREAL.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_SIDEREAL =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_SIDEREAL, PRECISION );

    /** The number of seconds in 1000 sidereal years. */
    public static final BigDecimal PER_MILLENIUM_SIDEREAL =
        Seconds.PER_YEAR_SIDEREAL.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_SIDEREAL =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_SIDEREAL, PRECISION );


    /** The number of seconds in 1 anomalistic year. */
    public static final BigDecimal PER_YEAR_ANOMALISTIC =
        new BigDecimal ( "31558432.5504" );
    public static final BigDecimal TO_YEARS_ANOMALISTIC =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_ANOMALISTIC, PRECISION );

    /** The number of seconds in 10 anomalistic years. */
    public static final BigDecimal PER_DECADE_ANOMALISTIC =
        Seconds.PER_YEAR_ANOMALISTIC.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_ANOMALISTIC =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_ANOMALISTIC, PRECISION );

    /** The number of seconds in 100 anomalistic years. */
    public static final BigDecimal PER_CENTURY_ANOMALISTIC =
        Seconds.PER_YEAR_ANOMALISTIC.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_ANOMALISTIC =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_ANOMALISTIC, PRECISION );

    /** The number of seconds in 1000 anomalistic years. */
    public static final BigDecimal PER_MILLENIUM_ANOMALISTIC =
        Seconds.PER_YEAR_ANOMALISTIC.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_ANOMALISTIC =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_ANOMALISTIC, PRECISION );


    /** The number of seconds in 1 draconic year. */
    public static final BigDecimal PER_YEAR_DRACONIC =
        new BigDecimal ( "29947974.5563" );
    public static final BigDecimal TO_YEARS_DRACONIC =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_DRACONIC, PRECISION );

    /** The number of seconds in 10 draconic years. */
    public static final BigDecimal PER_DECADE_DRACONIC =
        Seconds.PER_YEAR_DRACONIC.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_DRACONIC =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_DRACONIC, PRECISION );

    /** The number of seconds in 100 draconic years. */
    public static final BigDecimal PER_CENTURY_DRACONIC =
        Seconds.PER_YEAR_DRACONIC.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_DRACONIC =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_DRACONIC, PRECISION );

    /** The number of seconds in 1000 draconic years. */
    public static final BigDecimal PER_MILLENIUM_DRACONIC =
        Seconds.PER_YEAR_DRACONIC.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_DRACONIC =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_DRACONIC, PRECISION );


    /** The number of seconds in 1 lunar year. */
    public static final BigDecimal PER_YEAR_LUNAR =
        new BigDecimal ( "30617568" );
    public static final BigDecimal TO_YEARS_LUNAR =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_LUNAR, PRECISION );

    /** The number of seconds in 10 lunar years. */
    public static final BigDecimal PER_DECADE_LUNAR =
        Seconds.PER_YEAR_LUNAR.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_LUNAR =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_LUNAR, PRECISION );

    /** The number of seconds in 100 lunar years. */
    public static final BigDecimal PER_CENTURY_LUNAR =
        Seconds.PER_YEAR_LUNAR.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_LUNAR =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_LUNAR, PRECISION );

    /** The number of seconds in 1000 lunar years. */
    public static final BigDecimal PER_MILLENIUM_LUNAR =
        Seconds.PER_YEAR_LUNAR.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_LUNAR =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_LUNAR, PRECISION );


    /** The number of seconds in 1 Gaussian year. */
    public static final BigDecimal PER_YEAR_GAUSSIAN =
        new BigDecimal ( "31558196.0131" );
    public static final BigDecimal TO_YEARS_GAUSSIAN =
        BigDecimal.ONE.divide ( Seconds.PER_YEAR_GAUSSIAN, PRECISION );

    /** The number of seconds in 10 Gaussian years. */
    public static final BigDecimal PER_DECADE_GAUSSIAN =
        Seconds.PER_YEAR_GAUSSIAN.multiply ( BigDecimal.TEN );
    public static final BigDecimal TO_DECADES_GAUSSIAN =
        BigDecimal.ONE.divide ( Seconds.PER_DECADE_GAUSSIAN, PRECISION );

    /** The number of seconds in 100 Gaussian years. */
    public static final BigDecimal PER_CENTURY_GAUSSIAN =
        Seconds.PER_YEAR_GAUSSIAN.multiply ( HUNDRED );
    public static final BigDecimal TO_CENTURIES_GAUSSIAN =
        BigDecimal.ONE.divide ( Seconds.PER_CENTURY_GAUSSIAN, PRECISION );

    /** The number of seconds in 1000 Gaussian years. */
    public static final BigDecimal PER_MILLENIUM_GAUSSIAN =
        Seconds.PER_YEAR_GAUSSIAN.multiply ( THOUSAND );
    public static final BigDecimal TO_MILLENIA_GAUSSIAN =
        BigDecimal.ONE.divide ( Seconds.PER_MILLENIUM_GAUSSIAN, PRECISION );


    /** The number of seconds in 1 week. */
    public static final BigDecimal PER_WEEK =
        new BigDecimal ( "604800" );
    public static final BigDecimal TO_WEEKS =
        BigDecimal.ONE.divide ( Seconds.PER_WEEK, PRECISION );

    /** The number of seconds in 1 hour. */
    public static final BigDecimal PER_HOUR =
        new BigDecimal ( "3600" );
    public static final BigDecimal TO_HOURS =
        BigDecimal.ONE.divide ( Seconds.PER_HOUR, PRECISION );

    /** The number of seconds in 1 day (not the start or end of Daylight
     *  Savings Time). */
    public static final BigDecimal PER_DAY_NOT_DAYLIGHT_SAVINGS =
        new BigDecimal ( "86400" );
    public static final BigDecimal TO_DAYS_NOT_DAYLIGHT_SAVINGS =
        BigDecimal.ONE.divide ( Seconds.PER_DAY_NOT_DAYLIGHT_SAVINGS, PRECISION );
    public static final BigDecimal PER_DAY =
        Seconds.PER_DAY_NOT_DAYLIGHT_SAVINGS;
    public static final BigDecimal TO_DAYS =
        Seconds.TO_DAYS_NOT_DAYLIGHT_SAVINGS;

    /** The number of seconds in 1 day when Daylight Savings Time starts
     *  in the spring, losing an hour from the length of the day. */
    public static final BigDecimal PER_DAY_DAYLIGHT_SAVINGS_START =
        Seconds.PER_DAY_NOT_DAYLIGHT_SAVINGS.subtract ( Seconds.PER_HOUR );
    public static final BigDecimal TO_DAYS_DAYLIGHT_SAVINGS_START =
        BigDecimal.ONE.divide ( Seconds.PER_DAY_DAYLIGHT_SAVINGS_START, PRECISION );
    public static final BigDecimal PER_DAY_DAYLIGHT_SAVINGS_SPRING =
        Seconds.PER_DAY_DAYLIGHT_SAVINGS_START;
    public static final BigDecimal TO_DAYS_DAYLIGHT_SAVINGS_SPRING =
        Seconds.TO_DAYS_DAYLIGHT_SAVINGS_START;

    /** The number of seconds in 1 day when Daylight Savings Time ends
     *  in the fall, adding an hour to the length of the day. */
    public static final BigDecimal PER_DAY_DAYLIGHT_SAVINGS_END =
        Seconds.PER_DAY_NOT_DAYLIGHT_SAVINGS.add ( Seconds.PER_HOUR );
    public static final BigDecimal TO_DAYS_DAYLIGHT_SAVINGS_END =
        BigDecimal.ONE.divide ( Seconds.PER_DAY_DAYLIGHT_SAVINGS_END, PRECISION );
    public static final BigDecimal PER_DAY_DAYLIGHT_SAVINGS_FALL =
        Seconds.PER_DAY_DAYLIGHT_SAVINGS_END;
    public static final BigDecimal TO_DAYS_DAYLIGHT_SAVINGS_FALL =
        Seconds.TO_DAYS_DAYLIGHT_SAVINGS_END;

    /** The number of seconds in 1 minute. */
    public static final BigDecimal PER_MINUTE =
        new BigDecimal ( "60" );
    public static final BigDecimal TO_MINUTES =
        BigDecimal.ONE.divide ( Seconds.PER_MINUTE, PRECISION );


    /** The number of seconds in 1 millisecond. */
    public static final BigDecimal PER_MILLISECOND =
        new BigDecimal ( "0.001" );
    public static final BigDecimal TO_MILLISECONDS =
        BigDecimal.ONE.divide ( Seconds.PER_MILLISECOND, PRECISION );

    /** The number of seconds in 1 microsecond. */
    public static final BigDecimal PER_MICROSECOND =
        new BigDecimal ( "0.000001" );
    public static final BigDecimal TO_MICROSECONDS =
        BigDecimal.ONE.divide ( Seconds.PER_MICROSECOND, PRECISION );

    /** The number of seconds in 1 nanosecond. */
    public static final BigDecimal PER_NANOSECOND =
        new BigDecimal ( "0.000000001" );
    public static final BigDecimal TO_NANOSECONDS =
        BigDecimal.ONE.divide ( Seconds.PER_NANOSECOND, PRECISION );


    /** The number of seconds in January. */
    public static final BigDecimal IN_JANUARY =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );

    /** The number of seconds in February (non leap year). */
    public static final BigDecimal IN_FEBRUARY_NON_LEAP_YEAR =
        Seconds.PER_DAY.multiply ( TWENTY_EIGHT );
    public static final BigDecimal IN_FEBRUARY =
        Seconds.IN_FEBRUARY_NON_LEAP_YEAR;

    /** The number of seconds in February (leap year). */
    public static final BigDecimal IN_FEBRUARY_LEAP_YEAR =
        Seconds.PER_DAY.multiply ( TWENTY_NINE );

    /** The number of seconds in March. */
    public static final BigDecimal IN_MARCH =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );

    /** The number of seconds in April. */
    public static final BigDecimal IN_APRIL =
        Seconds.PER_DAY.multiply ( THIRTY );

    /** The number of seconds in May. */
    public static final BigDecimal IN_MAY =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );

    /** The number of seconds in June. */
    public static final BigDecimal IN_JUNE =
        Seconds.PER_DAY.multiply ( THIRTY );

    /** The number of seconds in July. */
    public static final BigDecimal IN_JULY =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );

    /** The number of seconds in August. */
    public static final BigDecimal IN_AUGUST =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );

    /** The number of seconds in September. */
    public static final BigDecimal IN_SEPTEMBER =
        Seconds.PER_DAY.multiply ( THIRTY );

    /** The number of seconds in October. */
    public static final BigDecimal IN_OCTOBER =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );

    /** The number of seconds in November. */
    public static final BigDecimal IN_NOVEMBER =
        Seconds.PER_DAY.multiply ( THIRTY );

    /** The number of seconds in December. */
    public static final BigDecimal IN_DECEMBER =
        Seconds.PER_DAY.multiply ( THIRTY_ONE );


    // No point in creating a Seconds object, just use BigDecimal numbers.
    private Seconds ()
    {
    }
}
