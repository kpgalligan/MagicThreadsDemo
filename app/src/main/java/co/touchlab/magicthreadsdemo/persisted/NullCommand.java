package co.touchlab.magicthreadsdemo.persisted;

import android.content.Context;
import android.util.Log;

import co.touchlab.android.threading.errorcontrol.SoftException;
import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.persisted.CheckedCommand;
import co.touchlab.android.threading.tasks.persisted.Command;

/**
 * Created by kgalligan on 10/4/14.
 */
public class NullCommand extends CheckedCommand
{
    @Override
    public boolean handlePermanentError(Context context, Throwable exception)
    {
        return false;
    }

    @Override
    public String logSummary()
    {
        return "NullCommand";
    }

    @Override
    public boolean same(Command command)
    {
        return false;
    }

    @Override
    public void callCommand(Context context) throws SoftException, Throwable
    {
        Log.w("asdf", "time: "+ System.currentTimeMillis());
        Thread.sleep(5000);
        EventBusExt.getDefault().post(this);
    }
}
