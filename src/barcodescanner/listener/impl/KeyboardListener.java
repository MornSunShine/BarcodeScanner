package barcodescanner.listener.impl;

import barcodescanner.buffer.BarcodeBuffer;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2017/9/27
 * Time: 15:41
 * Description: 键盘监听器
 * 这里认为的合法条形码数据为若干位0-9数字，并且以"回车"结尾
 * 如果需要，可以将这个类定义为一个interface，通过实现类的形式，
 * 自定义多种策略判定合法字符以及定义maxScanTime和barcodeMinLength的数值
 */
public class KeyboardListener {
    //条形码数据缓冲区
    private StringBuilder barcode;
    //扫描开始时间
    private long start;
    //合法字符表
    private Map<Integer, Integer> keyToLetter = new HashMap<Integer, Integer>();
    //一次扫描的最长时间
    private static int maxScanTime = 300;
    //条形码的最短长度
    private static int barcodeMinLength = 6;

    /**
     * 初始键盘代码和字母的对应关系
     */
    public KeyboardListener() {
        keyToLetter.put(48, 0);
        keyToLetter.put(49, 1);
        keyToLetter.put(50, 2);
        keyToLetter.put(51, 3);
        keyToLetter.put(52, 4);
        keyToLetter.put(53, 5);
        keyToLetter.put(54, 6);
        keyToLetter.put(55, 7);
        keyToLetter.put(56, 8);
        keyToLetter.put(57, 9);
    }

    /**
     * 此方法响应扫描枪事件
     *
     * @param keyCode 输入键对应的数字码
     */
    public void onKey(int keyCode) {
        //回车判断标记
        boolean tag;
        if(keyCode>=48&&keyCode<=57){
            tag=false;
        }else if(keyCode==13){
            tag=true;
        }else{
            return;
        }
        //缓存区还未初始化
        if (barcode == null) {
            //初始化缓存区
            barcode = new StringBuilder();
            //记录开始扫描时间
            start = System.currentTimeMillis();
        }
        //扫描时间计算
        long cost = System.currentTimeMillis() - start;
        //超过最低扫描时间限制，清空缓存区，重新计时
        if (cost > maxScanTime) {
            //清空缓存区
            barcode = new StringBuilder();
            //重新记录开始扫描时间
            start = System.currentTimeMillis();
        }
        if(tag){
            /*
             * 回车键
             * 扫描结束，数据合法性判断
             * 在 maxScanTime 毫秒内，至少输入 barcodeMinLength 个字符，并且以"回车"结束，才会被判定为扫码枪输入
             */
            if (barcode.length() >= barcodeMinLength && cost < maxScanTime) {
                cost = System.currentTimeMillis() - start;
                System.out.println("耗时：" + cost);
                System.out.println(barcode.toString());
                //将数据加入缓存阻塞队列
                BarcodeBuffer.product(barcode.toString());
            }
            //清空原来的缓冲区,方便重新扫描计时
            barcode = null;
        }else{
            //数字则加入缓冲区
            barcode.append(keyToLetter.get(keyCode));
        }
    }
}
