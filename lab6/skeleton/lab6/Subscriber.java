package lab6;

import java.util.concurrent.*;

public class Subscriber extends Thread
{
    private MessageQueue MQ;

    public Subscriber(MessageQueue MQ) {
        this.MQ = MQ;
    }

    public void run() {
        int count = 0;
        while (count < 20) {
            String toRead = MQ.getMessage();

            if (!toRead.equals("")) {
                count++;
                Util.print(Util.getDate() + " Subscriber - read " + "\"" + toRead + "\"");
            }
            else {
                Util.print(Util.getDate() + " Subscriber - tried to read but no message ...");
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(0,1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}
