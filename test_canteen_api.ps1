# 强制设置控制台编码为 UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$baseUrl = "http://localhost:8080"
$canteenId = 1 # 紫荆食堂
$url = "$($baseUrl)/api/canteens/$canteenId"

Write-Host "`n--- 正在请求接口: $url ---" -ForegroundColor Cyan

try {
    # 创建 WebClient 并强制使用 UTF-8 下载数据
    $wc = New-Object System.Net.WebClient
    $wc.Encoding = [System.Text.Encoding]::UTF8
    $json = $wc.DownloadString($url)
    
    # 解析 JSON
    $response = $json | ConvertFrom-Json
    
    if ($response.code -eq 0) {
        Write-Host "状态: 成功 (code 0)" -ForegroundColor Green
        Write-Host "`n紫荆食堂正式菜品列表:" -ForegroundColor Yellow
        
        foreach ($dish in $response.data.dishes) {
            # 格式化输出
            $msg = "ID: {0,-3} | 菜名: {1,-10} | 窗口: {2,-10} | 楼层: {3}" -f $dish.id, $dish.name, $dish.windowName, $dish.floorNo
            Write-Host $msg
        }
    } else {
        Write-Host "后端报错: $($response.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "脚本执行失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "提示: 请确保后端程序正在运行且 8080 端口已开启。" -ForegroundColor Gray
}
