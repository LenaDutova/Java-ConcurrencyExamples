package executors;

import java.util.concurrent.*;

/**
 * Данный пример демонстрирует
 * нивелирование запуска потоков
 * при выполнении задач
 *
 * пакета java.util.concurrent
 */
public class ExecutorsExample {

    // некий набор фоновых действий,
    // результат которых нам не принципиален
    static Runnable procedure = () -> {
        for (int i = 0; i <= 50; i += 10) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    };

    // некий набор фоновых действий,
    // результат которых мы хотим получить
    // возможны исключительные ситуации
    static Callable<Integer> function = () -> {
        int result = 0;
        for (int i = 0; i <= 50; i += 10) {
            result += i;
            System.out.println(Thread.currentThread().getName() + " " + result);
        }
        return result;
    };

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // вручную запускаем поток и метод в нем
        withoutExecutors();

        // запуск с помощью пакета java.util.concurrent
//        withExecutors();
    }

    /**
     * Пример использования готовых фабрик Исполнителей
     * для фоновых задач. В них поток будет
     * удерживаться до получения новой задачи или
     * закрытия фабрики
     */
    private static void withExecutors() throws ExecutionException, InterruptedException {
        ExecutorService service = null;

//        service = Executors.newSingleThreadExecutor();  // создать один фоновый поток, который получит задачу
        service = Executors.newFixedThreadPool(10); // создать несколько фоновых потоков, которые будут ожидать задачи

        // можно повторять запуск процедуры,
        // но потоки не перезапускаются
        System.out.println("Запуск процедуры через фабричный метод Исполнителя");
        service.execute(procedure);
        service.execute(procedure);


        // выполнение фоновых задач можно дождаться
//        System.out.println("Запуск процедуры через фабричный метод Исполнителя");
//        Future procedureFuture = service.submit(procedure); // не execute(), а submit()
//        procedureFuture.get(); // аналог классического метода потоков - join()
//        System.out.println("С удержанием главного потока");


        // а можно и получить результат, но нужен не Runnable, а Callable
//        System.out.println("Запуск функции через фабричный метод Исполнителя с удержанием главного потока");
//        Future future = service.submit(function);
//        System.out.println("Is done? - " + future.isDone()); // уже посчитал?
//        System.out.println("Is done? - " + future.isDone()); // а сейчас?
//        System.out.println(future.get());    // get() - дождись результат функции и верни его
//        System.out.println("Is done? - " + future.isDone()); // а теперь?

        // TODO: обязаны закрыть, иначе программа не завершится
        service.shutdown();
    }

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
