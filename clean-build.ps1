# PowerShell script to force clean Android build directories
# This script helps resolve "Unable to delete directory" errors

param(
    [string]$ProjectPath = $PSScriptRoot
)

Write-Host "Cleaning Android build directories..." -ForegroundColor Yellow

$buildPaths = @(
    "$ProjectPath\app\build",
    "$ProjectPath\build",
    "$ProjectPath\.gradle"
)

foreach ($path in $buildPaths) {
    if (Test-Path $path) {
        Write-Host "Attempting to delete: $path" -ForegroundColor Cyan
        
        # Try normal deletion first
        try {
            Remove-Item -Path $path -Recurse -Force -ErrorAction Stop
            Write-Host "Successfully deleted: $path" -ForegroundColor Green
        }
        catch {
            Write-Host "Failed to delete normally. Trying alternative method..." -ForegroundColor Yellow
            
            # Alternative: Use robocopy to delete (works better with locked files)
            $emptyDir = "$env:TEMP\empty_$(Get-Random)"
            New-Item -ItemType Directory -Path $emptyDir -Force | Out-Null
            
            try {
                # Robocopy trick: copy empty dir to target, then delete
                & robocopy $emptyDir $path /MIR /R:0 /W:0 /NFL /NDL /NJH /NJS | Out-Null
                Remove-Item -Path $emptyDir -Recurse -Force -ErrorAction SilentlyContinue
                Remove-Item -Path $path -Recurse -Force -ErrorAction Stop
                Write-Host "Successfully deleted: $path (using robocopy method)" -ForegroundColor Green
            }
            catch {
                Write-Host "ERROR: Could not delete $path" -ForegroundColor Red
                Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
                Write-Host "  Solution: Close Android Studio and any running processes, then try again." -ForegroundColor Yellow
            }
        }
    }
    else {
        Write-Host "Path does not exist: $path" -ForegroundColor Gray
    }
}

Write-Host "`nCleanup complete!" -ForegroundColor Green
Write-Host "You can now rebuild your project in Android Studio." -ForegroundColor Cyan

