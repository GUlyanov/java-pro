package ru.innotech.multi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class MyThreadPool {
    private final LinkedList<Runnable> taskList = new LinkedList<>();
    private final List<MyThread> threadList = new ArrayList<>();
    private volatile boolean isShutdown = false;

    Consumer<Integer> sleepFunc = (x) -> {
        try {
            Thread.sleep(x);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public MyThreadPool(int capacity) {
        for (int i = 0; i <= capacity; i++) {
            MyThread worker = new MyThread();
            threadList.add(worker);
            worker.start();
        }
        System.out.printf("Создан custom TreadPool из: %s потоков%n",capacity);
    }

    public void execute(Runnable task) {

        if (isShutdown) {
            throw new IllegalStateException("Пул потоков остановлен, задачи не принимаются");
        }
        synchronized (taskList) {
            taskList.addLast(task);
            taskList.notifyAll();
        }
    }

    public void shutdown() {
        isShutdown = true;
        for (MyThread worker : threadList) {
            worker.interrupt();
        }
    }

    public void awaitTermination() throws InterruptedException {
        while (true) {
            boolean isComplete = threadList.stream()
                    .map(th -> th.getState() == Thread.State.WAITING)
                    .reduce(Boolean::logicalAnd)
                    .orElse(false);
            sleepFunc.accept(100);
            if (isComplete && taskList.size() == 0) return;
        }
    }

    private class MyThread extends Thread {
        public void run() {
            while (true) {
                Runnable task;
                synchronized (taskList) {
                    while (taskList.isEmpty()) {
                        try {
                            taskList.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    task = taskList.removeFirst();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
