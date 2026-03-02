package start.liveness;

import java.util.Date;

import static start.liveness.LifeCycle.*;

/**
 * Данный пример демонстрирует
 * стадии жизненного цикла потоков
 *
 * {@link Thread.State#NEW} Thread state for a thread which has not yet started.
 * {@link Thread.State#RUNNABLE} A thread executing in the Java virtual machine is in this state.
 * {@link Thread.State#BLOCKED} Thread state for a thread blocked waiting for a monitor lock
 * {@link Thread.State#TIMED_WAITING} Thread state for a waiting thread with a specified waiting time
 * {@link Thread.State#WAITING} A thread in the waiting state is waiting for another thread to perform a particular action
 */
public class LifeCycle {

    protected static final int TIME_INTERVAL = 1000;

    /**
     * Дочерний поток спит немного, потом опускает флаг
     * Главный поток запускает дочерний поток,
     * и опрашивает его статус до и после подъема флага.
     */
    public static void main(String[] args)
            throws InterruptedException {

        Flag resource = new Flag();

        Thread child = new Thread(()->{
            try {
                Thread.sleep(2 * TIME_INTERVAL);
                resource.down(); // взять флаг
            } catch (InterruptedException e) { }
        });

        System.out.println(child.getState().name());
        // NEW - запросили, но еще не запустили

        child.start();              // Стартуем дочерний поток
        System.out.println(child.getState().name());
        // RUNNABLE - сразу после вызова start(), даже если не исполняется

        Thread.sleep(TIME_INTERVAL); // Дочерний поток спит
        System.out.println(child.getState().name());
        // TIMED_WAITING - когда ждем чего-то по времени (sleep, wait, join)

        Thread.sleep(2 * TIME_INTERVAL); // Дочерний выспался, не смог опустить флаг и заблокирован
        System.out.println(child.getState().name());
        // WAITING - когда ожидается событие без тайминга (wait, join)

        resource.up();
        System.out.println(child.getState().name());
        // BLOCKED - если не успел схватить монитор обратно

        Thread.sleep(TIME_INTERVAL); // Дочерний поток закончил работу
        System.out.println(child.getState().name());
        // TERMINATED - когда поток завершился или погиб
    }
}

/**
 * Некий разделяемый ресурс
 */
class Flag {
    private boolean flag = false;

    // Опустить можно только поднятый флаг, иначе ждать
    public synchronized void down() throws InterruptedException {
        if (flag == false) {
            wait();
        }
        flag = false;
    }

    // "Разбудить" ждущие потоки после подъема флага
    public synchronized void up(){
        if (flag == false) {
            flag = true;
            notify();
        }
    }
}
