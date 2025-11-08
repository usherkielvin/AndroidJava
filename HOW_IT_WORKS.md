# How the Android App Connects to Laravel Database

## 🔄 Complete Authentication Flow

### 1. **User Login Process**

```
Android App                    Laravel API                  Database
    │                              │                           │
    │───(1) Enter email/password──>│                           │
    │                              │                           │
    │                              │───(2) Check credentials──>│
    │                              │                           │
    │                              │<──(3) User data + token───│
    │                              │                           │
    │<──(4) Return token───────────│                           │
    │                              │                           │
    │───(5) Save token locally─────│                           │
    │                              │                           │
    │───(6) Navigate to Dashboard──│                           │
```

### 2. **Step-by-Step Breakdown**

#### **Step 1: User Enters Credentials**
- User types email and password in Android app
- Click "Login" button
- App validates inputs (not empty)

#### **Step 2: Android App Sends Request**
```java
// MainActivity.java
LoginRequest request = new LoginRequest(email, password);
Call<LoginResponse> call = apiService.login(request);
```

**What happens:**
- Creates a `LoginRequest` object with email/password
- Sends POST request to: `http://10.0.2.2:8000/api/v1/login`
- Request body: `{"email": "admin@ad", "password": "password"}`

#### **Step 3: Laravel API Receives Request**
```php
// AuthController.php
public function login(Request $request) {
    // 1. Validate email and password
    $request->validate([
        'email' => 'required|string|email',
        'password' => 'required|string',
    ]);
    
    // 2. Find user in database
    $user = User::where('email', $request->email)->first();
    
    // 3. Check password (hashed)
    if (!$user || !Hash::check($request->password, $user->password)) {
        return response()->json(['success' => false], 401);
    }
    
    // 4. Create API token using Sanctum
    $token = $user->createToken('mobile-app')->plainTextToken;
    
    // 5. Return user data + token
    return response()->json([
        'success' => true,
        'data' => [
            'user' => [...],
            'token' => $token
        ]
    ]);
}
```

**What happens in database:**
- Laravel queries: `SELECT * FROM users WHERE email = ?`
- Checks if password hash matches stored hash
- Creates token in `personal_access_tokens` table
- Returns user info + token

#### **Step 4: Android Receives Response**
```java
// MainActivity.java
if (loginResponse.isSuccess()) {
    // Save token
    tokenManager.saveToken("Bearer " + loginResponse.getData().getToken());
    tokenManager.saveEmail(loginResponse.getData().getUser().getEmail());
    
    // Navigate to dashboard
    startActivity(new Intent(this, DashboardActivity.class));
}
```

**What gets saved:**
- Token stored in `SharedPreferences` (local storage)
- Email saved for quick access
- Name saved for display

### 3. **Token-Based Authentication**

#### **Why Tokens?**
- Tokens prove user is authenticated
- No need to send password every time
- Server can verify token without database lookup
- Tokens can be revoked (logout)

#### **How Tokens Work:**
```
┌─────────────────────────────────────────────┐
│  Token Structure (Sanctum)                  │
├─────────────────────────────────────────────┤
│  Format: "user_id|random_string"            │
│  Example: "1|fXw1niqMpic3wrn7xO7opUV..."    │
│                                             │
│  Stored in:                                 │
│  - Database: personal_access_tokens table   │
│  - Android: SharedPreferences               │
└─────────────────────────────────────────────┘
```

#### **Using Token for Authenticated Requests:**
```java
// ProfileActivity.java - Get user profile
String token = tokenManager.getToken(); // "Bearer 1|abc123..."
Call<UserResponse> call = apiService.getUser(token);
```

**Request Headers:**
```
Authorization: Bearer 1|fXw1niqMpic3wrn7xO7opUV...
```

**Laravel checks:**
```php
// Protected route
Route::middleware('auth:sanctum')->group(function () {
    Route::get('/user', [ProfileController::class, 'user']);
});
```

**Laravel process:**
1. Extracts token from `Authorization` header
2. Looks up token in `personal_access_tokens` table
3. Finds associated user
4. Returns user data

