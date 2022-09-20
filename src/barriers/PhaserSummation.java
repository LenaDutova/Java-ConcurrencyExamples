package barriers;

import java.util.concurrent.Phaser;

import static barriers.PhaserSummation.sleepTime;
import static barriers.PhaserSummation.synchronization;

/**
 * Данный пример демонстрирует
 * синхронизацию стадий выполнения общего действия
 *
 * пакета java.util.concurrent
 */
public class PhaserSummation {

    // в конструкторе можно сразу указать
    // число подписанных потоков
    // или подписаться в дальнейшем
    protected static Phaser synchronization = new Phaser();
    protected static int sleepTime = 100;

    public static void main(String[] args) {

        new SummationPhaserThread(0, 1_000, 1_000_000).start();
        new SummationPhaserThread(0, 999_000, 1_000_000).start();

        // подписываемся на синхронизацию работы и получаем номер текущей фазы
        int phase = synchronization.register();

        // ждем завершения фазы 0
        synchronization.arriveAndAwaitAdvance(); // сообщаем, что фаза достигнута и ждем остальных
        System.out.println("Фаза " + phase + " завершена");

        // ждем завершения фазы 1
        phase = synchronization.getPhase();
        synchronization.arriveAndAwaitAdvance(); // сообщаем, что фаза достигнута и ждем остальных
        System.out.println("Фаза " + phase + " завершена");

        // ждем завершения фазы 2
        phase = synchronization.getPhase();
        synchronization.arriveAndAwaitAdvance(); // сообщаем, что фаза достигнута и ждем остальных
        System.out.println("Фаза " + phase + " завершена");

//        // ждем завершения фазы (добавочная)
//        phase = synchronization.getPhase();
//        synchronization.arriveAndAwaitAdvance(); // сообщаем, что первая фаза достигнута и так как кроме нас никого нет, сразу идем дальше
//        System.out.println("Фаза " + phase + " завершена");

        // сообщаем о завершении синхронизации и отписываемся (на фазе 3)
        System.out.println("Фаза " + synchronization.arriveAndDeregister() + " отмена регистрации");
    }
}

/**
 * Поток выполняет некоторую работу в каждой фазе,
 * следующую фазу начинает, как только подтянуться все остальные
 */
class SummationPhaserThread extends Thread{

    private int a, b, c;

    public SummationPhaserThread(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void run(){
        int result = 0;

        // подписываемся на синхронизацию
        System.out.println(getName() + " фаза " + synchronization.register() + " - регистрация");


        try {
            Thread.sleep(sleepTime);
        } catch(InterruptedException ex){}
        result += summation(a, b); // имитация полезной работы


        // завершили фазу 0
        synchronization.arriveAndAwaitAdvance(); // сообщаем, что фаза достигнута и "работаем" дальше


        try {
            Thread.sleep(sleepTime); // имитация полезной работы
        } catch(InterruptedException ex){}
        System.out.println(getName() + " фаза " + synchronization.getPhase() + " - result = " + result);
        result += summation(b, c);


        // завершили фазу 1
        synchronization.arriveAndAwaitAdvance(); // сообщаем, что фаза достигнута, ждем остальных и "работаем" дальше


        // сообщаем о завершении синхронизации и отписываемся
        System.out.println(getName() + " фаза " + synchronization.arriveAndDeregister() + " - отмена регистрации - result = " + result);
    }

    private int summation (int x, int y){
        int result = 0;
        for (int i = x; i < y; i++) {
            result += i;
        }
        return result;
    }
}
