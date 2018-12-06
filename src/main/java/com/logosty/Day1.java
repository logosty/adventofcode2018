package com.logosty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IDEA by @author:logosty(ganyingle)
 * Date:2018/12/6 Time:10:40
 * Description: 第一天，穿越世界， 首先纠正手表的电磁频率
 */

public class Day1 {
    private static Logger logger = LoggerFactory.getLogger(Day1.class);

    /**
     * * Here are other example situations:
     * * +1, +1, +1 results in  3
     * * +1, +1, -2 results in  0
     * * -1, -2, -3 results in -6
     */
    private int test1() {
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
                        return total;
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
        return 0;
    }

    /**
     * Current frequency  0, change of +1; resulting frequency  1.
     * Current frequency  1, change of -2; resulting frequency -1.
     * Current frequency -1, change of +3; resulting frequency  2.
     * Current frequency  2, change of +1; resulting frequency  3.
     * (At this point, the device continues from the start of the list.)
     * Current frequency  3, change of +1; resulting frequency  4.
     * Current frequency  4, change of -2; resulting frequency  2, which has already been seen.
     * 重复列表，直到找到同评率2次的数
     */
    private int test2() throws Exception {
        int total = 0;
        URL resource = Day1.class.getClassLoader().getResource("day1.txt");
        File file = new File(resource.getFile());

        HashSet<Integer> set = new HashSet<Integer>();
        List<Integer> numList = new LinkedList<Integer>();

        set.add(0);
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
                case -1:
                    numList.add(isPositive ? temNum : -temNum);
                    total += isPositive ? temNum : -temNum;
                    if (!set.add(total)) {
                        return total;
                    }
                    break;
                default:
                    temNum = temNum * 10 + (tempbyte - 48);
                    break;
            }
            if (tempbyte == -1) {
                break;
            }
        }

        while (true) {
            for (Integer x : numList) {
                total += x;
                if (!set.add(total)) {
                    return total;
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        Day1 day1 = new Day1();

        stopWatch.start("test1");
        int result1 = day1.test1();
        stopWatch.stop();
        logger.info("result1={}, costtime={}", result1, stopWatch.getLastTaskTimeMillis());

        stopWatch.start("test2");
        int result2 = day1.test2();
        stopWatch.stop();
        logger.info("result2={}, costtime={}", result2, stopWatch.getLastTaskTimeMillis());


    }
}
