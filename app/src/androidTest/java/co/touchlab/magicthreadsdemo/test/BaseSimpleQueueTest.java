package co.touchlab.magicthreadsdemo.test;

import android.os.Handler;
import android.test.UiThreadTest;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.android.threading.utils.UiThreadContext;

/**
 * Created by kgalligan on 10/4/14.
 */
public abstract class BaseSimpleQueueTest extends BaseQueueTest
{
    private Handler handler;
    PersistedTaskQueue queue;
    PersistedTaskQueue.PersistedTaskQueueState queueState;

    @UiThreadTest
    public void testUiThread()
    {
        UiThreadContext.assertUiThread();

        handler = new Handler();

        queue = DefaultPersistedTaskQueue.getInstance(getActivity());
        runQueueOps();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                checkQueueState();
            }
        }, 5000);
    }

    protected abstract void runQueueOps();

    private void checkQueueState()
    {
        queueState = queue.clearQueue();
    }


    @Override
    protected void tearDown() throws Exception
    {
        Thread.sleep(7000);
        asserQueueState();

        super.tearDown();

    }

    protected abstract void asserQueueState();
}
