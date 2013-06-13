package cec.net;
import java.util.concurrent.TimeUnit;

class Wait {
    static void seconds(long amount){
        try {
            TimeUnit.SECONDS.sleep(amount);
        } catch (InterruptedException e) {
            // swallow exception often a bad practice
        }
    }

    static void millis(long amount){
        try {
            TimeUnit.MILLISECONDS.sleep(amount);
        } catch (InterruptedException e) {
            // swallow exception often a bad practice
        }
    }
}
