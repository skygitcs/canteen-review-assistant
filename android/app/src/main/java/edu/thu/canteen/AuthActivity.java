package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {
    private boolean registerMode = false;
    private TextView title;
    private EditText nameInput;
    private Button submitButton;
    private Button switchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        title = findViewById(R.id.auth_title);
        nameInput = findViewById(R.id.auth_name);
        submitButton = findViewById(R.id.auth_submit);
        switchButton = findViewById(R.id.auth_switch);

        findViewById(R.id.nav_back).setOnClickListener(v -> finish());
        submitButton.setOnClickListener(v -> {
            UiUtils.toast(this, registerMode
                    ? "\u6ce8\u518c\u5b8c\u6210\uff08\u9759\u6001\u6f14\u793a\uff0c\u4e0d\u4fdd\u5b58\u6570\u636e\uff09"
                    : "\u767b\u5f55\u6210\u529f\uff08\u9759\u6001\u6f14\u793a\uff0c\u4e0d\u6821\u9a8c\u8d26\u53f7\uff09");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        switchButton.setOnClickListener(v -> {
            registerMode = !registerMode;
            render();
        });
        render();
    }

    private void render() {
        title.setText(registerMode ? "\u6ce8\u518c\u8d26\u53f7" : "\u767b\u5f55");
        nameInput.setVisibility(registerMode ? android.view.View.VISIBLE : android.view.View.GONE);
        submitButton.setText(registerMode ? "\u6ce8\u518c" : "\u767b\u5f55");
        switchButton.setText(registerMode ? "\u5df2\u6709\u8d26\u53f7\uff0c\u53bb\u767b\u5f55" : "\u6ca1\u6709\u8d26\u53f7\uff0c\u53bb\u6ce8\u518c");
    }
}
