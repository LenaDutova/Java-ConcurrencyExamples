package sumofmany.data_race;


public class MethodIncAndDecrement {

    private static SynchronizedMethodValue value = new SynchronizedMethodValue();
    private static int counter = 100_000;    // различия были заметны

    public static void main(String[] args) throws InterruptedException {
        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
//                synchronized (value) { // можно "перезахватывать" удерживаемый вами монитор
                    value.increment();
//                }
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                value.decrement();
            }
        });

        long time = System.currentTimeMillis();

        // Запускаем оба
        increment.start();
        decrement.start();

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        System.out.println("Value: " + value.getValue());
        System.out.println("Time:  " + (System.currentTimeMillis() - time) + "ms");
    }

}

class SynchronizedMethodValue {

    private int value;

    public int getValue() {
        return value;
    }

    public synchronized void increment (){
        value++;
    }
    public synchronized void decrement (){
        value--;
    }
}