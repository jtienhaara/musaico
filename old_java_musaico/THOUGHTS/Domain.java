public interface Domain<D> extends Filter<D>
{
    public Filter<D> filter ();
    {
        public Filter.State filter ( D );

        public void filter ( Collection<D> );
    }

    public void addInsideDomainTestData ( Collection<D> );

    public void addOutsideDomainTestData ( Collection<D> );
}

Order and OrderedFilter

Filters:

  Not
  Null
  Zero (OrderedFilter)
    Integer etc
  One (OrderedFilter)
    Integer etc
  ContainsDuplicates
    Array, Collection, Iterable
  Empty
    Array, Collection, Iterable, String
  MinimumLength (OrderedFilter)
    sub-domain<int>
    Array, Collection, Iterable, String
  MaximumLength

Domains:

NoDomain
AllDomain
Is
  Null
  Zero
  One
  SpecificValue
IsNot
  Null
  Zero
  One
  SpecificValue
GreaterThan
  Zero
  One
  SpecificValue
GreaterThanOrEqualTo
  Zero
  One
  SpecificValue
LessThan
  Zero
  One
  SpecificValue
LessThanOrEqualTo
  Zero
  One
  SpecificValue
Contains
  Null
  Duplicates
  SpecificValue
  SpecificValues
Excludes
  Null
  Duplicates
  SpecificValue
  SpecificValues
Empty
NotEmpty





public class StandardDomains
{
    public static class Is
    {
        public static class Null
            implements Domain<Object>, Filter<Object>
        {
            public static final Null DOMAIN = new Null ();

            public Filter.State filter ( Object object )
            {
                ...;
            }
            ...;
        }
    }
}

public class Parameter1
{
    public static class Is
    {
        public static class Null
            implements Contract<Object>
        {
            public Domain<Object> domain ()
            {
                return AllDomains.Is.Null.DOMAIN;
            }
            public Enforcer<Object, Parameter1.Violation> enforcer ()
            {
                return this;
            }
            public Parameter1.Is.Null.Violation enforce ( Contract<...> contract,
                                                          Object patron,
                                                          Object inspectable )
            {
                return new Parameter1.Is.Null.Violation ( contract, patron, inspectable );
            }

            public static class Violation extends Parameter1.Violation
            {
                public Violation ( Contract<...> contract,
                                   Object patron,
                                   Object inspectable )
                {
                    super ( contract, patron, inspectable );
                }
            }
        }
    }
}
