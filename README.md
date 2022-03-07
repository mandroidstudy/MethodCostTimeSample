# MethodCostTimeSample
Android耗时方法打印
## 背景
最近在做启动优化，我需要打印出所有耗时的方法，或者是打印出所有耗时超过指定时间的方法，为此我写了这个工具，通过编写Plugin，注册Transform，在class文件转为dex文件之前进行字节码插桩，
在每个方法的开头和结尾插入我指定的方法。具体效果如下：
插桩前：
```
  private void c() {
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```
插桩后：
```
    private void c() {
        TimeMonitor.i("com/maove/methodcosttimesample/MainActivity", "c", "()V");

        try {
            Thread.sleep(80L);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

        TimeMonitor.o("com/maove/methodcosttimesample/MainActivity", "c", "()V");
    }
```
## 如何使用？
因为比较简单，目前还没有打算发布为插件，需要复制buildSrc的代码。并且编写一个TimeMonitor类，或者复制我项目中的。
在app下使用插件并且配置属性
apply plugin: 'org.maove.methodTimeBeat'
methodTime{
    isOpen = true
    injectClass = "com/maove/libutil/TimeMonitor"
}
TimeMonitor类是被插入的类，通过injectClass传给插件，格式是包名+类名 TimeMonitor的方法参数必须是3个并且都是String类型的，eg:
```
public class TimeMonitor {

    private static final HashMap<String,Long> timeMap = new HashMap<>();

    public static void i(String className,String methodName,String descriptor){
        if (className.contains(TimeMonitor.class.getSimpleName())) return;
        String key= className+"_"+methodName+"_"+descriptor;
        timeMap.put(key,System.currentTimeMillis());
    }

    public static void o(final String className,String methodName,String descriptor){
        if (className.contains(TimeMonitor.class.getSimpleName())) return;
        String key= className+"_"+methodName+"_"+descriptor;
        long endTime = System.currentTimeMillis();
        Long startTime = timeMap.get(key);
        if (startTime!=null){
            timeMap.remove(key);
            long cost = endTime - startTime;
            //只答应耗时超过OVER_TIME ms的方法
            long OVER_TIME = 2;
            if (cost > OVER_TIME){
                String tag = className.substring(className.lastIndexOf("/")+1);
                Log.d(TimeMonitor.class.getSimpleName(),tag+"#"+methodName+":"+cost+"ms" + " thread:"+Thread.currentThread().getName());
            }
        }
    }
}
```
方法名i和o是默认的，如果想使用其他的，可以通过methodIn和methodOut来配置 eg:
```
methodTime{
    isOpen = true
    injectClass = "com/maove/libutil/TimeMonitor"
    methodIn = "methodEnter"
    methodIn = "methodExit"
}
```

```
public class TimeMonitor {

    public static void methodEnter(String className,String methodName,String descriptor){
        //code...
    }

    public static void methodExit(final String className,String methodName,String descriptor){
        //code..
    }
}
```