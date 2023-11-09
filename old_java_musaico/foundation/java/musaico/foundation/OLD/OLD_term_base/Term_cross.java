

    /**
     * <p>
     * Creates a cross-product of sorts: an Operation which maps
     * each input Term containing the elements of this
     * Term to an output Term containing the elements of that Term.
     * </p>
     *
     * <p>
     * If this Term has elements indexed <code> 0L, 1L, 2L, ... M </code>,
     * and that Term as elements indexed <code> 0L, 1L, 2L, ... N </code>,
     * then the resulting Operation maps each of the first
     * <code> min ( M, N ) </code> elements of this Term to the same
     * indexed element in that Term.  If <code> M == N </code> then
     * the Operation maps elements 1:1.  If <code> M &gt; N </code>
     * then the Operation will map the elements of each input
     * from the first <code> N </code> elements to the <code> N </code>
     * elements of that Term, but when the Operation receives
     * an input Term containing any of the last <code> M - N </code>
     * elements of this Term, those elements will not be mapped at the output.
     * If <code> M &lt; N </code> then all of the elements of this
     * Term have corresponding output elements.
     * </p>
     *
     * @param that The Term to cross.  Must not be null.
     *
     * @return The Operation from elements in this Term to elements
     *         in that Term.  Never null.
     */
    public abstract <TO>
        Operation<VALUE, TO> cross (
            Term<TO> that
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
