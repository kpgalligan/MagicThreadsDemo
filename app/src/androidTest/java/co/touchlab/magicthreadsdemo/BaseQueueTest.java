package co.touchlab.magicthreadsdemo;

import android.test.ActivityInstrumentationTestCase2;

import java.io.File;

/**
 * Created by kgalligan on 10/4/14.
 */
public class BaseQueueTest extends ActivityInstrumentationTestCase2<OptionsActivity>
{
    OptionsActivity activity;

    public BaseQueueTest()
    {
        super(OptionsActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        File test = getInstrumentation().getTargetContext().getDatabasePath("test");
        File[] files = test.getParentFile().listFiles();
        for (File file : files)
        {
            file.delete();
        }

        activity = getActivity();
    }
}
