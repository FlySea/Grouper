package cn.com.libbasic.net;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.libbasic.net.BaseTask.TaskPrio;
import cn.com.libbasic.util.LogUtil;

/**
 * 线程池
 *
 */
public class ThreadScheduler {

    private static final String TAG = "ThreadScheduler";
    private static int THREAD_COUNT = 8;

    private static int THREAD_FILE_COUNT = 4;//文件线程最多占用个数
    private static int file_count = 0;//记录当前文件线程个数

    private ArrayList<BaseTask> taskLists[];
    private MyThread threads[];
    private HashMap<String, Boolean> hasMap = new HashMap<String, Boolean>();

    private static ThreadScheduler mThreadScheduler = null;

    public synchronized static ThreadScheduler getInstance() {
        if (mThreadScheduler == null) {
            mThreadScheduler = new ThreadScheduler();
        }
        return mThreadScheduler;
    }

    @SuppressWarnings("unchecked")
    ThreadScheduler() {
        taskLists = new ArrayList[BaseTask.TaskPrio.PRIO_COUNT];

        for (int i = 0; i < BaseTask.TaskPrio.PRIO_COUNT; i++) {
            taskLists[i] = new ArrayList<BaseTask>();
        }
        LogUtil.d(TAG, " ---ThreadScheduler---");

        threads = new MyThread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new MyThread();
            threads[i].start();
            LogUtil.d(TAG, " ---ThreadScheduler---i=" + i + " thread start");
        }

    }

    class MyThread extends Thread {
        boolean flag = true;

        public void run() {
            while (flag) {
                BaseTask task = getReadyTask();
                if (task != null) {
                    task.doJob();
                    hasMap.remove(task.getMD5());
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public synchronized void addTask(BaseTask task) {
        int pos;

        if (task.mTaskData.prio < 0 || task.mTaskData.prio >= BaseTask.TaskPrio.PRIO_COUNT)
            return;

        if (hasMap.containsKey(task.getMD5())) {
            return;
        }
        hasMap.put(task.getMD5(), true);
        pos = taskLists[task.mTaskData.prio].size();
        taskLists[task.mTaskData.prio].add(pos, task);

        LogUtil.d(TAG, " >>>>>>> TaskManager , addTask:" + task);
    }

    synchronized void removeTask() {
        LogUtil.d(TAG, ">>>>>>>>>>> removeTask ");
        for (int i = 0; i < BaseTask.TaskPrio.PRIO_COUNT; i++) {
            ArrayList<BaseTask> list1 = taskLists[i];
            LogUtil.d(TAG, "prio=" + i + ">>>>>>>>>>> removeTask clear list1.size=" + list1.size());
            list1.clear();
        }
    }

    public synchronized void removeTask(BaseTask task) {
        if (task.mTaskData.prio < 0 || task.mTaskData.prio >= BaseTask.TaskPrio.PRIO_COUNT)
            return;

        taskLists[task.mTaskData.prio].remove(task);

    }

    synchronized BaseTask getReadyTask() {
        for (int i = 0; i < BaseTask.TaskPrio.PRIO_COUNT; i++) {

            if (taskLists[i].size() > 0) {
                if (i == TaskPrio.PRIO_FILE && file_count > THREAD_FILE_COUNT) {//文件线程大于最大文件线程数
                    continue;
                }
                BaseTask task = taskLists[i].get(0);
                taskLists[i].remove(task);

                return task;
            }
        }

        return null;
    }

}
