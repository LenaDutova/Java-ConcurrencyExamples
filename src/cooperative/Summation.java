package cooperative;

/**
 * Данный пример демонстрирует выигрыш
 * от совместной работы в решении задачи
 * над однопоточным режимом работы
 *
 * Осторожно нет синхронизации
 */
public class Summation {

    private static final int COUNT = 8;
    private static final long PART = 125_000_000; //125_000_000
    private static final long LIMIT = COUNT * PART; // миллиард 8х125'000'000

    private static long sumMany = 0;

    public static void main(String[] args) throws InterruptedException {
        oneWorker();
        manyWorkers();
    }

    /**
     * Главные поток сам вычисляет сумму чисел от нуля LIMIT
     * и отчитывается о времени выполнения
     */
    private static void oneWorker(){
        long time = System.currentTimeMillis();
        long sumOne = 0;
        for (int i = 0; i < LIMIT; i++) {
            sumOne += i;
        }
        System.out.println("sumOne =  " + sumOne);
        time = System.currentTimeMillis() - time;
        System.out.println("Main thread worked " + time + "ms and exited");
    }

    /**
     * Главный поток запускает четыре потока
     * и отдает им четыре промежутка суммирования
     * Результат работы выводится после того,
     * как все потоки отработали
     * @throws InterruptedException - ошибка прерывания потока, порождаемая методом join()
     */
    private static void manyWorkers() throws InterruptedException {
        long time = System.currentTimeMillis();


        Thread[] threads = new Thread[COUNT];
        SummationCallback callback = new SummationCallback() {
            @Override
            public void call(long part) {
                sumMany += part;
            }
        };

        // Создать и запустить
        for (int i = 0; i < COUNT; i++){
            threads[i] = new SummationThread(PART * i, PART * (i + 1), callback);
            threads[i].start();
        }

        // Подождать последнего или любого из выживших
        for (int i = COUNT - 1; i >= 0; i--){
            if (threads[i].isAlive()) {
                System.out.println("Wait thread-" + i );
                threads[i].join();
            }
        }

        System.out.println("sumMany = " + sumMany);
        time = System.currentTimeMillis() - time;
        System.out.println("All threads worked " + time + "ms and exited");
    }
}



interface SummationCallback {
    void call(long part);
}



class SummationThread extends Thread{

    private long a, b;
    private SummationCallback callback;

    public SummationThread(long a, long b, SummationCallback callback) {
        this.a = a;
        this.b = b;
        this.callback = callback;
    }

    @Override
    public void run() {
        //        long time = System.currentTimeMillis();

        long partOfSum = 0;
        for (long j = a; j < b; j++) {
            partOfSum += j;
        }
        callback.call(partOfSum);

        //        time = System.currentTimeMillis() - time;
        //        System.out.println(getName() + " worked " + time + "ms and exited");
    }
}
