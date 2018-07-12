package jsync.com.drawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    MySurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.my_surface_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.pause();
    }
}
