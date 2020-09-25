package com.qx.guli.common.base.util;


import java.text.DecimalFormat;
import java.util.*;

/**
 * @Classname RandomUtils
 * @Description 获取随机数
 * @Date 2020/6/23 12:00
 * @Created by 卿星
 */
public class RandomUtils {

    private static final Random RANDOM = new Random();
    private static final DecimalFormat DECIMAL_FORMAT_FOUR = new DecimalFormat("0000");
    private static final DecimalFormat DECIMAL_FORMAT_SIX = new DecimalFormat("000000");

    public static String getFourBitRandom(){
        return DECIMAL_FORMAT_FOUR.format(RANDOM.nextInt(10000));
    }

    public static String getSixBitRandom(){
        return DECIMAL_FORMAT_SIX.format(RANDOM.nextInt(1000000));
    }

    /**
     * 給定数组抽取n个数据
     * @param list
     * @param n
     * @return
     */
    private static ArrayList getRandom(List list,int n){

        Random random = new Random();

        HashMap<Object, Object> hashMap = new HashMap<>();

        // 生成随机数字并存入hashmap
        for (int i = 0; i < list.size(); i++){
          //
            int number = random.nextInt(100) + 1;
            hashMap.put(number,i);
        }
        // 从hashMap导入数组
        Object[] objects = hashMap.values().toArray();

    System.out.println("hashMap"+hashMap);

    System.out.println("objects"+Arrays.toString(objects));
        ArrayList<Object> r = new ArrayList<>();
        // 遍历并打印数据
        for (int i = 0; i < n; i++){
          //
            r.add(list.get((int)objects[i]));
            System.out.print(list.get((int) objects[i]) + "\t");
        }

        return r;
    }

    /*@Test
    public void test(){
       *//* ArrayList<Object> objects = new ArrayList<>();
        objects.add(1);
        objects.add(3);
        objects.add(7);

        ArrayList random = getRandom(objects, 5);
    System.out.println(random);*//*
        String s = getFourBitRandom();
        String s1 = getSixBitRandom();
    System.out.println(s);
    System.out.println(s1);
    }*/

}
