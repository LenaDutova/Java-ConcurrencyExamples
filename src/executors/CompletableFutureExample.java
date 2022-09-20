package executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Данный пример демонстрирует
 * возможность асинхронной работы,
 * скрывающей запуск фонового потока
 *
 * пакета java.util.concurrent
 * since java 1.8
 */
public class CompletableFutureExample {

    // некий набор фоновых действий,
    // результат которых нам не принципиален
    static Runnable procedure = () -> {
        for (int i = 0; i <= 50; i += 10) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    };

    // некий набор фоновых действий,
    // результат которых мы хотим получить
    // не Callable, a Supplier
    static Supplier<Integer> function = () -> {
        int result = 0;
        for (int i = 0; i <= 50; i += 10) {
            result += i;
            System.out.println(Thread.currentThread().getName() + " " + result);
        }

        if (result < 100) throw new NullPointerException();

        return result;
    };

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // асинхронный запуск процедуры
//        CompletableFuture<Void> future1 = CompletableFuture.runAsync(procedure);

//        System.out.println(Thread.currentThread().getName() + " не жду");
//        future1.get();// get() - НО дождись окончания процедуры
//        System.out.println(Thread.currentThread().getName() + " подождал");

        // асинхронный запуск функции, которая возвращает результат
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(function);

        // преобразовать результат
        CompletableFuture<String> future3 = future2.thenApply(new Function<Integer, String>() {
            @Override
            public String apply(Integer result) {
                return " result is " + result;
            }
        });

        // обработать результат
        CompletableFuture<Void> future4 = future3.thenAccept(new Consumer<String>() {
            @Override
            public void accept(String result) {
                System.out.println(Thread.currentThread().getName() + result);
            }
        });


        // ожидание до вычисления результата
        if (!future4.isDone()){
            future4.get();
        }
    }
}
