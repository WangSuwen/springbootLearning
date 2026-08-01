import java.util.concurrent.CountDownLatch;

public class Demo2 {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) {
        for (Long i = 0L; i < Long.MAX_VALUE; i++) {
            x = 0;
            y = 0;
            a = 0;
            b = 0;

            CountDownLatch latch = new CountDownLatch(2);
            Thread t1 = new Thread(() -> {
                a = 1;
                x = b;
                latch.countDown();
            });

            Thread t2 = new Thread(() -> {
                b = 1;
                y = a;
                latch.countDown();
            });

            t1.start();
            t2.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String resultString = "第" + i + "次运行结果:（" + x + "," + y + "）";
            if (x == 0 && y == 0) {
                System.out.println(resultString); 
                break;
            }
        }
    }
}
