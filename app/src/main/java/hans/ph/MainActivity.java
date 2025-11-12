package hans.ph;

import hans.ph.R;
import hans.ph.api.ApiClient;
import hans.ph.api.ApiService;
import hans.ph.api.LoginRequest;
import hans.ph.api.LoginResponse;
import hans.ph.util.EmailValidator;
import hans.ph.util.TokenManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

	private TokenManager tokenManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tokenManager = new TokenManager(this);

		// Check if already logged in
		if (tokenManager.isLoggedIn()) {
			Intent intent = new Intent(this, DashboardActivity.class);
			intent.putExtra(DashboardActivity.EXTRA_EMAIL, tokenManager.getEmail());
			startActivity(intent);
			finish();
			return;
		}

		TextInputEditText emailInput = findViewById(R.id.emailInput);
		TextInputEditText passwordInput = findViewById(R.id.passwordInput);
		TextInputLayout emailInputLayout = findViewById(R.id.emailInputLayout);
		MaterialButton loginButton = findViewById(R.id.loginButton);
		TextView registerButton = findViewById(R.id.registerButton);

		// Real-time email validation
		if (emailInput != null) {
			emailInput.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}

				@Override
				public void afterTextChanged(Editable s) {
					String email = s.toString().trim();
					if (email.isEmpty()) {
						if (emailInputLayout != null) {
							emailInputLayout.setError(null);
						}
						return;
					}

					EmailValidator.ValidationResult result = EmailValidator.validate(email);
					if (!result.isValid() && email.length() > 5) {
						// Only show error if user has typed enough characters
						if (emailInputLayout != null) {
							emailInputLayout.setError(result.getMessage());
						}
					} else {
						if (emailInputLayout != null) {
							emailInputLayout.setError(null);
						}
					}
				}
			});
		}

		if (registerButton != null) {
			registerButton.setOnClickListener(v -> {
				Intent intent = new Intent(this, RegisterActivity.class);
				startActivity(intent);
			});
		}

		if (loginButton != null) {
			loginButton.setOnClickListener(v -> {
				String email = emailInput != null ? emailInput.getText().toString().trim() : "";
				String password = passwordInput != null ? passwordInput.getText().toString() : "";

				if (email.isEmpty() || password.isEmpty()) {
					Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
					return;
				}

				login(email, password);
			});
		}
	}

	private void login(String email, String password) {
		final MaterialButton loginButton = findViewById(R.id.loginButton);
		if (loginButton != null) {
			loginButton.setEnabled(false);
			loginButton.setText("Logging in...");
		}

		ApiService apiService = ApiClient.getApiService();
		LoginRequest request = new LoginRequest(email, password);

		Call<LoginResponse> call = apiService.login(request);
		call.enqueue(new Callback<LoginResponse>() {
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
				runOnUiThread(() -> {
					if (loginButton != null) {
						loginButton.setEnabled(true);
						loginButton.setText("Login");
					}
				});

				if (response.isSuccessful() && response.body() != null) {
					LoginResponse loginResponse = response.body();
					if (loginResponse.isSuccess() && loginResponse.getData() != null) {
						// Save token and user data
						tokenManager.saveToken("Bearer " + loginResponse.getData().getToken());
						tokenManager.saveEmail(loginResponse.getData().getUser().getEmail());
						tokenManager.saveName(loginResponse.getData().getUser().getName());

						// Navigate to dashboard
						runOnUiThread(() -> {
							Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
							intent.putExtra(DashboardActivity.EXTRA_EMAIL, loginResponse.getData().getUser().getEmail());
							startActivity(intent);
							finish();
						});
					} else {
						final String message = loginResponse.getMessage() != null ? loginResponse.getMessage() : "Login failed";
						runOnUiThread(() -> showError("Login Failed", message));
					}
				} else {
					String errorMsg;
					if (response.code() == 401) {
						errorMsg = "Invalid email or password";
					} else if (response.code() == 500) {
						errorMsg = "Server error. Please try again later.";
					} else {
						errorMsg = "Invalid credentials";
					}
					final String finalErrorMsg = errorMsg;
					runOnUiThread(() -> showError("Login Failed", finalErrorMsg));
				}
			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t) {
				runOnUiThread(() -> {
					if (loginButton != null) {
						loginButton.setEnabled(true);
						loginButton.setText("Login");
					}
				});

				String errorMsg = getConnectionErrorMessage(t);
				runOnUiThread(() -> showError("Connection Error", errorMsg));
			}
		});
	}

	private void showError(String title, String message) {
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton("OK", null)
			.show();
	}

	// Get user-friendly connection error message with diagnostics
	private String getConnectionErrorMessage(Throwable t) {
		if (t.getMessage() == null) {
			return "Connection error. Please try again.";
		}

		String message = t.getMessage();
		String baseUrl = ApiClient.getBaseUrl();
		StringBuilder errorMsg = new StringBuilder();
		
		if (message.contains("Failed to connect") || message.contains("Unable to resolve host")) {
			errorMsg.append("❌ Cannot connect to server\n\n");
			errorMsg.append("Trying to reach: ").append(baseUrl).append("/api/v1/login\n\n");
			errorMsg.append("Please check:\n");
			errorMsg.append("1. Laravel server is running: php artisan serve\n");
			errorMsg.append("2. Server is on port 8000 (default)\n");
			errorMsg.append("3. For emulator, use: http://10.0.2.2:8000\n");
			errorMsg.append("4. For physical device, use your computer's IP\n");
			errorMsg.append("5. Check ApiClient.java BASE_URL setting\n");
			errorMsg.append("6. Verify network_security_config.xml allows cleartext\n\n");
			errorMsg.append("Test in browser: ").append(baseUrl).append("/api/v1/login");
		} else if (message.contains("timeout")) {
			errorMsg.append("⏱ Connection timeout\n\n");
			errorMsg.append("Server may be slow or unreachable.\n");
			errorMsg.append("Check if Laravel server is running and accessible.");
		} else if (message.contains("Connection refused")) {
			errorMsg.append("🔌 Connection refused\n\n");
			errorMsg.append("Server is not running or not accessible.\n");
			errorMsg.append("Please start Laravel server: php artisan serve\n");
			errorMsg.append("Expected URL: ").append(baseUrl);
		} else {
			errorMsg.append("❌ Connection error\n\n");
			errorMsg.append("Details: ").append(message);
		}

		return errorMsg.toString();
	}
}
