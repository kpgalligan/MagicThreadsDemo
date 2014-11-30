package co.touchlab.magicthreadsdemo.test.fromdb;

import android.os.Handler;
import android.test.ApplicationTestCase;

import java.io.File;

import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.storage.DefaultPersistedTaskQueue;
import co.touchlab.android.threading.utils.UiThreadContext;
import co.touchlab.magicthreadsdemo.DemoApplication;
import co.touchlab.magicthreadsdemo.OptionsActivity;
import co.touchlab.magicthreadsdemo.test.NetworkExceptionCommand;
import co.touchlab.magicthreadsdemo.test.NeverCommand;
import co.touchlab.magicthreadsdemo.test.TestCommand;
import co.touchlab.magicthreadsdemo.test.utils.ThreadHelper;

/**
 * Created by kgalligan on 10/4/14.
 */
public class FirstSaveTest extends ApplicationTestCase<DemoApplication>
{
    public FirstSaveTest()
    {
        super(DemoApplication.class);
    }

    OptionsActivity activity;
    private Handler handler;
    PersistedTaskQueue queue;
    PersistedTaskQueue.PersistedTaskQueueState queueState;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        handler = ThreadHelper.mainHandler();

        File test = getContext().getDatabasePath("test");
        File parentFile = test.getParentFile();
        if(parentFile.exists())
        {
            File[] files = parentFile.listFiles();
            for (File file : files)
            {
                file.delete();
            }
        }
    }

    public void testInsertSavedValues()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                UiThreadContext.assertUiThread();

                queue = DefaultPersistedTaskQueue.getInstance(getContext());
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
                queueState = queue.copyPersistedState();
            }
        }, 2200);

        ThreadHelper.sleep(4000);
        assertEquals(queueState.getPending().size(), 0);
        assertEquals(queueState.getQueued().size(), 4);
        assertNull(queueState.getCurrentTask());
    }
}
