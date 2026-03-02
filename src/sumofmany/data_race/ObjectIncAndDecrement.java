package sumofmany.data_race;

/**
 * Данный пример демонстрирует "состязание" двух потоков
 * за результат разделяемой переменной value
 */
public class ObjectIncAndDecrement {

    private static SynchronizedObjectValue value = new SynchronizedObjectValue();
    private static int counter = 100_000;    // различия были заметны

    public static void main(String[] args) throws InterruptedException {
        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                synchronized (value){
                    value.increment();
                }
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                synchronized (value){
                    value.decrement();
                }
            }
        });

        long time = System.currentTimeMillis();

        // Запускаем оба
        increment.start();
        decrement.start();

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        /* FIXME: Warning! Если объект разделяемый,
        * вы не можете гарантировать,
        * что его не будут использовать без синхронизации */
//        Thread.currentThread().setName("IGIL"); // главный поток - тоже участвует
//        value.setValue(new Random().nextInt());

        System.out.println("Value: " + value.getValue());
        System.out.println("Time:  " + (System.currentTimeMillis() - time) + "ms");
    }
}

class SynchronizedObjectValue {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increment (){
        value++;
    }
    public void decrement (){
        value--;
    }
}