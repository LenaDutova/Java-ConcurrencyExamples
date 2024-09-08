package executors;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Данный пример демонстрирует
 * возможность синхронной/асинхронной работы,
 *
 * пакета java.util.concurrent
 * since 1.7
 */
public class ForkJoinExample {
    protected static final long PART =    100;
    protected static final long LIMIT = 10_000;

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        ForkJoinTask task = new SummationTask(0, LIMIT);
        System.out.println(Thread.currentThread() + " result " + pool.invoke(task));

        // без запуска в пуле, будет работать на главном
//        task.fork();
//        System.out.println(Thread.currentThread() + " result " + task.join());
    }
}

class SummationTask extends RecursiveTask<Long> {

    private long a, b; // промежуток суммирования
    public SummationTask(long a, long b) {
        System.out.println("New task on range[" + a + "," + b + "]");
        this.a = a;
        this.b = b;
    }

    @Override
    protected Long compute() {
        long result = 0;

        // частный случай, когда мы можем решить задачку сами
        if (b - a <= ForkJoinExample.PART){
            for (long i = a; i < b; i++) {
                result += i;
            }

            System.out.println(Thread.currentThread() + " small part = " + result + " on range[" + a + "," + b + "]");
            return result;
        }

        // иначе разбить задачу на 10 кусочков
        SummationTask[] tasks = new SummationTask[10];
        for (int i = 0; i < 10; i++){
            tasks[i] = new SummationTask(i * (b-a) / 10, (i + 1) * (b-a) / 10);
            tasks[i].fork(); // асинхронно все запустить
        }
        // асинхронно все запустить
//        ForkJoinTask.invokeAll(tasks);
        // синхронно собрать все
        for (int i = 0; i < 10; ){
           result += tasks[i].join();
        }

        System.out.println(Thread.currentThread() + " big part = " + result + " on range[" + a + "," + b + "]");
        return result;
    }
}
