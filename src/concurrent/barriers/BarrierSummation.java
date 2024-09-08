package concurrent.barriers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static concurrent.barriers.BarrierSummation.sleepTime;
import static concurrent.barriers.BarrierSummation.synchronization;

/**
 * Данный пример демонстрирует
 * синхронизацию стадий выполнения общего действия
 * через примитив синхронизации - барьер
 *
 * пакета java.util.concurrent
 */
public class BarrierSummation {

    // Количество ожидающих потоков задается обязательно,
    // изменить нельзя - только создав новый
    protected static CyclicBarrier synchronization = new CyclicBarrier(3);
    protected static int sleepTime = 1000;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException, TimeoutException {

        new SummationBarrierThread(0, 1_000, 1_000_000).start();
        new SummationBarrierThread(0, 999_000, 1_000_000).start();

        // подписываемся на синхронизацию работы и получаем номер текущей фазы
        int phase = 0;

        // ждем завершения фазы 0
        synchronization.await(); // фаза достигнута - ждем остальных
        System.out.println("Фаза " + phase + " завершена");

        // ждем завершения фазы 1
        synchronization.await(); // фаза достигнута - ждем остальных
        System.out.println("Фаза " + ++phase + " завершена");

        // ждем завершения фазы 2
        int count = synchronization.await(sleepTime, TimeUnit.MILLISECONDS); // немного подождем
        System.out.println("Фаза " + ++phase + " завершена");
        synchronization.reset();

    }

}

/**
 * Поток выполняет некоторую работу в каждой фазе,
 * следующую фазу начинает, как только подтянуться все остальные
 */
class SummationBarrierThread extends Thread{

    private int a, b, c;

    public SummationBarrierThread(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void run(){
        int result = 0;

        System.out.println(getName() + " фаза 0");
        try {
            Thread.sleep(sleepTime);
        } catch(InterruptedException ex){}
        result += summation(a, b); // имитация полезной работы

        // завершили фазу 0
        try {
            synchronization.await(); // сообщаем, что фаза достигнута и "работаем" дальше
            Thread.sleep(sleepTime); // имитация полезной работы
        } catch (InterruptedException e) {}
        catch (BrokenBarrierException e) {}

        System.out.println(getName() + " фаза 1 - result = " + result);
        result += summation(b, c);

        // завершили фазу 1
        try {
            synchronization.await(); // сообщаем, что фаза достигнута и "работаем" дальше
        } catch (InterruptedException e) {}
        catch (BrokenBarrierException e) {}
        System.out.println(getName() + " фаза 2 - result = " + result);

        // на фазе 2 исполняемый метод завершается
    }

    private int summation (int x, int y){
        int result = 0;
        for (int i = x; i < y; i++) {
            result += i;
        }
        return result;
    }
}