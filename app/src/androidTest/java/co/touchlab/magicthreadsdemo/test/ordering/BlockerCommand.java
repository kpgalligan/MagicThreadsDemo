package co.touchlab.magicthreadsdemo.test.ordering;

import android.content.Context;

import co.touchlab.android.threading.errorcontrol.SoftException;
import co.touchlab.android.threading.tasks.persisted.Command;

/**
 * Created by kgalligan on 10/5/14.
 */
public class BlockerCommand extends Command
{
    @Override
    public void run(Context context) throws SoftException, Throwable
    {
        throw new SoftException();
    }

    @Override
    public boolean handlePermanentError(Context context, Throwable exception)
    {
        return false;
    }
}