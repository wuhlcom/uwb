package com.zhilutec.common;

public class ThreadUtil {//操作查看JVM虚拟机中所有的线程和线程组的类

    public static void listAllThreads() {//找到根线程组并列出它递归的信息
        ThreadGroup currentThreadGroup;//当前线程组
        ThreadGroup rootThreadGroup;//根线程组
        ThreadGroup parent;
        currentThreadGroup = Thread.currentThread().getThreadGroup();//获取当前活动线程所在的线程组
        rootThreadGroup = currentThreadGroup;//赋值给变量-根线程组
        parent = rootThreadGroup.getParent();//获取上级线程组/上级线程
        while (parent != null) {//循环对根线程组重新赋值
            rootThreadGroup = parent;
            parent = rootThreadGroup.getParent();
        }
        showThreadGroup(rootThreadGroup, "");
    }

    public static void showThreadGroup(ThreadGroup group, String index) {//显示线程组信息
        if (group == null) return;//线程组为空，直接返回
        int count = group.activeCount(); //获取活动的线程
        int countGroup = group.activeGroupCount();//获取活动的线程组数
        //System.out.println("活动的线程  ： "+count);
        //System.out.println("活动的线程组数  ： "+countGroup);
        Thread[] threads = new Thread[count];
        ThreadGroup[] groups = new ThreadGroup[countGroup];
        //把当前组下的所有活动线程/线程组的引用复制到指定数组中，false表示不包括对当前组下活动线程组的线程/线程组的引用
        group.enumerate(threads, false);//把线程放到当前的ThreadGroup中
        group.enumerate(groups, false);//把线程组放到当前的ThreadGroup中
        System.out.println(index + "线程组的名称- " + group.getName() + " 最高优先级- " + group.getMaxPriority() + (group.isDaemon() ? " 守护" : " "));
        for (int i = 0; i < count; i++) {
            showThread(threads[i], index + "  ");// 展示ThreadGroup中所包含的线程的信息
        }
        for (int i = 0; i < countGroup; i++) {// 展示ThreadGroup中所包含的线程组的信息
            showThreadGroup(groups[i], index + "  ");//递归调用方法展示ThreadGroup中所包含的线程组中的信息
        }
    }

    public static void showThread(Thread thread, String index) {
        if (thread == null) return;
        System.out.println(index + "线程的名称-" + thread.getName() + " 最高优先级- " + thread.getPriority() + (thread.isDaemon() ? " 守护" : " ") + (thread.isAlive() ? " 活动" : " 不活动"));
    }
}