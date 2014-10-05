package co.touchlab.magicthreadsdemo.test;

import android.media.ThumbnailUtils;
import android.os.Handler;
import android.test.UiThreadTest;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.android.threading.utils.UiThreadContext;
import co.touchlab.magicthreadsdemo.test.utils.ThreadHelper;

/**
 * Created by kgalligan on 10/4/14.
 */
public class RestartQueueTest extends BaseQueueTest
{
    PersistedTaskQueue queue;
    PersistedTaskQueue.PersistedTaskQueueState queueState;

    public void testUiThread()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                queue = DefaultPersistedTaskQueue.getInstance(getActivity());
                queue.execute(new TestCommand());
                queue.execute(new NetworkExceptionCommand());
                queue.execute(new TestCommand());
                queue.execute(new NeverCommand());
            }
        });

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                queue.restartQueue();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        checkQueueState();
                    }
                }, 2000);
            }
        }, 2000);

        ThreadHelper.sleep(6000);
        assertEquals(queueState.getPending().size(), 0);
        assertEquals(queueState.getQueued().size(), 0);
        assertNull(queueState.getCurrentTask());
    }

    private void checkQueueState()
    {
        queueState = queue.copyState();
    }



}
