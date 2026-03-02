package start;

/**
 * В любой программе содержится как минимум один поток исполнения –
 * «главный», «стартовый» или также «родительский», который,
 * если потребуется, будет запрашивать дополнительные или «дочерние» потоки
 */
public class MainThread {
    public static void main(String[] args) {
        System.out.print(Thread.currentThread().getName());
    }
}

