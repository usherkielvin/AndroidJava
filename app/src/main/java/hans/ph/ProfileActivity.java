package hans.ph;

import hans.ph.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

	public static final String EXTRA_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		MaterialToolbar toolbar = findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		String email = getIntent().getStringExtra(EXTRA_EMAIL);
		TextView emailTextView = findViewById(R.id.emailTextView);
		if (emailTextView != null && email != null) {
			emailTextView.setText(email);
		}

		MaterialButton logoutButton = findViewById(R.id.logoutButton);
		if (logoutButton != null) {
			logoutButton.setOnClickListener(v -> {
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			});
		}
	}
}

