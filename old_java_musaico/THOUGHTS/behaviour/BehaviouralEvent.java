public interface BehaviouralEvent<BEHAVING_OBJECT extends Object>
{
    public abstract void exectue (
                                  BEHAVING_OBJECT behaving_object
                                  )
        throws BehaviourViolation;
}
