import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RealDownloadUIDemo {

    // 预设一个用于测试真实下载的文件 URL（这里使用一个约 10MB 的开源测试图片）
    // 你也可以替换为任何合法的下载链接
    // private static final String DOWNLOAD_URL = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=1000";
    private static final String DOWNLOAD_URL = "https://download-cdn.jetbrains.com/idea/idea-2026.2-aarch64.dmg";
    
    // 下载文件保存到本地的目标路径
    private static final Path SAVE_PATH = Paths.get("downloaded_idea.dmg");

    public static void main(String[] args) {
        // 在 UI 事件调度线程中创建和初始化界面
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // 1. 创建主窗口
        JFrame frame = new JFrame("真实网络文件下载与 UI 多线程响应演示");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 260);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // 2. 创建UI组件
        JButton testClickButton = new JButton("点击测试响应（点我）");
        JButton startDownloadButton = new JButton("开始真实下载");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(450, 30));
        progressBar.setStringPainted(true); // 显示百分比文本

        JLabel statusLabel = new JLabel("状态：等待下载...");

        // -------------------------------------------------------------
        // 交互 1：点击“测试响应”按钮
        // 证明主线程（UI线程）一直活着用以响应用户点击，绝不卡死
        // -------------------------------------------------------------
        testClickButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "UI 界面非常流畅！随时能响应你的点击！");
        });

        // -------------------------------------------------------------
        // 交互 2：点击“开始下载”按钮
        // 核心：启动【后台子线程】去真正请求网络 API 并将数据流写入本地文件
        // -------------------------------------------------------------
        startDownloadButton.addActionListener(e -> {
            // 点击后禁用下载按钮，防止重复点击
            startDownloadButton.setEnabled(false);
            statusLabel.setText("状态：正在连接服务器下载...");
            progressBar.setValue(0);

            // 开启【新子线程】来执行真实的网络 IO 与文件写入
            Thread downloadThread = new Thread(() -> {
                try {
                    // 调用真实的网络下载逻辑
                    downloadFileWithProgress(DOWNLOAD_URL, SAVE_PATH, progressBar, statusLabel);

                    // 下载成功后的 UI 恢复操作
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("状态：下载完成！保存路径：" + SAVE_PATH.toAbsolutePath());
                        startDownloadButton.setEnabled(true);
                        JOptionPane.showMessageDialog(frame, "文件下载成功！\n保存至：" + SAVE_PATH.toAbsolutePath());
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    // 下载失败后的 UI 提示
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("状态：下载失败 - " + ex.getMessage());
                        startDownloadButton.setEnabled(true);
                        JOptionPane.showMessageDialog(frame, "下载出错：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    });
                }
            });

            // 启动后台下载子线程
            downloadThread.start();
        });

        // 3. 组装 UI 界面
        frame.add(startDownloadButton);
        frame.add(testClickButton);
        frame.add(progressBar);
        frame.add(statusLabel);

        frame.setLocationRelativeTo(null); // 窗口居中
        frame.setVisible(true);
    }

    /**
     * 真实的网络文件下载方法（运行在后台子线程中）
     */
    private static void downloadFileWithProgress(String fileUrl, Path savePath, JProgressBar progressBar, JLabel statusLabel) throws Exception {
        // 创建 Java 现代 HttpClient
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS) // 自动跟随 302/301 重定向
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .GET()
                .build();

        // 发送 HTTP 请求，以流（InputStream）的形式接收响应体
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() != 200) {
            throw new RuntimeException("服务器返回异常状态码: " + response.statusCode());
        }

        // 获取服务器返回的文件总大小（字节）
        long totalBytes = response.headers().firstValueAsLong("content-length").orElse(-1L);

        // 如果获取到了文件总大小，在 UI 界面展示提示
        if (totalBytes > 0) {
            SwingUtilities.invokeLater(() -> statusLabel.setText(String.format("状态：正在下载 (总计 %.2f MB)...", totalBytes / (1024.0 * 1024.0))));
        }

        // 逐块读取网络流并写入本地文件
        try (InputStream inputStream = response.body();
             OutputStream outputStream = Files.newOutputStream(savePath)) {

            byte[] buffer = new byte[8192]; // 8KB 的缓冲区
            int bytesRead;
            long totalBytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // 如果拿到了文件总大小，则计算真实百分比；否则做未知进度的累加
                if (totalBytes > 0) {
                    int percent = (int) ((totalBytesRead * 100) / totalBytes);

                    // 💡 关键点：网络读取是在子线程，更新 UI 必须通过 SwingUtilities.invokeLater 丢给主线程
                    SwingUtilities.invokeLater(() -> progressBar.setValue(percent));
                }
            }
        }
    }
}