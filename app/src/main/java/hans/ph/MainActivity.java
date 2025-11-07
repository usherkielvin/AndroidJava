package hans.ph;

import hans.ph.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

	private static final String DEFAULT_EMAIL = "example@com";
	private static final String DEFAULT_PASSWORD = "1234";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextInputEditText emailInput = findViewById(R.id.emailInput);
		TextInputEditText passwordInput = findViewById(R.id.passwordInput);
		MaterialButton loginButton = findViewById(R.id.loginButton);

		if (loginButton != null) {
			loginButton.setOnClickListener(v -> {
				String email = emailInput != null ? emailInput.getText().toString().trim() : "";
				String password = passwordInput != null ? passwordInput.getText().toString() : "";

				if (email.isEmpty() || password.isEmpty()) {
					Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
					return;
				}

				if (email.equals(DEFAULT_EMAIL) && password.equals(DEFAULT_PASSWORD)) {
					startActivity(new Intent(this, DashboardActivity.class));
					finish();
				} else {
					Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
