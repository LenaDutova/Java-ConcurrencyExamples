/**
 * Данный пример демонстрирует
 * варианты создания дочерних потоков
 * (нитей исполнения)
 */
public class ThreadCreation {

    public static void main(String[] args) throws InterruptedException {
        String mainThreadName = Thread.currentThread().getName();
        printStatus(mainThreadName, true);

        // создаем дочерний поток
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String childThreadName = Thread.currentThread().getName();
                printStatus(childThreadName, true);
                printStatus(childThreadName, false);
            }
        });
        // запускаем его
        backgroundThread.start();

        // дожидаемся окончания работы дочернего потока
//        if (backgroundThread.isAlive()) backgroundThread.join(); //throws InterruptedException

        printStatus(mainThreadName, false);
    }

    public static void printStatus (String name, boolean status){
        System.out.println(name + " " + (status ? "started" : "finished"));
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
                                // можно задать название потока методом setName()
                                Thread.currentThread().setName("child");

                                String name = Thread.currentThread().getName();
                                printStatus(name, true);
                                printStatus(name, false);
                            }
                        },
                        "background").start();
                break;
            }
            case 1 : {
                // Если нить исполнения не требует параметров,
                // то метод run() можно описать лямбдой
                new Thread(() -> {
                    printStatus(Thread.currentThread().getName(), true);
                    printStatus(Thread.currentThread().getName(), false);
                }).start();
                break;
            }
            case 2 : {
                // Создание и запуск анонимного потока
                new Thread() {
                    @Override
                    public void run() {
                        printStatus(Thread.currentThread().getName(), true);
                        printStatus(Thread.currentThread().getName(), false);
                    }
                }.start();
            }
        }
    }
}
