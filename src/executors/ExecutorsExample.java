package executors;

import java.util.concurrent.*;

/**
 * Данный пример демонстрирует
 * нивелирование запуска потоков
 * при выполнении задач
 *
 * пакета java.util.concurrent
 * since 1.5
 */
public class ExecutorsExample {

    static int N = 5;

    // некий набор фоновых действий,
    // результат которых нам не принципиален
    static Runnable procedure = () -> {
        for (int i = 0; i <= N; i ++) {
            System.out.println(i + "-" + Thread.currentThread().getName() + ": Hello");
        }
    };

    // некий набор фоновых действий,
    // результат которых мы хотим получить
    // возможны исключительные ситуации
    static Callable<Integer> function = () -> {
        int result = 0;
        for (int i = 0; i <= N; i ++) {
            result += i;
            System.out.println(i + "-" + Thread.currentThread().getName() + "-" + result);
        }
        return result;
    };

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        withoutExecutors(); // вручную запускаем поток и метод в нем
        withExecutors();  // запуск с помощью пакета java.util.concurrent
    }

    /**
     * Пример использования готовых фабрик Исполнителей
     * для фоновых задач. В них поток будет
     * удерживаться до получения новой задачи или
     * закрытия фабрики
     */
    private static void withExecutors() throws ExecutionException, InterruptedException {


        ExecutorService service = null;
        service = Executors.newSingleThreadExecutor(); // создать пул с одним фоновым потоком
//        service = Executors.newFixedThreadPool(10); // создать пул на несколько фоновых потоков

        // можно повторять запуск процедуры, но потоки не перезапускаются
        service.execute(procedure);
        service.execute(procedure);


        // выполнение фоновых задач можно дождаться
        Future procedureFuture = service.submit(procedure); // не execute(), а submit()
        procedureFuture.get(); // аналог классического метода потоков - join()
        System.out.println("С удержанием главного потока");


        // а можно и получить результат, но нужен не Runnable, а Callable
        System.out.println("Запуск функции через фабричный метод Исполнителя с удержанием главного потока");
        Future future = service.submit(function);
        System.out.println("Is done? - " + future.isDone()); // уже посчитал?
        System.out.println("Is done? - " + future.isDone()); // а сейчас?
        System.out.println(future.get());    // get() - дождись результат функции и верни его
        System.out.println("Is done? - " + future.isDone()); // а теперь?

        // TODO: обязаны закрыть, иначе программа не завершится
        service.shutdown();
    }

    /**
     * Пример создания пула потоков без использования фабричных методов
     */
    private static void withoutExecutors(){
        // в поток легко "прокинуть" процедуру
        System.out.println("Классический запуск процедуры в исполняемом методе потока");
        Thread thread = new Thread(procedure);
        thread.start();

        // ручной запуск через интерфейс Исполнителя потока
        System.out.println("Запуск процедуры через интерфейс Исполнителя");
        Executor executor = (runnable) -> {
            new Thread(runnable).start();
        };
        executor.execute(procedure); // можно несколько раз отправить задачу на исполнение (в новый поток)
        executor.execute(procedure); // можно несколько раз отправить задачу на исполнение (в новый поток)
    }
}
