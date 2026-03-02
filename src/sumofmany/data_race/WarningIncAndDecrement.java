package sumofmany.data_race;

/**
 * Данный пример демонстрирует "состязание" двух потоков
 * за результат разделяемой переменной value.
 * Результат определяет тот поток,
 * который записал результат в память последним.
 *
 * Осторожно нет синхронизации
 */
public class WarningIncAndDecrement {

    private static int value = 0;
    private static int n = 100;    // различия не заметны
//    private static int n = 100_000;// ошибки очевидны

    public static void main(String[] args) throws InterruptedException {
        // Поток, увеличивающий число на единицу
        Thread increment = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                value++;
            }
        });

        // Поток, столько же раз уменьшающий число на единицу
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                value--;
            }
        });

        long time = System.currentTimeMillis();

        // Запускаем оба
        increment.start();
        decrement.start();

        // Ожидаем окончания работы обоих
        if (decrement.isAlive()) decrement.join();
        if (increment.isAlive()) increment.join();

        System.out.println("Value: " + value);
        System.out.println("Time:  " + (System.currentTimeMillis() - time) + "ms");
    }
}
