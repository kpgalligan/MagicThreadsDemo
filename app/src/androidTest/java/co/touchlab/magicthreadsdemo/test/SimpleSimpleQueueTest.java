package co.touchlab.magicthreadsdemo.test;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;

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
    protected void asserQueueState(PersistedTaskQueue.PersistedTaskQueueState endState)
    {
        assertEquals(endState.getPending().size(), 0);
        assertEquals(endState.getQueued().size(), 3);
        assertNull(endState.getCurrentTask());
    }
}
