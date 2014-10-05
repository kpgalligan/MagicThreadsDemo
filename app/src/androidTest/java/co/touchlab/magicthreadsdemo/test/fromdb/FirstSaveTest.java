package co.touchlab.magicthreadsdemo.test.fromdb;

import android.os.Handler;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import java.io.File;

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
public class FirstSaveTest extends ActivityInstrumentationTestCase2<OptionsActivity>
{
    OptionsActivity activity;
    private Handler handler;
    PersistedTaskQueue queue;
    PersistedTaskQueue.PersistedTaskQueueState queueState;

    public FirstSaveTest()
    {
        super(OptionsActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        handler = ThreadHelper.mainHandler();

        File test = getInstrumentation().getTargetContext().getDatabasePath("test");
        File[] files = test.getParentFile().listFiles();
        for (File file : files)
        {
            file.delete();
        }

        activity = getActivity();
    }

    public void testInsertSavedValues()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                UiThreadContext.assertUiThread();

                queue = DefaultPersistedTaskQueue.getInstance(getActivity());
                queue.execute(new TestCommand());
                queue.execute(new NetworkExceptionCommand());
                queue.execute(new TestCommand());
                queue.execute(new NetworkExceptionCommand());
                queue.execute(new NeverCommand());
            }
        });

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                queueState = queue.copyState();
            }
        }, 2200);

        ThreadHelper.sleep(4000);
        assertEquals(queueState.getPending().size(), 0);
        assertEquals(queueState.getQueued().size(), 4);
        assertNull(queueState.getCurrentTask());
    }
}
