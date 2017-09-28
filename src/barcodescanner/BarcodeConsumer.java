package barcodescanner;

import barcodescanner.buffer.BarcodeBuffer;
import barcodescanner.service.BarcodeSaveService;
import barcodescanner.tool.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2017/9/28
 * Time: 14:44
 * Description: 条形码数据消费者
 * 从阻塞队列中拿取生产者类生产的条码数据，根据配置的操作实现类，执行相应的操作序列
 * 数据的保存调用BarcodeSaveService接口定义的save方法
 */
public class BarcodeConsumer {
    //消费者线程
    private Thread thread;
    //数据保存服务（可有多个）
    private List<BarcodeSaveService> barcodeSaveServices=new ArrayList<BarcodeSaveService>();
    private boolean quit;

    /**
     * 停止消费者线程
     * 此方法在应用关闭的时候被调用
     */
    public void stopConsume(){
        if(thread!=null){
            thread.interrupt();
            //释放资源
            for(BarcodeSaveService barcodeSaveService : barcodeSaveServices){
                barcodeSaveService.finish();
            }
        }
    }
    /**
     * 启动消费者线程
     * 此方法在应用启动的时候被调用
     */
    public void startConsume(){
        //防止重复启动
        if(thread!=null && thread.isAlive()){
            return;
        }
        System.out.println("条形码消费者线程启动");

        System.out.println("注册条形码保存服务");
        registerBarcodeSaveServcie();

        thread=new Thread(){
            @Override
            public void run(){
                while(!quit){
                    try{
                        //当缓冲区没有数据的时候，此方法会阻塞
                        String barcode= BarcodeBuffer.consume();
                        if(barcodeSaveServices.isEmpty()){
                            System.out.println("没有注册任何条形码保存服务");
                        }
                        for(BarcodeSaveService barcodeSaveService : barcodeSaveServices){
                            barcodeSaveService.save(barcode);
                        }
                    }catch(InterruptedException e){
                        quit=true;
                    }
                }
                System.out.println("条形码消费者线程退出");
            }
        };
        thread.setName("consumer");
        thread.start();
    }

    /**
     * 消费者线程从缓冲区获取到数据后需要调用保存服务对数据进行处理
     */
    private void registerBarcodeSaveServcie() {
        List<String> classes=getBarcodeSaveServcieImplClasses();
        System.out.println("条形码保存服务实现数目有："+classes.size());
        for(String clazz : classes){
            try{
                barcodeSaveServices.add((BarcodeSaveService)Class.forName(clazz).newInstance());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 从配置中获取保存服务类名序列
     * @return  多个保存服务实现类名
     */
    private List<String> getBarcodeSaveServcieImplClasses() {
        List<String> result=new ArrayList<String>();
        String regex="barcodescanner.service.impl.";
        String []temp=Config.getInstance().getSaveService().split(",");
        for(String i:temp){
            result.add(regex+i);
        }
        return result;
    }
}
