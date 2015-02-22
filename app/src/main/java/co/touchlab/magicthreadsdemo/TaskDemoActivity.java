package co.touchlab.magicthreadsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.sticky.StickyTaskManager;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;
import co.touchlab.magicthreadsdemo.tasks.NullTask;
import co.touchlab.magicthreadsdemo.tasks.SaveFileTask;


public class TaskDemoActivity extends Activity
{
    private View doThing;
    private View progress;
    private StickyTaskManager stickyTaskManager;

    public static void callMe(Activity a)
    {
        Intent i = new Intent(a, TaskDemoActivity.class);
        a.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_demo);

        EventBusExt.getDefault().register(this);

        stickyTaskManager = new StickyTaskManager(savedInstanceState);

        doThing = findViewById(R.id.doThing);
        progress = findViewById(R.id.progress);

        doThing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendTask();
            }
        });
        checkRunning();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        stickyTaskManager.onSaveInstanceState(outState);
    }

    private void sendTask()
    {
        TaskQueue.loadQueueDefault(this).execute(new NullTask(stickyTaskManager));
        checkRunning();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(NullTask task)
    {
        if(stickyTaskManager.isTaskForMe(task))
        {
            checkRunning();
            findViewById(R.id.taskDone).setVisibility(View.VISIBLE);
        }
    }

    private void checkRunning()
    {
        boolean found = TaskQueueHelper.hasTasksOfType(TaskQueue.loadQueueDefault(this), NullTask.class);
        doThing.setEnabled(!found);
        progress.setVisibility(found ? View.VISIBLE : View.GONE);
    }
}
