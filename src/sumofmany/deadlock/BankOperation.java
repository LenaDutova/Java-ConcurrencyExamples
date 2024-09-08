package sumofmany.deadlock;

import static sumofmany.deadlock.BankOperation.*;

/**
 * Разное написание synchronized, не отменяет механизма его использования:
 * всегда идет проверка состояния синхронизируемого объекта
 *
 * Осторожно, взаимоблокировка
 */
public class BankOperation {

    private static final int counter = 10;
    private static final int value = 10;

    private static BankAccount annAccount = new BankAccount("Ann", 100);
    private static BankAccount bobAccount = new BankAccount("Bob", 100);

    public static void main(String[] args) throws InterruptedException {

        Thread threadAnn = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                transfer(annAccount, bobAccount, value);
            }
        });

        Thread threadBob = new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                transfer(bobAccount, annAccount, value);
            }
        });

        threadAnn.start();
//        threadBob.start();

        // Ожидаем окончания работы обоих
        if (threadAnn.isAlive()) threadAnn.join();
        if (threadBob.isAlive()) threadBob.join();

        System.out.println("FINISH");
        annAccount.show();
        bobAccount.show();
    }

    /**
     * Перевод некоторой суммы денег с одного аккаунта на другой
     * @param from
     * @param to
     * @param value
     */
    public static boolean transfer(BankAccount from, BankAccount to, int value){
        if (value < 0) return false;

        synchronized (from){
            if (from.removeMoney(value))
                if (to.putMoney(value)) {
                    return true;
                } else from.putMoney(value);
            return false;
        }
    }
}

/**
 * Защищенный класс с помощью synchronized
 */
class BankAccount {
    private int balance;
    private String name;

    public BankAccount(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public void show (){
        System.out.println(name + " balance is " + balance);
    }

    public synchronized boolean putMoney (int money){
        if (money >= 0) {
            balance += money;
            show();
            return true;
        }
        return false;
    }

    public synchronized boolean removeMoney (int money){
        if (balance >= money ) {
            balance -= money;
            show();
            return true;
        }
        return false;
    }
}
