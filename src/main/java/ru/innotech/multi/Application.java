package ru.innotech.multi;

public class Application {

    final static int DELAY_THREAD = 800;
    final static int THREAD_COUNT = 5;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("--- Начало: Pool Threads ---");

        // создание кастомного пула фиксированного размера на THREAD_COUNT потоков
        MyThreadPool threadPool = new MyThreadPool(THREAD_COUNT);

        // кидаем в пул 10 задач
        for (int i = 0; i < 10; i++) {
            int taskNum = i;
            threadPool.execute(() -> {
                System.out.printf("Начало: Выполнение задачи: %s потоком: %s%n", taskNum, Thread.currentThread().getName());
                threadPool.sleepFunc.accept(DELAY_THREAD);
                System.out.printf("Конец: Выполнение задачи: %s потоком: %s%n", taskNum, Thread.currentThread().getName());
            });
        }

        System.out.println("--- перед awaiting ---");
        //---- ожидаем когда все потоки отработают и очередь будет пустая --------
        threadPool.awaitTermination();
        System.out.println("--- после awaiting ---");

        // ---- Перевод всех потоков в статус -> TERMINATED
        threadPool.shutdown();
        System.out.println("--- Конец: Pool Threads ---");
    }

}
