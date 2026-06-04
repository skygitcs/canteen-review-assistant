# Backend API Test Script - Windows PowerShell
# Ensure backend is running at localhost:8080

$BaseUrl = "http://localhost:8080/api"
$Token = ""
$Username = "testuser_$(Get-Date -Format "yyyyMMddHHmmss")"

Write-Host "=== Starting Backend API Test (PowerShell) ===" -ForegroundColor Cyan

try {
    # 1. Register
    Write-Host "`n[1/7] Testing Register..."
    $RegisterBody = @{
        username = $Username
        password = "password123"
        nickname = "TestUser"
    } | ConvertTo-Json

    $RegisterRes = Invoke-RestMethod -Uri "$BaseUrl/auth/register" -Method Post -Body $RegisterBody -ContentType "application/json"
    if ($RegisterRes.code -eq 0) {
        Write-Host "Register Success" -ForegroundColor Green
        $Token = $RegisterRes.data.token
    } else {
        Write-Host "Register Failed: $($RegisterRes.message)" -ForegroundColor Red
        return
    }

    # 2. Login
    Write-Host "`n[2/7] Testing Login..."
    $LoginBody = @{
        username = $Username
        password = "password123"
    } | ConvertTo-Json
    $LoginRes = Invoke-RestMethod -Uri "$BaseUrl/auth/login" -Method Post -Body $LoginBody -ContentType "application/json"
    if ($LoginRes.code -eq 0) {
        Write-Host "Login Success" -ForegroundColor Green
    }

    # 3. Get Canteens
    Write-Host "`n[3/7] Getting Canteen List..."
    $CanteensRes = Invoke-RestMethod -Uri "$BaseUrl/canteens" -Method Get
    if ($CanteensRes.code -eq 0) {
        $CanteenId = $CanteensRes.data[0].id
        Write-Host "Success, First Canteen ID: $CanteenId" -ForegroundColor Green
    }

    # 4. Get Dishes
    Write-Host "`n[4/7] Getting Dish List..."
    $DishesRes = Invoke-RestMethod -Uri "$BaseUrl/dishes?canteenId=$CanteenId" -Method Get
    if ($DishesRes.code -eq 0) {
        $DishId = $DishesRes.data[0].id
        Write-Host "Success, First Dish ID: $DishId" -ForegroundColor Green
    }

    # 5. Add Favorite
    Write-Host "`n[5/7] Testing Add Favorite..."
    $Headers = @{ Authorization = "Bearer $Token" }
    $FavAddBody = @{ dishId = $DishId } | ConvertTo-Json
    $FavAddRes = Invoke-RestMethod -Uri "$BaseUrl/users/me/favorites" -Method Post -Headers $Headers -Body $FavAddBody -ContentType "application/json"
    if ($FavAddRes.code -eq 0) {
        Write-Host "Add Favorite Success" -ForegroundColor Green
    }

    # 6. Get Favorites
    Write-Host "`n[6/7] Getting Favorite List..."
    $FavListRes = Invoke-RestMethod -Uri "$BaseUrl/users/me/favorites" -Method Get -Headers $Headers
    if ($FavListRes.code -eq 0) {
        Write-Host "Get Favorites Success, Count: $($FavListRes.data.Count)" -ForegroundColor Green
    }

    # 7. Remove Favorite
    Write-Host "`n[7/7] Testing Remove Favorite (Delete)..."
    $FavDelRes = Invoke-RestMethod -Uri "$BaseUrl/users/me/favorites/$DishId" -Method Delete -Headers $Headers
    if ($FavDelRes.code -eq 0) {
        Write-Host "Remove Favorite Success" -ForegroundColor Green
    }

    Write-Host "`n=== Test Completed Successfully ===" -ForegroundColor Cyan
}
catch {
    Write-Host "`nError Occurred: $_" -ForegroundColor Red
    Write-Host "Ensure backend is running at localhost:8080 and DB is started." -ForegroundColor Yellow
}
