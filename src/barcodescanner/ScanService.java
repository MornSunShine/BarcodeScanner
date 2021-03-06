package barcodescanner;

import barcodescanner.listener.KeyboardListener;
import barcodescanner.listener.impl.NumKeyboardListener;
import barcodescanner.tool.Config;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;


/**
 * Author: MaoMorn
 * Date: 2017/9/27
 * Time: 16:32
 * Description: 扫码枪扫描监听服务
 * 通过HOOK技术监听键盘事件(这里认为扫码枪相当于只有0-9和回车的键盘)
 */
public class ScanService {
    private HHOOK hhkKeyBoard;
    private final User32 lib = User32.INSTANCE;

    /**
     * 停止扫码枪服务
     */
    public void stopScanService() {
        //卸载键盘钩子
        lib.UnhookWindowsHookEx(hhkKeyBoard);
    }

    /**
     * 启动扫码枪服务
     */
    public void startScanService() {
        //键盘事件监听
        //从配置文件中确定监听器的实现类
        String listenerName = "barcodescanner.listener.impl." + Config.getInstance().getScanListener();
        final KeyboardListener listener;
        try {
            listener = (KeyboardListener) Class.forName(listenerName).newInstance();

            //键盘事件回掉函数
            LowLevelKeyboardProc keyboardHook = new LowLevelKeyboardProc() {
                @Override
                public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
                    if (nCode >= 0) {
                        switch (wParam.intValue()) {
                            case WinUser.WM_KEYUP:
                                int keyCode = info.vkCode;

                                //监听数字键0-9
                                if (keyCode >= 48 && keyCode <= 57) {
                                    //交个监听器处理
                                    listener.onKey(keyCode);
                                } else if (keyCode == 13) {
                                    //监听回车键
                                    //交个监听器处理
                                    listener.onKey(keyCode);
                                }
                                break;
                        }
                    }
                    //交个下一个钩子
                    return lib.CallNextHookEx(hhkKeyBoard, nCode, wParam,
                            info.getPointer());
                }

            };

            HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);

            //将上面定义的 回调方法 安装到挂钩链中对系统的底层的键盘输入事件进行监控
            hhkKeyBoard = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);

            // 处理消息（线程阻塞）
            int result;
            MSG msg = new WinUser.MSG();
            while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
                if (result == -1) {
                    System.err.println("error in get message");
                    break;
                } else {
                    lib.TranslateMessage(msg);
                    lib.DispatchMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
