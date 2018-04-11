package com.homework;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class SumOfArgs {
    public static void main(String[] args){
        Integer sum = 0;
        for(String i : args){
            if(i.matches("\\d*")) {
                Integer number = Integer.parseInt(i);
                sum = sum + number;
            }
        }
        System.out.println(sum);
    }
}
