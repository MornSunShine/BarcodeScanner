package barcodescanner.listener;

/**
 * Author: MaoMorn
 * Date: 2017/9/27
 * Time: 17:10
 * Description: 监听键盘活动监听器
 * 通过实现此接口，完成对键盘输入字符合法性的判断，以及关于扫码枪输入合法规则的定义
 */
public interface KeyboardListener {
    /**
     *
     * @param keyCode 按键的ASCII码，包括合法字符，以及默认的结束符“回车”
     */
    public void onKey(int keyCode);
}
