param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$Password = "123456"
)

$ErrorActionPreference = "Stop"

function Assert-True {
    param(
        [bool]$Condition,
        [string]$Message
    )
    if (-not $Condition) {
        throw "Smoke test failed: $Message"
    }
}

function Assert-Ok {
    param(
        [object]$Response,
        [string]$Name
    )
    Assert-True ($null -ne $Response) "$Name returned null"
    Assert-True ($Response.code -eq 0) "$Name returned code $($Response.code): $($Response.message)"
}

function Invoke-Json {
    param(
        [string]$Method,
        [string]$Path,
        [object]$Body = $null,
        [hashtable]$Headers = @{}
    )

    $uri = "$BaseUrl$Path"
    if ($null -eq $Body) {
        return Invoke-RestMethod -Uri $uri -Method $Method -Headers $Headers
    }

    $json = $Body | ConvertTo-Json -Depth 10
    return Invoke-RestMethod -Uri $uri -Method $Method -Headers $Headers -Body $json -ContentType "application/json"
}

$suffix = Get-Date -Format "yyyyMMddHHmmss"
$username = "smoke$suffix"

Write-Host "Running backend smoke test against $BaseUrl"

$health = Invoke-Json Get "/api/health"
Assert-Ok $health "health"
Assert-True ($health.data.status -eq "UP") "health status should be UP"

$register = Invoke-Json Post "/api/auth/register" @{
    username = $username
    password = $Password
    nickname = "Smoke Test User"
}
Assert-Ok $register "register"
Assert-True (![string]::IsNullOrWhiteSpace($register.data.token)) "register should return token"
$headers = @{ Authorization = "Bearer $($register.data.token)" }

$login = Invoke-Json Post "/api/auth/login" @{
    username = $username
    password = $Password
}
Assert-Ok $login "login"

$demoLogin = Invoke-Json Post "/api/auth/login" @{
    username = "demo"
    password = "password"
}
Assert-Ok $demoLogin "demo login"

$announcements = Invoke-Json Get "/api/announcements"
Assert-Ok $announcements "announcements"
Assert-True ($announcements.data.Count -gt 0) "announcements should not be empty"

$canteens = Invoke-Json Get "/api/canteens"
Assert-Ok $canteens "canteens"
Assert-True ($canteens.data.Count -ge 7) "canteens should contain seed data"
$canteenId = $canteens.data[0].id

$canteenDetail = Invoke-Json Get "/api/canteens/$canteenId"
Assert-Ok $canteenDetail "canteen detail"
Assert-True ($canteenDetail.data.windows.Count -gt 0) "canteen detail should contain windows"
Assert-True ($canteenDetail.data.dishes.Count -gt 0) "canteen detail should contain dishes"
$windowId = $canteenDetail.data.windows[0].id

$dishes = Invoke-Json Get "/api/dishes"
Assert-Ok $dishes "dishes"
Assert-True ($dishes.data.Count -ge 20) "dishes should contain seed data"
$dishId = $dishes.data[0].id

$dishDetail = Invoke-Json Get "/api/dishes/$dishId"
Assert-Ok $dishDetail "dish detail"
Assert-True ($dishDetail.data.base.id -eq $dishId) "dish detail id mismatch"

$tags = Invoke-Json Get "/api/dishes/tags"
Assert-Ok $tags "dish tags"
Assert-True ($tags.data.Count -gt 0) "dish tags should not be empty"

$recommendations = Invoke-Json Get "/api/dishes/recommendations?limit=3"
Assert-Ok $recommendations "recommendations"
Assert-True ($recommendations.data.Count -le 3) "recommendations should honor limit"

$review = Invoke-Json Post "/api/dishes/$dishId/reviews" @{
    rating = 5
    content = "Automated smoke test review"
    imageUrl = $null
} $headers
Assert-Ok $review "create review"
Assert-True ($review.data.rating -eq 5) "review rating mismatch"

$favorite = Invoke-Json Post "/api/users/me/favorites" @{
    dishId = $dishId
} $headers
Assert-Ok $favorite "favorite"

$favorites = Invoke-Json Get "/api/users/me/favorites" $null $headers
Assert-Ok $favorites "favorites"
Assert-True ($favorites.data.Count -gt 0) "favorites should not be empty"

$submission = Invoke-Json Post "/api/dishes/submissions" @{
    canteenId = $canteenId
    windowId = $windowId
    name = "Smoke Test Dish $suffix"
    imageUrl = $null
    price = 9.90
    description = "Automated smoke test submission"
    spiceLevel = 1
    tags = @("test", "light")
} $headers
Assert-Ok $submission "dish submission"
Assert-True ($submission.data.auditStatus -eq "PENDING") "submission should be pending"

Write-Host "Smoke test passed."
Write-Host "Temporary user: $username"
