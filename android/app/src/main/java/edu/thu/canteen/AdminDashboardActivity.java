package edu.thu.canteen;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.model.DishSubmission;
import edu.thu.canteen.data.network.AdminDtos;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {
    private SubmissionAdapter adapter;
    private List<DishSubmission> submissions = new ArrayList<>();
    private EditText announceTitle;
    private EditText announceContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        announceTitle = findViewById(R.id.admin_announce_title);
        announceContent = findViewById(R.id.admin_announce_content);
        findViewById(R.id.admin_post_announce_button).setOnClickListener(v -> postAnnouncement());

        RecyclerView submissionList = findViewById(R.id.submission_list);
        submissionList.setLayoutManager(new LinearLayoutManager(this));
        
        // Initializing with empty list, will be populated by fetchData()
        adapter = new SubmissionAdapter(submissions, new SubmissionAdapter.OnActionClick() {
            @Override
            public void onApprove(DishSubmission submission) {
                handleAudit(submission.id, true);
            }

            @Override
            public void onReject(DishSubmission submission) {
                handleAudit(submission.id, false);
            }
        });
        submissionList.setAdapter(adapter);

        // Remove Support List initialization as it's mock and not supported by API
        
        fetchData();
    }

    private void fetchData() {
        NetworkClient.getService().getPendingSubmissions().enqueue(new Callback<ApiResponse<List<DishSubmission>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DishSubmission>>> call, Response<ApiResponse<List<DishSubmission>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    submissions.clear();
                    submissions.addAll(response.body().data);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<DishSubmission>>> call, Throwable t) {}
        });
    }

    private void handleAudit(long id, boolean approve) {
        AdminDtos.AuditRequest request = new AdminDtos.AuditRequest("Admin approved via mobile app");
        Call<ApiResponse<Object>> call = approve 
            ? NetworkClient.getService().approveSubmission(id, request)
            : NetworkClient.getService().rejectSubmission(id, request);
            
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    UiUtils.toast(AdminDashboardActivity.this, approve ? "\u5df2批准" : "\u5df2驳回");
                    fetchData();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {}
        });
    }

    private void postAnnouncement() {
        String title = announceTitle.getText().toString().trim();
        String content = announceContent.getText().toString().trim();
        if (title.isEmpty() || content.isEmpty()) {
            UiUtils.toast(this, "\u8bf7\u586b\u5199\u5b8c\u6574\u516c\u544a\u4fe1\u606f");
            return;
        }

        NetworkClient.getService().createAnnouncement(new AdminDtos.AnnouncementRequest(title, content))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful()) {
                            UiUtils.toast(AdminDashboardActivity.this, "\u516c\u544a\u53d1\u5e03\u6210\u529f");
                            announceTitle.setText("");
                            announceContent.setText("");
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {}
                });
    }
}

