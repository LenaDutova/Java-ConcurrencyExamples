package start;

import static java.lang.Thread.sleep;

/**
 * Данный пример демонстрирует
 * варианты создания дочерних потоков
 * (нитей исполнения)
 */
public class ThreadCreation {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " started");

        // создаем дочерний поток
        Thread childThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " started");

//                goToSleep(); // FIXME 1 : добавить задержку

                System.out.println(Thread.currentThread().getName() + " finished");
            }
        });
//        childThread.setDaemon(true); // FIXME 2 : изменить статус потока, до его запуска
        // запускаем его
        childThread.start();

        // дожидаемся окончания работы дочернего потока
//        if (childThread.isAlive()) childThread.join(); //throws InterruptedException

        System.out.println(Thread.currentThread().getName() + " finished");
    }

    protected static void goToSleep() {
        // методы, уводящие поток от "исполнения", должны быть помещены в блоки проверки,
        // чтобы иметь возможность обработать прерывания
        try {
            sleep(1000);
        } catch (InterruptedException e) { }
    }

    /**
     * Создаем некоторым образом и запускаем дочерний поток
     */
    private static void createThread(int variant){
        switch (variant) {
            case 0 : {
                // Создание нового потока через реализацию интерфейса
                // с указанием имени потока
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                // Или можно задать название потока методом setName()
                                Thread.currentThread().setName("child");

                                System.out.println(Thread.currentThread().getName() + " started");
                                System.out.println(Thread.currentThread().getName() + " finished");
                            }
                        },
                        "background").start();
                break;
            }
            case 1 : {
                // Если нить исполнения не требует параметров,
                // то метод run() можно описать лямбдой
                new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    System.out.println(Thread.currentThread().getName() + " finished");
                }).start();
                break;
            }
            case 2 : {
                // Создание и запуск анонимного потока
                new Thread() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + " started");
                        System.out.println(Thread.currentThread().getName() + " finished");
                    }
                }.start();
            }
        }
    }
}
