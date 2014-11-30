package co.touchlab.magicthreadsdemo.test;

import android.os.Handler;
import android.test.UiThreadTest;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.android.threading.utils.UiThreadContext;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends BaseQueueTest
{
    private Handler handler;
    private PersistedTaskQueue queue;
    private PersistedTaskQueue.PersistedTaskQueueState persistedTaskQueueState;

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
                checkQueueState();
            }
        }, 3000);
    }

    private void checkQueueState()
    {
        persistedTaskQueueState = queue.copyPersistedState();

    }


    @Override
    protected void tearDown() throws Exception
    {
        Thread.sleep(5000);
        assertEquals(persistedTaskQueueState.getPending().size(), 0);
        assertEquals(persistedTaskQueueState.getQueued().size(), 3);
        assertNull(persistedTaskQueueState.getCurrentTask());

        super.tearDown();

    }
}