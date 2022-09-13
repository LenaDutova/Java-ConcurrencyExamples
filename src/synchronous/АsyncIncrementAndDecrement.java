package synchronous;

/**
 * Данный пример демонстрирует "состязание" двух потоков
 * за результат разделяемой переменной а
 *
 * Осторожно нет синхронизации
 */
public class АsyncIncrementAndDecrement {

    private static int value = 0;
    private static int counter = 100;    // различия не заметны
//    private static int counter = 100_000;// ошибки очевидны

    public static void main(String[] args) throws InterruptedException {

        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < counter; i++) {
                    value++;
                }
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                value--;
            }
        });

        // Запускаем оба
        increment.start();
        decrement.start();

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        System.out.println(value);
    }
}
