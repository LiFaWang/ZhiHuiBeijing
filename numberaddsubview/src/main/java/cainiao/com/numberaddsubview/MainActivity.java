package cainiao.com.numberaddsubview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    private NumberAddSubView mNumberAddSubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNumberAddSubView= (NumberAddSubView) findViewById(R.id.number_add_sub_view);
        initNumberView();
    }

    private void initNumberView() {
        mNumberAddSubView.setOnNumberClickListener(new NumberAddSubView.OnNumberClickListener() {
            @Override
            public void onButtonSub(View v, int value) {
                Toast.makeText(getApplicationContext(), "简点击"+v+value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonAdd(View v, int value) {
                Toast.makeText(getApplicationContext(), "加点击"+v+value, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
