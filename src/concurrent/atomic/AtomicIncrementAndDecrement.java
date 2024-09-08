package concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Данный пример демонстрирует
 * использование разделяемых переменных
 * с атомарными сеттерами
 *
 * пакета java.util.concurrent
 */
public class AtomicIncrementAndDecrement {

    private static AtomicInteger value = new AtomicInteger(0);
    private static int counter = 500_000;

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();

        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(() -> {
            for (int i = 0; i < counter; i++) {

                value.incrementAndGet(); // префиксный инкремент
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                value.decrementAndGet(); // префиксный декремент
            }
        });

        // Запускаем оба
        increment.start();
        decrement.start();

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        System.out.println("Result Value " + value.get());
        System.out.println("Task worked  " + (time = System.currentTimeMillis() - time) + "ms");
    }
}