### 4. **Database Tables Involved**

#### **users table:**
```
┌────┬──────────┬─────────────┬──────────┬──────────┐
│ id │ name     │ email       │ password │ role     │
├────┼──────────┼─────────────┼──────────┼──────────┤
│ 1  │ Admin    │ admin@ad    │ $2y$...  │ admin    │
│ 2  │ John     │ john@com    │ $2y$...  │ customer │
└────┴──────────┴─────────────┴──────────┴──────────┘
```

#### **personal_access_tokens table:**
```
┌────┬──────────┬──────────────┬────────────┬─────────────┐
│ id │ tokenable_id │ name      │ token      │ abilities   │
├────┼──────────────┼───────────┼────────────┼─────────────┤
│ 1  │ 1            │ mobile-app│ 1|abc123...│ ["*"]       │
│ 2  │ 2            │ mobile-app│ 2|xyz789...│ ["*"]       │
└────┴──────────────┴───────────┴────────────┴─────────────┘
```

### 5. **Logout Process**

```java
// ProfileActivity.java
public void logout() {
    // 1. Call API to revoke token
    apiService.logout(token);
    
    // 2. Delete token from database
    // (Laravel removes from personal_access_tokens)
    
    // 3. Clear local storage
    tokenManager.clear();
    
    // 4. Go back to login
    startActivity(new Intent(this, MainActivity.class));
}
```

### 6. **Security Features**

#### **Password Security:**
- Passwords are **hashed** using bcrypt in Laravel
- Never stored as plain text
- Cannot be reversed

#### **Token Security:**
- Tokens are unique per user
- Stored securely in database
- Can be revoked (logout deletes token)
- Sent over HTTPS in production (HTTP for local dev)

#### **API Security:**
- Protected routes require valid token
- Invalid token = 401 Unauthorized
- No token = Cannot access protected routes

### 7. **Data Flow Example**

**Login Request:**
```json
POST /api/v1/login
{
  "email": "admin@ad",
  "password": "password"
}
```

**Login Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user": {
      "id": 1,
      "name": "Admin User",
      "email": "admin@ad",
      "role": "admin"
    },
    "token": "1|fXw1niqMpic3wrn7xO7opUVX1ZvQlYh3hPCgGPOPde2629eb"
  }
}
```

**Get User Request (Authenticated):**
```json
GET /api/v1/user
Headers: {
  "Authorization": "Bearer 1|fXw1niqMpic3wrn7xO7opUVX1ZvQlYh3hPCgGPOPde2629eb"
}
```

**Get User Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@ad",
    "role": "admin"
  }
}
```

### 8. **Key Components**

#### **Android Side:**
- `ApiClient.java` - Configures Retrofit for API calls
- `ApiService.java` - Defines API endpoints
- `TokenManager.java` - Manages token storage (SharedPreferences)
- `MainActivity.java` - Handles login
- `ProfileActivity.java` - Fetches user data using token

#### **Laravel Side:**
- `routes/api.php` - API routes
- `AuthController.php` - Login/logout logic
- `ProfileController.php` - User profile endpoints
- `User.php` model - User data with HasApiTokens trait
- Sanctum - Token management

### 9. **What Happens When User Logs In Again?**

1. App checks if token exists in `TokenManager`
2. If token exists → Skip login, go to Dashboard
3. If no token → Show login screen
4. Token persists until logout or app uninstall

### 10. **Adding New Users**

**In Laravel (via database or registration):**
```php
User::create([
    'name' => 'New User',
    'email' => 'newuser@example.com',
    'password' => Hash::make('password123'),
    'role' => 'customer'
]);
```

**That user can now login from Android app!**
- Email: `newuser@example.com`
- Password: `password123`

## 🎯 Summary

1. **Android app** sends email/password to **Laravel API**
2. **Laravel** checks **database** for user
3. **Laravel** creates **token** and returns it
4. **Android** saves token locally
5. **Android** uses token for authenticated requests
6. **Laravel** validates token and returns data
7. **Logout** revokes token from database

**Any user in your Laravel database can now login to your Android app!** 🎉

