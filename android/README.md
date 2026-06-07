# Android 客户端（原型）

本模块为「校园食堂评测系统」Android UI 原型，使用 Java + 原生 XML + Material 组件实现。

## 环境要求

- Android Studio 2023.1+（推荐）
- Android SDK 34
- JDK 17

## 运行步骤

1. 用 Android Studio 打开 `canteen-review-assistant/android`。
2. 等待 Gradle 同步完成。
3. 选择模拟器或真机，点击 Run 运行。

## 连接本地后端

当前 Android 端的后端地址配置在 `edu.thu.canteen.data.network.NetworkClient`：

```java
public static final String BASE_URL = "http://127.0.0.1:8080/";
```

使用真机测试时，手机里的 `127.0.0.1` 默认指手机自己，不是电脑。需要先用 `adb reverse` 把手机的 8080 端口转发到电脑后端：

```powershell
adb devices
adb reverse tcp:8080 tcp:8080
```

如果 PowerShell 找不到 `adb`，说明 Android SDK 的 `platform-tools` 没有加入环境变量。可以先用完整路径执行，例如本机 SDK 在 `E:\AndroidSDK` 时：

```powershell
& "E:\AndroidSDK\platform-tools\adb.exe" devices
& "E:\AndroidSDK\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
```

如果刚刚修改了系统环境变量，已经打开的 Android Studio 终端不会自动刷新。可以重启 Android Studio，或者在当前终端临时追加：

```powershell
$env:Path += ";E:\AndroidSDK\platform-tools"
```

真机测试推荐顺序：

1. 启动后端，确认 `http://localhost:8080/swagger-ui.html` 能访问。
2. 手机通过 USB 连接电脑，并允许 USB 调试。
3. 执行 `adb devices`，确认设备状态为 `device`。
4. 执行 `adb reverse tcp:8080 tcp:8080`。
5. 在 Android Studio 中运行 App 到真机。

## 说明

- 当前主要通过后端 API 获取食堂、菜品、评论、审核等数据。
- 图片加载使用 Glide，图片地址由后端返回。
- UI 使用 XML + Material 组件，Tag 会根据含义显示不同颜色。

## 单元测试

Android 端已补充 JVM 单元测试，覆盖模型构造器、DTO 请求/响应对象、统一响应和 mock 数据过滤逻辑。测试入口位于：

```text
app/src/test/java/edu/thu/canteen/
```

执行命令：

```powershell
cd canteen-review-assistant/android
.\gradlew.bat testDebugUnitTest
```

如果提示找不到 `gradle-wrapper.jar`，需要先恢复 Gradle wrapper，详见 `../docs/test-report.md`。
