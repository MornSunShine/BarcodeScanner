package barcodescanner;

/**
 * Author: MaoMorn
 * Date: 2017/9/28
 * Time: 14:32
 * Description: 条码数据生产者
 * 实现对扫码枪(键盘)的监听，将监听得到的条码数据放入阻塞队列，提供给条码数据消费者操作
 */
public class BarcodeProducter {
    private boolean quit;
    private Thread thread;
    private ScanService scanService;

    public BarcodeProducter() {
        scanService = new ScanService();
    }

    /**
     * 启动生产者线程
     * 此方法在应用启动的时候被调用
     */
    public void startProduct() {
        //防止重复启动
        if (thread != null && thread.isAlive()) {
            return;
        }
        System.out.println("启动条形码生产者...");
        //启动一个线程用于在需要关闭扫描监听服务的时候卸载键盘钩子
        thread = new Thread() {
            @Override
            public void run() {
                System.out.println("条码枪扫描线程启动");
                while (!quit) {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (Exception e) {
                        quit = true;
                    }
                }
                scanService.stopScanService();
                System.out.println("条码枪扫描线程退出");
                System.exit(0);
            }
        };
        thread.start();
        //开始监听扫描活动
        new Thread() {
            @Override
            public void run() {
                scanService.startScanService();
            }
        }.start();
    }

    /**
     * 关闭生产者线程
     * 此方法在应用关闭的时候被调用
     */
    public void stopProduct() {
        if (thread != null) {
            thread.interrupt();
            System.out.println("停止条形码生产者...");
        }
    }
}
