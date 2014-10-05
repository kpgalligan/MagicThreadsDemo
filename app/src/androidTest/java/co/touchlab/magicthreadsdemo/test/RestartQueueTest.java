package co.touchlab.magicthreadsdemo.test;

import android.os.Handler;
import android.test.UiThreadTest;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.android.threading.utils.UiThreadContext;

/**
 * Created by kgalligan on 10/4/14.
 */
public class RestartQueueTest extends BaseQueueTest
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
        queue.execute(new TestCommand());
        queue.execute(new NetworkExceptionCommand());
        queue.execute(new TestCommand());
        queue.execute(new NeverCommand());

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                restartQueue();
            }
        }, 3000);
    }

    private void restartQueue()
    {
        queue.restartQueue();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                checkQueueState();
            }
        }, 3000);
    }
    private void checkQueueState()
    {
        queueState = queue.copyState();
    }


    @Override
    protected void tearDown() throws Exception
    {
        Thread.sleep(9000);
        assertEquals(queueState.getPending().size(), 0);
        assertEquals(queueState.getQueued().size(), 0);
        assertNull(queueState.getCurrentTask());

        super.tearDown();

    }
}
