# BarcodeScanner
Achieve and judge the data from barcode scanner gun, and save in custom wags after operation
Then, there is not much core code, because scanner is like keyboard, just input more quickly if use scanner,
what the demo did is just to define the way to judge the input rate, to differentiate scanner from common keyboard.

##Interface
KeyboardListener：定义合法输入数据的格式，比如数字0-9，以回车结尾

BarcodeSaveService：条形码数据保存服务，其实不只是保存服务，这里提供了保存到文件、保存到PostgreSQL数据库，
当然，如果想要更加的用户友好，可以将数据库的相关信息配置到配置文件中

##Configuration
application.properties：这里提供两个配置属性
* keyboardListener：键盘监听器实现类，类的存放路径为barcodescanner.listener.imple下，自定义的实现类也要放在这个路径下，
不然加载配置的时候会报错
* saveService：保存服务实现类，这个属性的值可以是一个序列，序列中以","作为分隔符，
实体类存放的路径为barcodescanner.service.imple

