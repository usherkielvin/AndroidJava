package hans.ph;

import hans.ph.R;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

	public static final String EXTRA_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		MaterialToolbar toolbar = findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		String email = getIntent().getStringExtra(EXTRA_EMAIL);
		MaterialButton profileButton = findViewById(R.id.profileButton);
		if (profileButton != null) {
			profileButton.setOnClickListener(v -> {
				Intent intent = new Intent(this, ProfileActivity.class);
				intent.putExtra(ProfileActivity.EXTRA_EMAIL, email);
				startActivity(intent);
			});
		}
	}
}

