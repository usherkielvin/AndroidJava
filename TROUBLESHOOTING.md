# Troubleshooting Connection Issues

## Common Issues: Server Failed, Registration Failed, Login Failed

If you're experiencing connection errors in the Android emulator, follow these steps:

### 1. Check Laravel Server Status

**First, verify your Laravel server is running:**

```bash
# In your Laravel project directory, run:
php artisan serve
```

**Expected output:**
```
Starting Laravel development server: http://127.0.0.1:8000
```

### 2. Verify API URL Configuration

**Check `ApiClient.java` base URL:**

- **For Android Emulator:** `http://10.0.2.2:8000/`
  - `10.0.2.2` is the special IP that Android emulator uses to access the host machine's localhost
  - **IMPORTANT:** Must include trailing slash `/` when endpoints don't start with `/`

- **For Physical Device:** `http://YOUR_COMPUTER_IP:8000/`
  - Find your computer's IP address:
    - **Windows:** `ipconfig` (look for IPv4 Address)
    - **Mac/Linux:** `ifconfig` (look for inet address)
  - Example: `http://192.168.1.100:8000/`
  - **IMPORTANT:** Must include trailing slash `/`

**Location:** `app/src/main/java/hans/ph/api/ApiClient.java`

```java
private static final String BASE_URL = "http://10.0.2.2:8000/"; // For emulator (note trailing slash)
// OR
private static final String BASE_URL = "http://192.168.1.100:8000/"; // For physical device (note trailing slash)
```

### 3. Test API Endpoints in Browser

**Test if your Laravel API is accessible:**

1. **Registration endpoint:**
   ```
   http://10.0.2.2:8000/api/v1/register
   ```
   (This should return an error about missing data, not a connection error)

2. **Login endpoint:**
   ```
   http://10.0.2.2:8000/api/v1/login
   ```
   (This should return an error about missing data, not a connection error)

**If you get "Connection refused" or "Site can't be reached":**
- Laravel server is not running
- Wrong port number
- Firewall is blocking the connection

### 4. Check Network Security Configuration

**Verify `network_security_config.xml` allows cleartext traffic:**

Location: `app/src/main/res/xml/network_security_config.xml`

The file should include:
```xml
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">10.0.2.2</domain>
    <domain includeSubdomains="true">localhost</domain>
    <domain includeSubdomains="true">127.0.0.1</domain>
</domain-config>
```

### 5. Check AndroidManifest.xml

**Verify internet permission and network security config:**

Location: `app/src/main/AndroidManifest.xml`

Should include:
```xml
<uses-permission android:name="android.permission.INTERNET" />

<application
    ...
    android:networkSecurityConfig="@xml/network_security_config"
    android:usesCleartextTraffic="true">
```

### 6. Check Logcat for Detailed Errors

**In Android Studio:**

1. Open **Logcat** tab
2. Filter by tag: `RegistrationError`, `ApiClient`, or `OkHttp`
3. Look for connection errors and server responses

**Common error messages:**
- `Failed to connect to /10.0.2.2:8000` - Server not running
- `Unable to resolve host` - Wrong URL or network issue
- `Connection refused` - Server not accessible
- `Connection timeout` - Server too slow or unreachable
- `Response code: 500` - Server error, check Laravel logs
- `Response code: 422` - Validation error, check error body for details

**The app now logs detailed error information:**
- Response codes
- Error response bodies
- Server error messages
- Connection error details

Look for logs tagged with `RegistrationError` to see the actual server response.

### 7. Verify Laravel API Routes

**Check if your Laravel routes are set up correctly:**

Location: `routes/api.php`

Should include:
```php
Route::post('/v1/register', [AuthController::class, 'register']);
Route::post('/v1/login', [AuthController::class, 'login']);
Route::post('/v1/send-verification-code', [AuthController::class, 'sendVerificationCode']);
Route::post('/v1/verify-email', [AuthController::class, 'verifyEmail']);
```

### 8. Check Laravel CORS Configuration

**If using CORS, verify it's configured correctly:**

Location: `config/cors.php`

Should allow requests from your app.

### 9. Common Solutions

#### Problem: "Failed to connect" or "Connection refused"
**Solution:**
1. Start Laravel server: `php artisan serve`
2. Verify server is running on port 8000
3. Check if port 8000 is not blocked by firewall

#### Problem: "Unable to resolve host" or "Server error failed to register"
**Solution:**
1. Check `ApiClient.java` BASE_URL is correct (must have trailing slash `/`)
2. For emulator, use: `http://10.0.2.2:8000/` (note the trailing slash)
3. For physical device, use your computer's IP address with trailing slash: `http://192.168.1.100:8000/`
4. Check Android Logcat for detailed error messages (filter by "RegistrationError" or "ApiClient")
5. Verify Laravel server is running and accessible
6. Check Laravel server logs for actual error details

#### Problem: Works in browser but not in app
**Solution:**
1. Check `network_security_config.xml` allows cleartext traffic
2. Verify `AndroidManifest.xml` has network security config
3. Check if using HTTPS vs HTTP mismatch

#### Problem: "Connection timeout"
**Solution:**
1. Check if server is responding slowly
2. Verify network connection
3. Try increasing timeout in Retrofit/OkHttp

### 10. Quick Diagnostic Steps

1. **Start Laravel server:**
   ```bash
   php artisan serve
   ```

2. **Test in browser:**
   - Open: `http://10.0.2.2:8000/api/v1/register`
   - Should see an error (not connection error)

3. **Check API URL in app:**
   - Open: `ApiClient.java`
   - Verify BASE_URL matches your setup

4. **Check Logcat:**
   - Look for "ApiClient" logs
   - Check for connection errors

5. **Rebuild and reinstall app:**
   - Clean and rebuild project
   - Reinstall app on emulator

### 11. Testing with Postman/cURL

**Test your API endpoints directly:**

```bash
# Test registration endpoint
curl -X POST http://10.0.2.2:8000/api/v1/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "password_confirmation": "password123"
  }'
```

If this works but the app doesn't, the issue is with the app configuration.

### 12. Debug Mode

The app now includes HTTP logging. Check Logcat for:
- Request URLs
- Request bodies
- Response codes
- Response bodies

Filter by: `OkHttp` or `ApiClient`

## Still Having Issues?

1. **Check Laravel logs:**
   ```bash
   tail -f storage/logs/laravel.log
   ```

2. **Check Android Logcat:**
   - Filter by: `ApiClient`, `OkHttp`, or your package name

3. **Verify network connectivity:**
   - Can you ping `10.0.2.2` from emulator?
   - Is your computer's firewall blocking connections?

4. **Test with different port:**
   - Try: `php artisan serve --port=8080`
   - Update `ApiClient.java` to use port 8080

## Summary Checklist

- [ ] Laravel server is running (`php artisan serve`)
- [ ] Server is on port 8000 (or correct port)
- [ ] `ApiClient.java` BASE_URL is correct
- [ ] `network_security_config.xml` allows cleartext traffic
- [ ] `AndroidManifest.xml` has internet permission
- [ ] API endpoints are accessible in browser
- [ ] Laravel routes are set up correctly
- [ ] CORS is configured (if needed)
- [ ] Check Logcat for detailed error messages

## Contact

If issues persist, check:
1. Laravel server logs
2. Android Logcat
3. Network configuration
4. Firewall settings

