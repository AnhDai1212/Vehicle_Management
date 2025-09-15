function Get-EnvVars {
    param([string]$Path = ".env")
    $envVars = @{}
    if (-not (Test-Path $Path)) {
        throw "Environment file not found: $Path"
    }
    switch -Regex -File $Path {
        # Match lines with key=value format, ignoring comments and empty lines
        "^\s*([^#][^=]*)\s*=\s*(.*)" {
            $key = $matches[1].Trim()
            $value = $matches[2].Trim()
            # Skip empty keys
            if (-not [string]::IsNullOrWhiteSpace($key)) {
                $envVars[$key] = $value
            }
        }
    }
    return $envVars
}

# Load environment variables from .env file
try {
    Write-Host "Loading environment variables from ./envs/.env..."
    $envVars = Get-EnvVars -Path "./envs/.env"
    if ($envVars.Count -eq 0) {
        Write-Warning "No environment variables found in .env file"
    } else {
        # Apply environment variables
        $envVars.GetEnumerator() | ForEach-Object {
            [System.Environment]::SetEnvironmentVariable($_.Key, $_.Value, [System.EnvironmentVariableTarget]::Process)
        }
        Write-Host "Successfully loaded $($envVars.Count) environment variables."
    }
}
catch {
    Write-Error "Failed to load environment variables: $_"
    exit 1
}

# Run Spring Boot application
.\mvnw.cmd spring-boot:run