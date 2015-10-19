package com.gnepux.numedittext.utils;

import android.text.TextUtils;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
import org.javia.arity.Util;

import java.text.NumberFormat;

/**
 * 数字计算类
 */
public class MathUtils {

    /**
     * 数字位数
     */
    private static final int MAX_DIGITS = 12;  // Added by 14110105
    /**
     * 小数点后面保留位数
     */
    private static final int ROUNDING_DIGITS = 3;   // Added by 14110105

    /**
     * 计算器
     * @param s1 第一个参数
     * @param s2 第二个参数
     * @param operate  操作符(+,-,*,/)
     * @return 计算好的结果
     */
    public static String count(String s1, String s2, final String operate)
    {

        String sum = "0";


        //如果s1为空，默认设置为0
        if(TextUtils.isEmpty(s1)) {
            s1 = "0";
        }

        // 假如只输入了一个小数点，我们就在小数点前面加个0
        if(s1.equals(".")) {
            s1 = 0 + s1;
        }

        if(TextUtils.isEmpty(s2)) {
            s2 = "0";
        }

        if( s2.equals(".")){
            s2 = 0 + s2;
        }

        Symbols mSymbols = new Symbols();
        try {
            double tmp;
            tmp = mSymbols.eval(s1 + operate + s2);
            sum = Util.doubleToString(tmp, MAX_DIGITS, ROUNDING_DIGITS);

            return sum;
        } catch (SyntaxException e) {
            e.printStackTrace();
        }

        return sum;
    }

    /**
     * 计算百分比
     * @param x 除数
     * @param total 被除数
     * @return 百分比
     */
    public static String getPercent(int x, int total) {

        if (total == 0) {
            return "0.00%";
        }

        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double total_doule = total * 1.0;
        double tempresult = x_double / total_doule;
        NumberFormat nf = NumberFormat.getPercentInstance(); // 注释掉的也是一种方法
        nf.setMinimumFractionDigits(2); // 保留到小数点后几位
        // DecimalFormat df1 = new DecimalFormat("00%"); // ##.00%
        // 百分比格式，后面不足2位的用0补齐
        result = nf.format(tempresult);
        // result = df1.format(tempresult);
        return result;
    }

}
