package co.touchlab.magicthreadsdemo.sticky;

import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.sticky.StickyTask;
import co.touchlab.android.threading.tasks.sticky.StickyTaskManager;

/**
 * This does nothing, really.
 *
 * Created by kgalligan on 9/13/14.
 */
public class NullStickyTask extends StickyTask
{
    public NullStickyTask(StickyTaskManager taskManager)
    {
        super(taskManager);
    }

    @Override
    protected void run(Context context) throws Exception
    {
        Thread.sleep(5000);
    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }
}
