public interface Conditional<VALUE, CHECKED_EXCEPTION>
...
{
    ...

    public abstract Conditional<VALUE, CHECKED_EXCEPTION> blockingWait (
                                                                        long max_nanoseconds
                                                                        )
        throws TimeoutException;

    public abstract void handleAsynchronously (
                                               ConditionHandler handler
                                               );
    ...
}
