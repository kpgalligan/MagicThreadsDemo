package co.touchlab.magicthreadsdemo;

/**
 * Created by kgalligan on 10/4/14.
 */
public class SimpleSimpleQueueTest extends BaseSimpleQueueTest
{
    @Override
    protected void runQueueOps()
    {
        queue.execute(new TestCommand());
        queue.execute(new NetworkExceptionCommand());
        queue.execute(new TestCommand());
        queue.execute(new NeverCommand());
    }

    @Override
    protected void asserQueueState()
    {
        assertEquals(queueState.getPending().size(), 0);
        assertEquals(queueState.getQueued().size(), 3);
        assertNull(queueState.getCurrentTask());
    }
}
