package messages;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Данный пример демонстрирует
 * обмена данными между двумя потоками
 * через типизированный буфер
 *
 * пакета java.util.concurrent
 */
public class ExchangerReceiver {
    public static void main(String[] args) throws InterruptedException, TimeoutException {
        Exchanger<String> mailbox = new Exchanger<String>();
        new SendMessageThread(mailbox);

        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " wait message");

        // будем ждать сообщение, но сами ничего умного не пошлем
        String message = mailbox.exchange(null, 1, TimeUnit.MICROSECONDS);
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " get message - " + message);
    }
}

class SendMessageThread extends Thread{

    private Exchanger<String> mailbox;
    private String messageTo = "Hello", messageFrom;

    public SendMessageThread(Exchanger<String> mailbox) {
        this.mailbox = mailbox;
        this.start();
    }

    @Override
    public void run() {
        try {
            // задержка
            sleep(1000);

            System.out.println(System.currentTimeMillis() + " " + getName() + " send message - " + messageTo);
            // отправим свое сообщение и получим новое
            messageFrom = mailbox.exchange(messageTo);
            System.out.println(System.currentTimeMillis() + " " + getName() + " get message - " + messageFrom);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
