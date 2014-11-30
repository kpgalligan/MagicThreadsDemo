package co.touchlab.magicthreadsdemo.test;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.magicthreadsdemo.test.utils.ThreadHelper;

/**
 * Created by kgalligan on 10/4/14.
 */
public abstract class BaseSimpleQueueTest extends BaseQueueTest
{
    PersistedTaskQueue queue;
    PersistedTaskQueue.PersistedTaskQueueState queueState;
    int firstPause;
    int secondPause;

    protected BaseSimpleQueueTest(int firstPause, int secondPause)
    {
        super();
        this.firstPause = firstPause;
        this.secondPause = secondPause;
    }

    protected BaseSimpleQueueTest()
    {
        this(2000, 4000);
    }

    public void testUiThread()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                queue = DefaultPersistedTaskQueue.getInstance(getActivity());
                runQueueOps();
            }
        });

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                checkQueueState();
            }
        }, firstPause);

        ThreadHelper.sleep(secondPause);
        asserQueueState(queueState);
    }

    protected abstract void runQueueOps();

    private void checkQueueState()
    {
        queueState = queue.copyPersistedState();
    }

    protected abstract void asserQueueState(PersistedTaskQueue.PersistedTaskQueueState endState);

    public PersistedTaskQueue getQueue()
    {
        return queue;
    }
}
