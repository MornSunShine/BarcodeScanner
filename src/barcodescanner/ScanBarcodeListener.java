package barcodescanner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Author: MaoMorn
 * Date: 2017/9/28
 * Time: 14:59
 * Description: 扫码枪监听器
 * 插件的入口类，实现Tomcat的静态监听器，可以直接将这个类配置到tomcat项目下的web.xml里作为一个后台服务存在
 */
public class ScanBarcodeListener implements ServletContextListener {
    private BarcodeProducter barcodeProducter;
    private BarcodeConsumer barcodeConsumer;

    /**
     * tomcat启动
     * @param sce 上下文事件
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        barcodeProducter=new BarcodeProducter();
        barcodeConsumer=new BarcodeConsumer();
        barcodeProducter.startProduct();
        barcodeConsumer.startConsume();
    }
    /**
     * tomcat关闭
     * @param sce 上下文事件
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        barcodeProducter.stopProduct();
        barcodeConsumer.stopConsume();
    }
    /**
     * 插件测试的入口
     * @param args 运行配置
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //主要执行
        BarcodeProducter producter=new BarcodeProducter();
        BarcodeConsumer consumer=new BarcodeConsumer();

        producter.startProduct();
        consumer.startConsume();

        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("输入 'exit' 退出程序");
        String line=reader.readLine();
        while(line!=null){
            if("exit".equals(line)){
                producter.stopProduct();
                consumer.stopConsume();
                System.exit(0);
            }
            line=reader.readLine();
        }
    }
}
