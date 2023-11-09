public interface Platform
{
    public abstract ContractArbiter contracts ();

    public abstract I18n i18n ();

    public abstract TypingEnvironment types ();
}


public interface I18n
{
    public abstract Conditional<InstanceBuilder> internationalize ( Object );
    // Returns an InstanceBuilder which has an internationalized storage
    // value and can be cast to other locale types.
    //
    // The return value of the eventual Instance.value ( LocaleTypes.XYZ )
    // is a localized Instance which can then be cast again to a value
    // to be printed etc (e.g. by calling .value ( String.class ) or
    // .value ( Integer.class ) and so on).
    //
    // For example:
    //
    //     I18n i18n = ...;
    //     LocaleType locale = ...;
    //     Date current_date = new Date ();
    //     String date_as_string =
    //         i18n.internationalize ( current_date ).orNone ().build ()
    //             .localize ( locale ).orNone ().value ( String.class )
    //             .orNone ();
    //
    // Except that it's way too verbose!
    //

    String date_as_string = i18n.localize ( instance, locale )
        .value ( String.class ).orNone ();
    Then the I18n creates a new instance whose type is a localized
    version of the original type, and whose typecasters
    do localized typecasting (e.g. the to-string
    typecaster, which creates a localized string).

    Parsers and formatters aren't far off i18n & l10n.
    For example converting a number to "1,000,000.00" or
    "1.000.000,00" isn't very different from the formatting
    half of a parser/formatter pair.
}
