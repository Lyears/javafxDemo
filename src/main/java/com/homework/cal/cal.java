package com.homework.cal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

/**
 * @author fzm
 * @date 2018/3/29
 **/
public class cal {

    public static void main(String[] args) {
        boolean running = true;
        Integer month = Integer.parseInt(args[0]);
        Integer year = Integer.parseInt(args[1]);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);

        System.out.print(yearMonth.getMonth() + " ");
        System.out.println(yearMonth.getYear());
        System.out.println("Su Mo Tu We Th Fr Sa");

        LocalDate date = firstDay;
        int monthValue = date.getMonthValue();
        while (running) {
            printLine(date);
            date = date.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            if (date.getMonthValue() != monthValue) {
                running = false;
            }
        }
    }

    private static String multipleSpaces(int n) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < n; i++) {
            space.append(" ");
        }
        return space.toString();
    }

    private static void printLine(LocalDate date) {
        int monthValue = date.getMonthValue();
        boolean running = true;
        if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            System.out.print(multipleSpaces(3 * date.getDayOfWeek().getValue()));
        }
        while (running) {

            System.out.print(formatting(date.getDayOfMonth()) + multipleSpaces(1));
            date = date.plusDays(1);
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                running = false;
            }
            if (date.getMonthValue() != monthValue) {
                running = false;
            }
        }
        System.out.println();
    }

    private static String formatting(int i) {
        StringBuilder value = new StringBuilder();
        if (i < 10) {
            value.append(" ").append(i);
        } else {
            value.append(i);
        }
        return value.toString();
    }

}
