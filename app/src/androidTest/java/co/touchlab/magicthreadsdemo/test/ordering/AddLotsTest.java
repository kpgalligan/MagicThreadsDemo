package co.touchlab.magicthreadsdemo.test.ordering;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.magicthreadsdemo.test.BaseQueueTest;
import co.touchlab.magicthreadsdemo.test.BaseSimpleQueueTest;

/**
 * Created by kgalligan on 10/5/14.
 */
public class AddLotsTest extends BaseSimpleQueueTest
{

    private PersistedTaskQueue.PersistedTaskQueueState stateAfterAdd;

    @Override
    protected void runQueueOps()
    {
        PersistedTaskQueue queue = getQueue();

        for(int i=0; i<20; i++)
        {
            queue.execute(new NumberedCommand(i));
        }

        queue.execute(new BlockerCommand());

        for(int i=20; i<40; i++)
        {
            queue.execute(new NumberedCommand(i));
        }


        stateAfterAdd = queue.copyState();
    }

    @Override
    protected void asserQueueState(PersistedTaskQueue.PersistedTaskQueueState endState)
    {
        assertEquals(stateAfterAdd.getPending().size(), 41);
        assertEquals(stateAfterAdd.getQueued().size(), 0);

        assertEquals(endState.getPending().size(), 0);
        assertEquals(endState.getQueued().size(), 21);
    }
}
