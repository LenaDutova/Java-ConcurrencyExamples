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
     * Главный поток:
     * 1) сообщает что он жив;
     * 2) запускает дочерний поток, и опрашивает его статус;
     * 2.1) дрыхнет и спрашивает статус дочернего (1 или два раза);
     * 3) получает доступ к разделяемой переменной;
     * 3.1) изменяет разделяемую переменную и будит связанные с ней потоки;
     * 3.2) а сам дрыхнет, если требуется;
     * 4) ждет завершения дочернего.
     */
    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setName(Thread.currentThread().getName().toUpperCase() + "     ");

        // RUNNABLE - уже работает, т.к. программа запущена
        System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - " + Thread.currentThread().getState().name());


        // Создаем разделяемый ресурс, дочерний поток и стартуем его
        Resource resource = new Resource();
        ChildThread child = new ChildThread("    child", resource, Thread.currentThread());

        // NEW - запросили, но еще не запустили
        System.out.println(new Date() + " - " + child.getName().toUpperCase() + " - " + child.getState().name());
        child.start();  // Стартуем дочерний поток
        Thread.sleep(TIME_INTERVAL); // Сон, чтобы дочерний поток тоже заснул

        // TIMED_WAITING, когда ждем чего-то по времени, например пока поток поспит заданное время
        System.out.println(new Date() + " - " + child.getName().toUpperCase() + " - " + child.getState().name() + " - on sleep");

        // region FIXME 1: отбросить ожидание того, что дочерний поток уйдет в ожидание
        Thread.sleep(2 * TIME_INTERVAL); // Сон, чтобы убедиться, что дочерний заблокирован на операции с ресурсом
        // WAITING, когда ожидается событие, например окончание работы другого потока или сами ушли в ожидание
        System.out.println(new Date() + " - " + child.getName().toUpperCase() + " - " + child.getState().name());
        // endregion FIXME1

        synchronized (resource){
            System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - resource up");
            resource.up();  // поднять флаг ресурса и освободить ожидающий поток

//            Thread.sleep(5 * TIME_INTERVAL); // FIXME 2: сон при удержании монитора не отпускает монитор

            // BLOCKED, блокировка, например пока не получит монитор, защищающий ресурс
            System.out.println(new Date() + " - " + child.getName().toUpperCase() + " - " + child.getState().name());
            System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - finish use resource");
        }

        if (child.isAlive()){
            child.join();
        }
        System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - TERMINATED"); // по правде все еще RUNNABLE, но это наиболее близкая точка к завершению
    }
}

/**
 * Дочерний поток:
 * 1) спит немного
 * 2) получает доступ к дочерней переменной
 * 2.1) если флаг отрицательный - уходит в ожидание
 * 3) завершается
 */
class ChildThread extends Thread{

    private Resource resource;
    private Thread parent;

    public ChildThread(String name, Resource resource, Thread parent) {
        super(name);
        this.resource = resource;
        this.parent = parent;
        // Thread.currentThread().getName() вернет родительский поток,
        // так как это еще подготовка к запуску, а не запуск самого потока
    }

    @Override
    public void run() {
        // RUNNABLE - только после вызова start()
        System.out.println(new Date() + " - " + getName() + " - " + Thread.currentThread().getState().name());

        try {
            System.out.println(new Date() + " - " + getName() + " - go to sleep ()");
            sleep(2 * TIME_INTERVAL); // сон на 2 секунды
            System.out.println(new Date() + " - " + getName() + " - wake up");

            System.out.println(new Date() + " - " + getName() + " - go to resource");
            synchronized (resource) {
                resource.down(); // взять флаг
            }
            System.out.println(new Date() + " - " + getName() + " - use resource");
        } catch (InterruptedException e) { }

        // когда ждем наступления события
        System.out.println(new Date() + " - " + parent.getName() + " - " + parent.getState().name() + " - on join ");
        System.out.println(new Date() + " - " + getName() + " - TERMINATED"); // по правде все еще RUNNABLE, но это наиболее близкая точка к завершению
    }
}

/**
 * Некий разделяемый ресурс
 */
class Resource {
    private boolean flag = false;

    public synchronized void down() throws InterruptedException {
        if (flag == false) {
            System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - start wait()"); // ждем наступления события
            wait();
            System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - waiting finished"); // были пробужены кем-то
        }
        flag = false;
    }

    public synchronized void up(){
        if (flag == false) {
            flag = true;
            System.out.println(new Date() + " - " + Thread.currentThread().getName() + " - notify() waiting threads");
            notify();
        }
    }
}
