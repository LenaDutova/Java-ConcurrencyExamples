package messages;


public class CallbackMailbox {
    public static void main(String[] args) {
        // создадим два потока
        MessageCallbackThread someThread = new MessageCallbackThread();
        MessageCallbackThread anotherThread = new MessageCallbackThread();

        // обменяемся ссылками для обратного вызова
        someThread.setCallback(anotherThread.getCallback());
        anotherThread.setCallback(someThread.getCallback());

        // стартуем их
        someThread.start();
        anotherThread.start();
    }

}

/**
 * Интерфейс с методом для возврата данных
 */
interface MessageCallback{
    void call(String message);
}

/**
 * Этот поток имеет два объекта для обратных вызовов -
 * один, чтобы получить данные, второй чтобы их передать
 *
 */
class MessageCallbackThread extends Thread{

    private String messageFrom;
    private String messageTo;

    private MessageCallback send;
    private MessageCallback receive = new MessageCallback() {
        @Override
        public void call(String message) {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " call with message " + message);
            messageFrom = message;
        }
    };

    /**
     * Возвращаем ссылку на Callback для получения данных
     * @return
     */
    public MessageCallback getCallback() {
        return receive;
    }

    /**
     * Получаем ссылку на Callback для отправки данных
     * @param send
     */
    public void setCallback(MessageCallback send) {
        this.send = send;
    }

    @Override
    public void run() {
        messageTo = Thread.currentThread().getName(); // генерируем сообщение
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " send message " + messageTo);
        send.call(messageTo); // прокидываем свое сообщение

        try {
            sleep(500); // за это "условное" время произошел обмен сообщениями
//            sleep(0); // а без задержки, возможно не было получено сообщение от другого потока
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " received message " + messageFrom);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}