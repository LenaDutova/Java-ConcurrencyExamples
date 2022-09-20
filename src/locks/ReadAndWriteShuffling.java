package locks;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Данный пример демонстрирует
 * разделяемый доступ одной группой (WriteLock)
 * и единственный доступ другой (ReedLock)
 *
 * пакета java.util.concurrent
 */
public class ReadAndWriteShuffling {
    public static ReadWriteLock rw = new ReentrantReadWriteLock();

    public static void main(String[] args) throws InterruptedException {

        // попробуем получить доступ на ЗАПИСЬ до запуска дочерних потоков
        rw.writeLock().lock();
        System.out.println(Thread.currentThread().getName() + " lock mutex");

        // создадим и запустим кучку дочерних потоков
        new Thread(new ReadRunnable()).start();
        new Thread(new ReadRunnable()).start();
        new Thread(new ReadRunnable()).start();
        new Thread(new ReadRunnable()).start();
        new Thread(new ReadRunnable()).start();

//        // попробуем получить доступ на ЗАПИСЬ после запуска дочерних потоков
//        rw.writeLock().lock();
//        System.out.println(Thread.currentThread().getName() + " lock mutex");

        System.out.println(Thread.currentThread().getName() + " unlock mutex");
        rw.writeLock().unlock();    // обязательно отдать доступ
    }


}

class ReadRunnable implements Runnable{

    @Override
    public void run() {
        try {

            // пробуем получить доступ на ЧТЕНИЕ
            if (ReadAndWriteShuffling.rw.readLock().tryLock()){
                // доступ дан, т.е. mutex.lock()
                System.out.println(Thread.currentThread().getName() + " lock mutex");
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " unlock mutex");
                ReadAndWriteShuffling.rw.readLock().unlock();   // обязательно отдать доступ
            } else {
                // не повезло
                System.out.println(Thread.currentThread().getName() + " didn't lock mutex");
            }

        } catch (InterruptedException e) {
            System.out.println("child catch InterruptedException");
        }
    }
}
