package co.touchlab.magicthreadsdemo.test;

import android.content.Context;
import android.util.Log;

import co.touchlab.android.threading.errorcontrol.SoftException;
import co.touchlab.android.threading.tasks.persisted.Command;
import co.touchlab.android.threading.utils.UiThreadContext;

/**
 * Created by kgalligan on 10/4/14.
 */
public class NetworkExceptionCommand extends Command
{
    public String message;
    private boolean firstRun = true;

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
        Log.w("queuetest", "Try and soft fail");
        if(firstRun)
        {
            firstRun = false;
            throw new SoftException("Network probs");
        }
    }

    @Override
    public void onTransientError(Context context, SoftException exception)
    {
        UiThreadContext.assertUiThread();
        Log.w("queuetest", "Transient error");
        message = exception.getMessage();
    }
}
