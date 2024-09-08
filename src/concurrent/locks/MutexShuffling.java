package concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Данный пример демонстрирует
 * применение мьютекса (примитива синхронизации)
 * для осуществления единственности доступа
 *
 * пакета java.util.concurrent
 */
public class MutexShuffling {

    // блокировка повторного входа
    public static Lock mutex = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

//        // попробуем получить доступ до запуска дочерних потоков
//        mutex.lock();
//        System.out.println(Thread.currentThread().getName() + " lock mutex");

        // создадим и запустим кучку дочерних потоков
        new Thread(new TryLockRunnable()).start();
        new Thread(new TryLockRunnable()).start();
        new Thread(new TryLockRunnable()).start();
        new Thread(new TryLockRunnable()).start();
        new Thread(new TryLockRunnable()).start();

        // попробуем получить доступ после запуска дочерних потоков
        mutex.lock();
        System.out.println(Thread.currentThread().getName() + " lock mutex");

        System.out.println(Thread.currentThread().getName() + " unlock mutex");
        mutex.unlock();
    }
}

/**
 * Потоки пробующие получить доступ в ограниченную область:
 * - если вышло, то засыпают
 * - если нет, то просто завершают работу
 */
class TryLockRunnable implements Runnable{

    @Override
    public void run() {
        try {

            // пробуем получить доступ
            if (MutexShuffling.mutex.tryLock()){
                // доступ дан, т.е. mutex.lock()
                System.out.println(Thread.currentThread().getName() + " lock mutex");
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " unlock mutex");
                MutexShuffling.mutex.unlock();  // обязательно отдать доступ, если он был получен
            } else {
                // не повезло
                System.out.println(Thread.currentThread().getName() + " didn't lock mutex");
            }

        } catch (InterruptedException e) {
            System.out.println("child catch InterruptedException");
        }
    }
}
