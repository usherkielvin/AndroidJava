package hans.ph;

import hans.ph.R;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
//hiiiiiiiiiiiii
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Edge-to-edge content for a modern look (defensive)
		try {
			WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
		} catch (Throwable t) {
			Log.w("MainActivity", "Edge-to-edge setup failed", t);
		}

		// Top app bar as ActionBar
		MaterialToolbar toolbar = findViewById(R.id.topAppBar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		} else {
			Log.w("MainActivity", "Toolbar not found in layout");
		}

		// Find views
		ImageView heroImage = findViewById(R.id.heroImage);
		TextView headline = findViewById(R.id.headline);
		TextView subtitle = findViewById(R.id.subtitle);
		MaterialButton primaryCta = findViewById(R.id.primaryCta);

		// Dynamic headline/subtitle based on screen width
		int widthDp = getResources().getConfiguration().screenWidthDp;
		if (headline != null && subtitle != null) {
			if (widthDp >= 600) {
				headline.setText("Welcome to JavaApp");
				subtitle.setText("Optimized for tablets and large screens");
			} else if (widthDp >= 360) {
				headline.setText("Welcome");
				subtitle.setText("A modern, responsive landing page");
			} else {
				headline.setText("Hi");
				subtitle.setText("Lightweight and fast on any device");
			}
		} else {
			Log.w("MainActivity", "Headline or subtitle not found in layout");
		}

		// CTA behavior
		if (primaryCta != null) {
			primaryCta.setOnClickListener(v -> {
				startActivity(new Intent(this, SecondActivity.class));
			});
		} else {
			Log.w("MainActivity", "Primary CTA not found in layout");
		}

		// Optionally update the hero image if desired
		// heroImage.setImageResource(R.mipmap.ic_launcher);
	}
}
