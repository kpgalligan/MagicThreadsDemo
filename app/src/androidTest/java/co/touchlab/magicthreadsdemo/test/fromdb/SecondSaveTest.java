package co.touchlab.magicthreadsdemo.test.fromdb;

import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.android.threading.utils.UiThreadContext;
import co.touchlab.magicthreadsdemo.OptionsActivity;
import co.touchlab.magicthreadsdemo.test.NetworkExceptionCommand;
import co.touchlab.magicthreadsdemo.test.NeverCommand;
import co.touchlab.magicthreadsdemo.test.TestCommand;
import co.touchlab.magicthreadsdemo.test.utils.ThreadHelper;

/**
 * Created by kgalligan on 10/4/14.
 */
public class SecondSaveTest extends ActivityInstrumentationTestCase2<OptionsActivity>
{
    OptionsActivity activity;
    private Handler handler;
    PersistedTaskQueue queue;
    PersistedTaskQueue.PersistedTaskQueueState queueState;

    public SecondSaveTest()
    {
        super(OptionsActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        handler = ThreadHelper.mainHandler();

        activity = getActivity();
    }

    public void testInsertSavedValues()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                queue = DefaultPersistedTaskQueue.getInstance(getActivity());
            }
        });

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                queueState = queue.copyState();
            }
        }, 1200);

        ThreadHelper.sleep(2000);
        assertEquals(queueState.getPending().size(), 0);
        assertEquals(queueState.getQueued().size(), 2);
        assertNull(queueState.getCurrentTask());
    }
}
