package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.AuthDtos;
import edu.thu.canteen.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private boolean registerMode = false;
    private TextView title;
    private EditText nameInput;
    private EditText accountInput;
    private EditText passwordInput;
    private Button submitButton;
    private Button switchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NetworkClient.getToken() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_auth);

        title = findViewById(R.id.auth_title);
        nameInput = findViewById(R.id.auth_name);
        accountInput = findViewById(R.id.auth_account);
        passwordInput = findViewById(R.id.auth_password);
        submitButton = findViewById(R.id.auth_submit);
        switchButton = findViewById(R.id.auth_switch);

        findViewById(R.id.nav_back).setOnClickListener(v -> finish());
        submitButton.setOnClickListener(v -> handleAuth());
        switchButton.setOnClickListener(v -> {
            registerMode = !registerMode;
            render();
        });
        render();
    }

    private void handleAuth() {
        String username = accountInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String nickname = nameInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || (registerMode && nickname.isEmpty())) {
            UiUtils.toast(this, "\u8bf7\u586b\u5199\u5b8c\u6574\u4fe1\u606f");
            return;
        }

        submitButton.setEnabled(false);
        if (registerMode) {
            NetworkClient.getService().register(new AuthDtos.RegisterRequest(username, password, nickname))
                    .enqueue(new AuthCallback());
        } else {
            NetworkClient.getService().login(new AuthDtos.LoginRequest(username, password))
                    .enqueue(new AuthCallback());
        }
    }

    private class AuthCallback implements Callback<ApiResponse<AuthDtos.AuthResponse>> {
        @Override
        public void onResponse(Call<ApiResponse<AuthDtos.AuthResponse>> call, Response<ApiResponse<AuthDtos.AuthResponse>> response) {
            submitButton.setEnabled(true);
            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                NetworkClient.saveToken(response.body().data.token);
                UiUtils.toast(AuthActivity.this, registerMode ? "\u6ce8\u518c\u6210\u529f" : "\u767b\u5f55\u6210\u529f");
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            } else {
                String msg = "\u8bf7\u6c42\u5931\u8d25";
                try {
                    if (response.body() != null) {
                        msg = response.body().message;
                    } else if (response.errorBody() != null) {
                        // Parse error body manually
                        String errorJson = response.errorBody().string();
                        ApiResponse<?> errorResp = new com.google.gson.Gson().fromJson(errorJson, ApiResponse.class);
                        if (errorResp != null) msg = errorResp.message;
                    }
                } catch (Exception e) {
                    msg = "\u534f\u8bae\u89e3\u6790\u9519\u8bef";
                }
                UiUtils.toast(AuthActivity.this, msg);
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<AuthDtos.AuthResponse>> call, Throwable t) {
            submitButton.setEnabled(true);
            UiUtils.toast(AuthActivity.this, "\u7f51\u7edc\u9519\u8bef: " + t.getMessage());
        }
    }

    private void render() {
        title.setText(registerMode ? "\u6ce8\u518c\u8d26\u53f7" : "\u767b\u5f55");
        nameInput.setVisibility(registerMode ? View.VISIBLE : View.GONE);
        submitButton.setText(registerMode ? "\u6ce8\u518c" : "\u767b\u5f55");
        switchButton.setText(registerMode ? "\u5df2\u6709\u8d26\u53f7\uff0c\u53bb\u767b\u5f55" : "\u6ca1\u6709\u8d26\u53f7\uff0c\u53bb\u6ce8\u518c");
    }
}
