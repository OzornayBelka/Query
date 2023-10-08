import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static int counterWord = 10_000;
    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    public static Thread threadText;

    public static void main(String[] args) throws InterruptedException{
        threadText = new Thread(() -> {
            for (int a = 0; a < counterWord; a++) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadText.start();
        threadMaxChar(queue1, 'a').start();
        threadMaxChar(queue2, 'c').start();
        threadMaxChar(queue3,'b').start();

    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread threadMaxChar(BlockingQueue<String> queue, char letter) {
        return new Thread(() -> {
            int max = 0;
            int count = 0;
            String text = null;
            while (threadText.isAlive()){
                try {
                    text = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (char s : text.toCharArray()){
                    if(s == letter) count++;
                }
                if(count > max) {
                    max = count;
                } count = 0;
            }
            System.out.println("Количество строк с символом " + letter + " - " + max);
        });
    }
}
