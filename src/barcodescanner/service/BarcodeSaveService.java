package barcodescanner.service;

/**
 * Author: MaoMorn
 * Date: 2017/9/27
 * Time: 14:47
 * Description: 数据保存服务接口
 * 可以通过这个接口自定义数据保存服务，例如不同的数据库，不同的目标文件等
 */
public interface BarcodeSaveService {
    /**
     * 保存条形码
     * @param barcode 条形码数据
     */
    public void save(String barcode);
    /**
     * 在这里释放资源，如数据库连接，关闭文件，关闭网络连接等
     */
    public void finish();
}
