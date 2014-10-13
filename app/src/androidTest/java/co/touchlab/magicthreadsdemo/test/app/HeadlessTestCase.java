package co.touchlab.magicthreadsdemo.test.app;

import android.os.Handler;
import android.test.ApplicationTestCase;

import java.util.concurrent.atomic.AtomicInteger;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.TaskQueueActual;
import co.touchlab.magicthreadsdemo.DemoApplication;
import co.touchlab.magicthreadsdemo.tasks.NullTask;
import co.touchlab.magicthreadsdemo.test.utils.ThreadHelper;

/**
 * Created by kgalligan on 10/13/14.
 */
public class HeadlessTestCase extends ApplicationTestCase<DemoApplication>
{

    public HeadlessTestCase()
    {
        super(DemoApplication.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        getApplication();
    }

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
                TaskQueue.execute(getContext(), new NullTask());
                TaskQueue.loadQueueDefault().query(new TaskQueueActual.QueueQuery()
                {
                    @Override
                    public void query(TaskQueue.Task task)
                    {
                        innerCount.addAndGet(1);
                    }
                });
            }
        });

        Thread.sleep(3000);

        assertEquals(innerCount.get(), 1);
    }
}
