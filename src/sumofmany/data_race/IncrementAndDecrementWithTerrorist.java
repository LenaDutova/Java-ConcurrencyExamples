package sumofmany.data_race;

import java.util.Random;

public class IncrementAndDecrementWithTerrorist {

    private static AnotherValue value = new AnotherValue();
    private static int counter = 500_000;    // различия были заметны

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();

        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                value.increment();
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                value.decrement();
            }
        });

        // Запускаем оба
        increment.start();
        decrement.start();

        // FIXME: Warning! Если объект разделяемый,
        // то все его сеттеры должны синхронизироваться
//        Thread.currentThread().setName("IGIL"); // главный поток - тоже участвует
//        value.setValue(new Random().nextInt());

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        System.out.println("Result Value " + value.getValue());
        System.out.println("Task worked  " + (time = System.currentTimeMillis() - time) + "ms");
    }

}

class AnotherValue {

    private int value;

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    public synchronized void increment (){
        value++;
    }
    public synchronized void decrement (){
        value--;
    }
}