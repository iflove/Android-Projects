# lib_logger

> implementation 'com.github.iflove.Hunter:logExtlibrary:2.0.0' 迁移继续维护



一个kt编写的轻量日志扩展，便于以后更换日志库和日志控制，同样适用纯JAVA安卓project

### 接口和类：

```java
Logger //日志通用简单接口
Logg //Log 工具
LogPrintln //灵活打印函数接口
Printer //打印包装,方便调试
```

`kotlin 用法`

```kotlin
//开启关闭日志
Logg.LOG_ENABLED = true //全局变量


//自定义Logger 处理
Logg.log = object : Logger {

  override fun v(tag: String, msg: String) {
    Log.v(tag, msg)
  }

  override fun v(tag: String, msg: String, tr: Throwable) {
    Log.v(tag, msg, tr)
  }

  override fun d(tag: String, msg: String) {
    Log.d(tag, msg)
  }

  override fun d(tag: String, msg: String, tr: Throwable) {
    Log.d(tag, msg, tr)
  }

  override fun i(tag: String, msg: String) {
    Log.i(tag, msg)
  }

  override fun i(tag: String, msg: String, tr: Throwable) {
    Log.i(tag, msg, tr)
  }

  override fun w(tag: String, msg: String) {
    Log.w(tag, msg)
  }

  override fun w(tag: String, msg: String, tr: Throwable) {
    Log.w(tag, msg, tr)
  }

  override fun e(tag: String, msg: String) {
    Log.e(tag, msg)
  }

  override fun e(tag: String, msg: String, tr: Throwable) {
    Log.e(tag, msg, tr)
  }

  /**
  * 灵活打印函数, 由logID logic handle
  */
  override fun println(logID: Int, level: Int, tag: String, msg: String) {
    Log.println(level, tag, msg)
  }
}

//动态开关变量
Logg.setLoggable(TAG, Log.DEBUG)
val loggable = Logg.isLoggable(TAG, Log.DEBUG)

//堆栈信息获取
val stackTraceMsg = Logg.getStackTraceMsg("test getStackTraceMsg",4)
Logg.d(TAG, "stackTraceMsg: $stackTraceMsg")
 
```

`Java 用法`

```java
Logg.setLog(new Logger() {
            @Override
            public void v(@NonNull String tag, @NonNull String msg) {
                Log.v(tag, msg);
            }

            @Override
            public void v(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void d(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void d(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void i(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void i(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void w(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void w(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void e(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void e(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void println(int logID, int level, @NonNull String tag, @NonNull String msg) {
                Logger.super.println(logID, level, tag, msg);
            }
        });
```

```kotlin
//日志开关
Logger log = new Printer("xxa", true).getLog();
log.v("xxa", "Printer 控制a");
log.v("xxa", "Printer 控制b");
        
        
```

