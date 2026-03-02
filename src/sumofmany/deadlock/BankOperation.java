package sumofmany.deadlock;

/**
 * Для работы операции transfer()
 * будут блокироваться два объекта,
 * но по очереди. При взаимной пересылке денег
 * может получиться ситуация, когда отправитель и получатель
 * заблокируют операцию transfer() друг для друга.
 *
 * Осторожно, взаимоблокировка.
 * Повторить несколько запусков
 */
public class BankOperation {

    static BankAccount annAccount = new BankAccount("Ann", 1000);
    static BankAccount bobAccount = new BankAccount("Bob", 1000);
    static final int counter = 5, value = 200;

    public static void main(String[] args) throws InterruptedException {

        Thread threadAnn = new Thread(() -> {
            for (int i = 0; i < counter; i++)
                transfer(annAccount, bobAccount, value);
        });
        threadAnn.start();

        Thread threadBob = new Thread(() -> {
            for (int i = 0; i < counter; i++)
                transfer(bobAccount, annAccount, value);
        });
        threadBob.start();
    }

    // Перевод некоторой суммы денег (value) с одного аккаунта (from) на другой (to)
    public static boolean transfer (BankAccount from, BankAccount to, int value) {
        if (value < 0) return false;

        // блокируем отправителя
        synchronized (from){
            if (from.getMoney(value)){

                // блокируем получателя
                synchronized (to){
                    if (to.putMoney(value)) {
                        System.out.printf("+%d From %s[%5d] to %s[%5d] %n",
                                value,
                                from.getName(), from.getBalance(),
                                to.getName(), to.getBalance());
                        return true;
                    }
                }

                from.putMoney(value);
            }
            return false;
        }
    }
}

//В данном классе НЕТ синхронизируемых методов
class BankAccount {
    private int balance;
    private String name;

    public BankAccount (String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public boolean putMoney (int money){
        if (money >= 0) {
            balance += money; return true;
        }
        return false;
    }
    public boolean getMoney (int money){
        if (balance >= money ) {
            balance -= money; return true;
        }
        return false;
    }

    public int getBalance() { return balance; }
    public String getName() { return name; }
}