import org.springframework.util.StopWatch;

import java.io.*;
import java.net.URL;

/**
 * Created with IDEA by @author:logosty(ganyingle)
 * Date:2018/12/6 Time:10:40
 * Description: 第一天，穿越世界， 首先纠正手表的电磁频率
 * Here are other example situations:
 * +1, +1, +1 results in  3
 * +1, +1, -2 results in  0
 * -1, -2, -3 results in -6
 */
public class Day1 {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int total = 0;
        URL resource = Day1.class.getClassLoader().getResource("day1.txt");
        File file = new File(resource.getFile());

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            int tempbyte;

            int temNum = 0;
            boolean isPositive = true;
            while (true) {
                tempbyte = bis.read();
                switch (tempbyte) {
                    case 45:
                        isPositive = false;
                        break;
                    case 43:
                        isPositive = true;
                        break;
                    case 10:
                        temNum = 0;
                        break;
                    case 13:
                        total += isPositive ? temNum : -temNum;
                        break;
                    case -1:
                        total += isPositive ? temNum : -temNum;
                        System.out.println(total);
                        stopWatch.stop();
                        System.out.println(stopWatch.getTotalTimeMillis());
                        return;
                    default:
                        temNum = temNum * 10 + (tempbyte - 48);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
