# Backend Full API Test Script - Windows PowerShell
# This script tests the core functionality of the Canteen Review Assistant Backend.

$BaseUrl = "http://localhost:8080/api"
$Token = ""
$AdminToken = ""
$Username = "testuser_$(Get-Date -Format "yyyyMMddHHmmss")"

Write-Host "=== Starting Full Backend API Test ===" -ForegroundColor Cyan

function Test-Endpoint {
    param($Name, $Method, $Path, $Body = $null, $AuthToken = $null)
    Write-Host "`n[$Name] Testing $Method $Path..." -NoNewline
    $Headers = @{ "Content-Type" = "application/json" }
    if ($AuthToken) { $Headers.Authorization = "Bearer $AuthToken" }
    
    try {
        $Params = @{ Uri = "$BaseUrl$Path"; Method = $Method; Headers = $Headers }
        if ($Body) { 
            $Params.Body = ($Body | ConvertTo-Json -Depth 10) 
        }
        
        $res = Invoke-RestMethod @Params
        if ($res.code -eq 0) {
            Write-Host " [SUCCESS]" -ForegroundColor Green
            return $res.data
        } else {
            Write-Host " [FAILED: $($res.message)]" -ForegroundColor Red
            return $null
        }
    } catch {
        Write-Host " [ERROR: $($_.Exception.Message)]" -ForegroundColor Red
        return $null
    }
}

# --- 1. Authentication ---
Write-Host "`n--- Testing Authentication ---" -ForegroundColor Yellow
$regData = Test-Endpoint "Register" "Post" "/auth/register" @{
    username = $Username
    password = "password123"
    nickname = "测试用户"
}
if ($regData) { 
    $Token = $regData.token 
    Write-Host "Register Success, Token obtained." -ForegroundColor Gray
}

# Try logging in as Admin (Default seed user)
$loginAdmin = Test-Endpoint "Login Admin" "Post" "/auth/login" @{
    username = "admin"
    password = "password123"
}
if ($loginAdmin) { 
    $AdminToken = $loginAdmin.token 
    Write-Host "Admin Login Success." -ForegroundColor Gray
}

# --- 2. Public Information (Canteens & Dishes) ---
Write-Host "`n--- Testing Public Data Endpoints ---" -ForegroundColor Yellow
$announcements = Test-Endpoint "Get Announcements" "Get" "/announcements"

$canteens = Test-Endpoint "Get Canteen List" "Get" "/canteens"
if ($canteens -and $canteens.Count -gt 0) { 
    $canteenId = $canteens[0].id 
    Write-Host "Target Canteen ID: $canteenId ($($canteens[0].name))" -ForegroundColor Gray
} else {
    $canteenId = 1 # Fallback
}

$canteenDetail = Test-Endpoint "Get Canteen Detail" "Get" "/canteens/$canteenId"
$recommendations = Test-Endpoint "Get Today's Recommendations" "Get" "/dishes/recommendations?limit=5"

$dishes = Test-Endpoint "Search Dishes" "Get" "/dishes?canteenId=$canteenId"
if ($dishes -and $dishes.Count -gt 0) { 
    $dishId = $dishes[0].id 
    Write-Host "Target Dish ID: $dishId ($($dishes[0].name))" -ForegroundColor Gray
} else {
    $dishId = 1 # Fallback
}

$dishDetail = Test-Endpoint "Get Dish Detail" "Get" "/dishes/$dishId"

# --- 3. User Actions (Requires Token) ---
Write-Host "`n--- Testing User-Specific Actions ---" -ForegroundColor Yellow
if ($Token) {
    # Favorites
    Test-Endpoint "Add Favorite" "Post" "/users/me/favorites" @{dishId = $dishId} $Token
    Test-Endpoint "Get My Favorites" "Get" "/users/me/favorites" $null $Token
    Test-Endpoint "Remove Favorite" "Delete" "/users/me/favorites/$dishId" $null $Token
    
    # Crowd Reporting
    Test-Endpoint "Report Crowd (Level 4)" "Post" "/canteens/$canteenId/crowd" @{level = 4} $Token
    
    # Reviews
    Test-Endpoint "Post Review for Dish $dishId" "Post" "/dishes/$dishId/reviews" @{
        rating = 5
        content = "这道菜非常好吃！(来自自动化测试脚本)"
        imageUrl = $null
    } $Token
    
    # Profile
    Test-Endpoint "Get My Profile" "Get" "/users/me" $null $Token
    Test-Endpoint "Update Profile" "Put" "/users/me" @{
        nickname = "极客之友"
        tastePreference = "香辣, 偏咸"
    } $Token
} else {
    Write-Host "Skipping User Actions: No valid user token." -ForegroundColor DarkGray
}

# --- 4. Admin Actions (Requires Admin Token) ---
Write-Host "`n--- Testing Admin Endpoints ---" -ForegroundColor Yellow
if ($AdminToken) {
    Test-Endpoint "Get Admin Submissions" "Get" "/admin/submissions" $null $AdminToken
} else {
    Write-Host "Skipping Admin Actions: No valid admin token." -ForegroundColor DarkGray
}

Write-Host "`n=== Full API Test Completed ===" -ForegroundColor Cyan
Write-Host "Verify the [SUCCESS] markers above to ensure all modules are functioning."
