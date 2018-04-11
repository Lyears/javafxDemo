package com.homework.subString;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class SubString {
    public static void main(String[] args) {
        String s = args[0];
        Integer index = Integer.parseInt(args[1]) - 1;
        Integer length = Integer.parseInt(args[2]);

        System.out.println(s.substring(index,index+length));

    }
}
