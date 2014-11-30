package co.touchlab.magicthreadsdemo.persisted;

import android.content.Context;
import android.util.Log;

import co.touchlab.android.threading.errorcontrol.SoftException;
import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.persisted.Command;

/**
 * Created by kgalligan on 10/4/14.
 */
public class NullCommand extends Command
{
    @Override
    public boolean handleError(Context context, Throwable exception)
    {
        return false;
    }

    @Override
    public void run(Context context) throws SoftException, Throwable
    {
        Log.w("asdf", "time: "+ System.currentTimeMillis());
        Thread.sleep(5000);
        EventBusExt.getDefault().post(this);
    }
}
