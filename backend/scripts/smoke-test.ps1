param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"

function Assert-Ok($Response, $Name) {
    if ($null -eq $Response) {
        throw "$Name failed: empty response"
    }
    if ($Response.code -ne 0) {
        throw "$Name failed: code=$($Response.code), message=$($Response.message)"
    }
    Write-Host "[OK] $Name"
}

Write-Host "Smoke testing $BaseUrl"

$announcements = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/announcements"
Assert-Ok $announcements "GET /api/announcements"

$canteens = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/canteens"
Assert-Ok $canteens "GET /api/canteens"

$dishes = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/dishes/recommendations?limit=3"
Assert-Ok $dishes "GET /api/dishes/recommendations"

$username = "smoke_$([DateTimeOffset]::UtcNow.ToUnixTimeSeconds())"
$password = "123456"

$registerBody = @{
    username = $username
    password = $password
    nickname = "Smoke Test"
} | ConvertTo-Json

$register = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/auth/register" -ContentType "application/json" -Body $registerBody
Assert-Ok $register "POST /api/auth/register"
$token = $register.data.token
$headers = @{ Authorization = "Bearer $token" }

$me = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/users/me" -Headers $headers
Assert-Ok $me "GET /api/users/me"

$crowdBody = @{ level = 3 } | ConvertTo-Json
$crowd = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/canteens/1/crowd" -Headers $headers -ContentType "application/json" -Body $crowdBody
Assert-Ok $crowd "POST /api/canteens/1/crowd"

$favoriteBody = @{ dishId = 1 } | ConvertTo-Json
$favorite = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/users/me/favorites" -Headers $headers -ContentType "application/json" -Body $favoriteBody
Assert-Ok $favorite "POST /api/users/me/favorites"

$favorites = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/users/me/favorites" -Headers $headers
Assert-Ok $favorites "GET /api/users/me/favorites"

Write-Host "Smoke test passed."
