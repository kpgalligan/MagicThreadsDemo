package co.touchlab.magicthreadsdemo.test;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgalligan on 10/5/14.
 */
public class StaticLog
{
    static List<String> log = new ArrayList<String>();

    public static synchronized void log(String s)
    {
        StaticLog.log(s);
    }

    public static synchronized String logOut()
    {
        return StringUtils.join(log, "\n");
    }
}
