package locks;

import java.util.concurrent.Semaphore;

/**
 * Данный пример демонстрирует
 * применение семафора (примитива синхронизации)
 * для осуществления ограниченного доступа
 *
 * пакета java.util.concurrent
 */
public class SemaphoreShuffling {
    // не более 3-х разом
    public static Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) throws InterruptedException {
        // создадим и запустим кучку дочерних потоков
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();
        new Thread(new ChildRunnable()).start();

        semaphore.acquire(3);// взять все возможные
        System.out.println(Thread.currentThread().getName() + " acquire semaphore");
        System.out.println(Thread.currentThread().getName() + " release semaphore");
        semaphore.release(3);   // отдать взятые
    }

}

/**
 * Каждый поток пробует получить доступ
 * к ограниченной области
 */
class ChildRunnable implements Runnable{

    @Override
    public void run() {
        // пробуем получить доступ на одного
        if (SemaphoreShuffling.semaphore.tryAcquire()){
            // доступ дан, т.е. semaphore.acquire()
            System.out.println(Thread.currentThread().getName() + " acquire semaphore");
            System.out.println(Thread.currentThread().getName() + " release semaphore");
            SemaphoreShuffling.semaphore.release();  // обязательно отдать доступ, если он был получен
        } else {
            // не повезло
            System.out.println(Thread.currentThread().getName() + " didn't acquire semaphore");
        }
    }
}