package musaico.platform.primitives.tag.string;

import java.io.Serializable;

import java.util.regex.Pattern;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.types.AbstractTag;
import musaico.foundation.types.Cast;
import musaico.foundation.types.Operation;
import musaico.foundation.types.PipeCast;
import musaico.foundation.types.Tag;

import musaico.platform.primitives.operations.string.Split;


/**
 * <p>
 * Tags a String as being a particular part of a text document, such
 * as a paragraph, a sentence or sentence fragment, a word, and so on.
 * </p>
 *
 * <p>
 * This Partitioned tag is really only meant for quick-and-dirty
 * text document processing, not industrial strength natural
 * language processing.
 * </p>
 *
 * <p>
 * For example, contractions are treated rather crudely.
 * In order to avoid treating "don't" as two separate sentence
 * fragments separated by a punctuation mark, apostrophes are
 * altogether ignored.  This can cause anomalies in text
 * processing, and you might be better off either constructing
 * your own regular expressions for Partitioned tags, or switching
 * to a smarter -- or industrial strength -- text processor
 * instead.
 * </p>
 *
 * <p>
 * For example, to convert a String into its constituent parts:
 * </p>
 *
 * <pre>
 *     Type<String> string = ...;
 *     Type<String> paragraph =
 *         string.sub ( Partitioned.PARAGRAPH );
 *     Type<String> sentence_fragment =
 *         string.sub ( Partitioned.SENTENCE_FRAGMENT );
 *     Type<String> word =
 *         string.sub ( Partitioned.WORD );
 *     Instance<String> text_document =
 *         "Hello, world!\n"
 *         + "The quick brown fox jumps over the lazy dog";
 *     for ( String part : text_document.value ( paragraph ) )
 *     {
 *         System.out.println ( "Paragraph:         " + part );
 *     }
 *     for ( String part : text_document.value ( sentence_fragment ) )
 *     {
 *         System.out.println ( "Sentence fragment: " + part );
 *     }
 *     for ( String part : text_document.value ( word ) )
 *     {
 *         System.out.println ( "Word:              " + part );
 *     }
 * </p>
 *
 * <p>
 * The above code would output:
 * </p>
 *
 * <pre>
 *     Paragraph:         Hello, world!
 *     Paragraph:         The quick brown fox jumps over the lazy dog
 *     Sentence fragment: Hello,
 *     Sentence fragment: world!
 *     Sentence fragment: The quick brown fox jumps over the lazy dog
 *     Word:              Hello
 *     Word:              world
 *     Word:              The
 *     Word:              quick
 *     Word:              brown
 *     Word:              fox
 *     Word:              jumps
 *     Word:              over
 *     Word:              the
 *     Word:              lazy
 *     Word:              dog
 * </pre>
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
public class Partitioned
    extends AbstractTag
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130831L;
    private static final String serialVersionHash =
        "0x9D551DEF6709F91075C6BBE1A222D282E0EB5B54";


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Partitioned.class );


    /** Splits each String into paragraphs split by one or more newlines
     *  (\r or \n). */
    public static final Partitioned PARAGRAPH =
        new Partitioned ( "paragraph",
                          Pattern.compile ( "[\\s]*[\\r\\n]+[\\s]*" ) );

    /** Splits each String into sentences split by a period or an ellipsis
     *  or a question mark, exclamation point, and so on. */
    public static final Partitioned SENTENCE =
        new Partitioned ( "sentence",
                          Pattern.compile ( "[\\s]*(\\.(\\.\\.)?)|[\\?\\!][\\s]*" ) );

    /** Splits each String into sentence fragments, split by one
     *  or more punctuation marks. */
    public static final Partitioned SENTENCE_FRAGMENT =
        new Partitioned ( "sentence_fragment",
                          Pattern.compile ( "[\\s]*[\\p{Punct}&&[^']][\\s\\p{Punct}]*" ) );

    /** Splits each String into words, each split by space(s)
     *  or punctuation mark(s) or newline(s).  Note that apostrophe
     *  (') is deliberately ignored because of English possessives
     *  and contractions. */
    public static final Partitioned WORD =
        new Partitioned ( "word",
                          Pattern.compile ( "[\\W&&[^']][\\W]*" ) );


    /** The regular expression used to split the text into parts. */
    private final Pattern partRegex;


    /**
     * <p>
     * Creates a new Partitioned tag using the specified regular expression
     * to split up each String into its constituent parts.
     * </p>
     *
     * @param id The unique identifier for this tag, distinguishing
     *           it within a typing environment from other text tags.
     *           Must not be null.
     *
     * @param part_regex The regular expression used to split each
     *                   String into parts.  Must not be null.
     */
    public Partitioned (
                        String id,
                        Pattern part_regex
                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( id );

        Partitioned.classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                           id, part_regex );

        this.partRegex = part_regex;
    }


    /**
     * @see musaico.foundation.types.Tag#refine(musaico.foundation.types.Operation)
     */
    @Override
    @SuppressWarnings("unchecked") // We check for String then cast
    public final <INPUT extends Object, OUTPUT extends Object>
        Operation<INPUT, OUTPUT> refine (
                                         Operation<INPUT, OUTPUT> operation
                                         )
    {
        if ( operation instanceof Cast )
        {
            Cast<INPUT, OUTPUT> caster = (Cast<INPUT, OUTPUT>) operation;
            final Class<OUTPUT> target_class = caster.targetClass ();

            if ( target_class == String.class )
            {
                // Once the cast to a String has been finished,
                // split up the String into its constituent parts.
                final Class<INPUT> source_class = caster.sourceClass ();
                final Operation<OUTPUT, OUTPUT> partitioner =
                    (Operation<OUTPUT, OUTPUT>)
                    new Split ( this.partRegex );
                final Cast<INPUT, OUTPUT> piper =
                    new PipeCast<INPUT, OUTPUT> ( source_class,
                                                  target_class,
                                                  caster.none (),
                                                  caster, // Op # 1
                                                  partitioner ); // Op # 2
                return piper;
            }
        }

        // Leave the operation as-is.
        return operation;
    }
}
