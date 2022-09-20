package liveness;

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

    /**
     * Главный поток:
     * 1) сообщает что он жив
     * 2) запускает дочерний
     * 3) дрыхнет
     * 4) получает доступ к разделяемой переменной
     * 3.1) дрыхнет
     * 3.2) изменяет разделяемую переменную
     * 3.3) будет связанные с ней потоки
     * 4) ждет завершения дочернего
     */
    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setName(Thread.currentThread().getName().toUpperCase() + " ");
        System.out.println(Thread.currentThread().getName() + " - RUNNABLE"); // уже работает, т.к. программа запущена в этом потоке

        // Создаем разделяемый ресурс, дочерний поток и стартуем его
        Resource resource = new Resource();
        ChildThread child = new ChildThread("\tchild", resource);
        child.start();


        // Сон, чтобы дочерний поток ушел в ожидание
        System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING start"); // когда ждем чего-то по времени
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING finish");


        System.out.println(Thread.currentThread().getName() + " - BLOCKED"); // пока не получит монитор, защищающий ресурс
        synchronized (resource){
            System.out.println(Thread.currentThread().getName() + " - RUNNABLE after BLOCKED");


//            System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING start");
//            Thread.sleep(1000); // не отпускает монитор
//            System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING finish");


            // поднять флаг
            resource.up();

            // сон при удержании монитора
            System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING start"); // когда ждем чего-то по времени
            Thread.sleep(1000); // не отпускает монитор
            System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING finish");
        }

//        System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING start"); // когда ждем чего-то по времени
//        Thread.sleep(1000);
//        System.out.println(Thread.currentThread().getName() + " - TIMED_WAITING finish");

        if (child.isAlive()){
            System.out.println(Thread.currentThread().getName() + " - WAITING on join "); // когда ждем наступления события
            child.join();
        }
    }
}

/**
 * Дочерний поток:
 * 1) Получает доступ к дочерней переменной
 * 1.1) Если флаг отрицательный - уходит в ожидание
 * 2) завершается
 */
class ChildThread extends Thread{

    private Resource resource;

    public ChildThread(String name, Resource resource) {
        super(name);
        this.resource = resource;
        System.out.println(getName() + " - NEW");
    }

    @Override
    public void run() {
        System.out.println(getName() + " - RUNNABLE"); // {@link Thread.State#NEW} A thread executing in the Java virtual machine is in this state.

        try {
            System.out.println(getName() + " - BLOCKED");
            synchronized (resource){
                System.out.println(getName() + " - RUNNABLE after BLOCKED");

                // взять флаг
                resource.down();
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " - INTERRUPTED");
        }

        System.out.println(getName() + " - TERMINATED"); // {@link Thread.State#TERMINATED} A thread that has exited is in this state.
    }
}

/**
 * Некий разделяемый ресурс
 */
class Resource {
    private boolean flag = false;

    public synchronized void down() throws InterruptedException {
        if (flag == false) {
            System.out.println(Thread.currentThread().getName() + " - WAITING start"); // когда ждем наступления события
            wait();
            System.out.println(Thread.currentThread().getName() + " - WAITING finish"); // когда ждем наступления события
        }
        flag = false;
    }

    public synchronized void up(){
        flag = true;
        System.out.println(Thread.currentThread().getName() + " - NOTIFY");
        notify();
    }
}
