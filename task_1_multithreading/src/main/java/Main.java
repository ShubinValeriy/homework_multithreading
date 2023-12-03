import java.util.*;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        // Изначальная реализация по условию
        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            int maxSize = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {
                        continue;
                    }
                    boolean bFound = false;
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
        }
        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");


        // Моя реализация используя потоки

        // Создание листа со списком потоков
        List<Thread> threads = new ArrayList<>();
        final String[] myTexts = texts;
        // Оценим выполнение кода с потоками по времени
        long myStartTs = System.currentTimeMillis(); // my start time
        //запустим цикл
        for (String myText : myTexts) {
            Thread thread = new Thread(()-> {
                int maxSize = 0;
                for (int i = 0; i < myText.length(); i++) {
                    for (int j = 0; j < myText.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (myText.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(myText.substring(0, 100) + " -> " + maxSize);
            });
            threads.add(thread);
            thread.start();
        }
        // Запустим ожидание запущенных потоков основным потоком программы

        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }
        long myEndTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (myEndTs - myStartTs) + "ms");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    
}