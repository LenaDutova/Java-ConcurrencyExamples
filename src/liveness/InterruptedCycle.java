package liveness;

/**
 * Данный пример демонстрирует
 * прерывание исполняемого метода потока
 */
public class InterruptedCycle {
    public static void main(String[] args) throws InterruptedException {
        InterruptedThread child = new InterruptedThread();
        child.start();

        //1
        Thread.sleep(10);
        child.interrupt();

        //2
        Thread.sleep(100);
        child.interrupt();

        //3
        Thread.sleep(10);
        child.interrupt();

    }
}

class InterruptedThread extends Thread{

    @Override
    public void run() {
        System.out.println(1 + "-я фаза");
        System.out.println("isInterrupted = " + isInterrupted());

        // Пока не прервут, выводи а
        while (!isInterrupted()){
            System.out.print("a");
        }

        System.out.println(System.lineSeparator() + "isInterrupted = " + isInterrupted());
        interrupted(); // сбить флаг прерывания и продолжить работу
        System.out.println("isInterrupted = " + isInterrupted());



        System.out.println(2 + "-я фаза");
        // Поспим, пока не прервут
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            // При прерывании методов wait, sleep, join
            // флаг прерывания остается false
            System.out.println("isInterrupted = " + isInterrupted());
        }



        System.out.println(3 + "-я фаза");
        // Пока не прервут, выводи b
        while (!isInterrupted()){
            System.out.print("b");
        }

        System.out.println(System.lineSeparator() + "isInterrupted = " + isInterrupted());
    }
}
