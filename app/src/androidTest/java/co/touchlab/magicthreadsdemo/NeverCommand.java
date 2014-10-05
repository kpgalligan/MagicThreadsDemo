package co.touchlab.magicthreadsdemo;

import android.content.Context;
import android.util.Log;

import co.touchlab.android.threading.errorcontrol.SoftException;
import co.touchlab.android.threading.tasks.persisted.Command;

/**
 * Created by kgalligan on 10/4/14.
 */
public class NeverCommand extends Command
{
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
        Log.w("queuetest", "Never get here");
    }
}
