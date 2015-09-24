package co.touchlab.magicthreadsdemo.test.app;

import android.os.Handler;
import android.test.AndroidTestCase;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import co.touchlab.android.threading.tasks.BaseTaskQueue;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.magicthreadsdemo.tasks.NullTask;
import co.touchlab.magicthreadsdemo.test.utils.ThreadHelper;

/**
 * Created by kgalligan on 10/13/14.
 */
public class HeadlessTestCase extends AndroidTestCase
{
    public void testQueue() throws InterruptedException
    {
        final AtomicInteger innerCount = new AtomicInteger(0);
        Handler handler = ThreadHelper.mainHandler();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                int count = 0;
                TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
                TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
                TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
                TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
                countQueue(innerCount);
            }
        });

        Thread.sleep(1000);

        assertEquals(innerCount.get(), 4);

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                countQueue(innerCount);
            }
        });

        Thread.sleep(1000);

        assertEquals(innerCount.get(), 0);
    }

    public void testTiming() throws InterruptedException
    {
        final AtomicInteger innerCount1 = new AtomicInteger(0);
        final AtomicInteger innerCount2 = new AtomicInteger(0);
        final AtomicInteger innerCount3 = new AtomicInteger(0);
        final AtomicInteger innerCountEnd = new AtomicInteger(0);
        Handler handler = ThreadHelper.mainHandler();

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //Make sure we get through our things first
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    //Ehh
                }
            }
        });

        TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                countQueue(innerCount1);
            }
        });
        TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                countQueue(innerCount2);
            }
        });
        TaskQueue.loadQueueDefault(getContext()).execute(new NullTask());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                countQueue(innerCount3);
            }
        });
        Thread.sleep(3000);
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                countQueue(innerCountEnd);
            }
        });

        assertEquals(innerCount1.get(), 1);
        assertEquals(innerCount2.get(), 2);
        assertEquals(innerCount3.get(), 3);
        assertEquals(innerCountEnd.get(), 0);
    }

    public void testFiles()
    {
        assertEquals(getContext().getFilesDir().exists(), true);
        File dbPath = getContext().getDatabasePath("test");

        File parentDbPath = dbPath.getParentFile();
        assertEquals(parentDbPath.exists(), true);
        File[] files = parentDbPath.listFiles();
        for (File file : files)
        {
            file.delete();
        }
    }

    private void countQueue(final AtomicInteger innerCount)
    {
        innerCount.set(0);
        TaskQueue.loadQueueDefault(getContext()).query(new BaseTaskQueue.QueueQuery()
        {
            @Override
            public void query(BaseTaskQueue queue, Task task)
            {
                innerCount.addAndGet(1);
            }
        });
    }
}
