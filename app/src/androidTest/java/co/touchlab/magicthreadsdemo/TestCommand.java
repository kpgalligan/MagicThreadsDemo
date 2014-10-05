package co.touchlab.magicthreadsdemo;

import android.content.Context;
import android.util.Log;

import co.touchlab.android.threading.errorcontrol.SoftException;
import co.touchlab.android.threading.tasks.persisted.CheckedCommand;
import co.touchlab.android.threading.tasks.persisted.Command;

/**
 * Created by kgalligan on 10/4/14.
 */
public class TestCommand extends CheckedCommand
{
    @Override
    public boolean handlePermanentError(Context context, Throwable exception)
    {
        return false;
    }

    @Override
    public String logSummary()
    {
        return null;
    }

    @Override
    public boolean same(Command command)
    {
        return false;
    }

    @Override
    public void callCommand(Context context) throws SoftException, Throwable
    {
        Log.w("queuetest", "start");
        Thread.sleep(1000);
        Log.w("queuetest", "end");
    }
}
