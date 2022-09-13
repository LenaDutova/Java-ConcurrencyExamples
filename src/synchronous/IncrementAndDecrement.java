package synchronous;

/**
 *
 */
public class IncrementAndDecrement {

    private static Value value = new Value();
    private static int counter = 500_000;    // различия были заметны
    private static Mode mode = Mode.ASYNC;

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();

        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < counter; i++) {
                    switch (mode){
                        case ASYNC -> value.increment();
                        case SYNC_METHOD -> value.synchronizedIncrement();
                        case SYNC_OBJECT -> {
                            synchronized (value){
                                value.increment();
                            }
                        }
                    }
                }
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                switch (mode){
                    case ASYNC -> value.decrement();
                    case SYNC_METHOD -> value.synchronizedDecrement();
                    case SYNC_OBJECT -> {
                        synchronized (value){
                            value.decrement();
                        }
                    }
                }
            }
        });

        // Запускаем оба
        increment.start();
        decrement.start();

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        System.out.println(value.getValue());
        time = System.currentTimeMillis() - time;
        System.out.println("Task worked " + time + "ms on " + mode);
    }


}

enum Mode {
    ASYNC, SYNC_OBJECT, SYNC_METHOD
}

class Value {
    private int value;

    public int getValue() {
        return value;
    }

    public void increment (){
        value++;
    }

    public void decrement (){
        value--;
    }


    public synchronized void synchronizedIncrement (){
        increment();
    }

    public synchronized void synchronizedDecrement (){
        decrement();
    }
}