package barcodescanner.service.impl;

import barcodescanner.service.BarcodeSaveService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

/**
 * Author: MaoMorn
 * Date: 2017/9/27
 * Time: 14:51
 * Description: 数据保存接口实现类
 * 将条码数据保存到资源文件barcode.txt中
 */
public class BarcodeToFile implements BarcodeSaveService {
    private Writer writer;

    /**
     * 保存到文件
     * @param barcode 条形码数据
     */
    @Override
    public void save(String barcode) {
        try {
            if (writer == null) {
                System.out.println("打开文件");
                URL url = BarcodeToFile.class.getResource("./barcode.txt");
                writer = new OutputStreamWriter(new FileOutputStream(String.valueOf(url), true));
            }
            writer.write(barcode + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭文件
     */
    @Override
    public void finish() {
        System.out.println("关闭文件");
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
