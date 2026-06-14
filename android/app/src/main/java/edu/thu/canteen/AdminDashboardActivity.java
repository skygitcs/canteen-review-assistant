package edu.thu.canteen;

import android.os.Bundle;
import android.view.View;
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
    private final List<DishSubmission> submissions = new ArrayList<>();
    private SubmissionAdapter adapter;
    private EditText announceTitle;
    private EditText announceContent;
    private View submissionEmptyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        announceTitle = findViewById(R.id.admin_announce_title);
        announceContent = findViewById(R.id.admin_announce_content);
        submissionEmptyText = findViewById(R.id.submission_empty_text);
        findViewById(R.id.admin_post_announce_button).setOnClickListener(v -> postAnnouncement());

        RecyclerView submissionList = findViewById(R.id.submission_list);
        submissionList.setLayoutManager(new LinearLayoutManager(this));
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

        fetchData();
    }

    private void fetchData() {
        NetworkClient.getService().getPendingSubmissions().enqueue(new Callback<ApiResponse<List<DishSubmission>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DishSubmission>>> call, Response<ApiResponse<List<DishSubmission>>> response) {
                submissions.clear();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().data != null) {
                    submissions.addAll(response.body().data);
                } else {
                    String message = response.body() == null ? "无法获取待审核菜品" : response.body().message;
                    UiUtils.toast(AdminDashboardActivity.this, message);
                }
                adapter.notifyDataSetChanged();
                updateEmptyState();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DishSubmission>>> call, Throwable t) {
                UiUtils.toast(AdminDashboardActivity.this, "网络错误");
                updateEmptyState();
            }
        });
    }

    private void handleAudit(long id, boolean approve) {
        AdminDtos.AuditRequest request = new AdminDtos.AuditRequest(approve ? "管理员审核通过" : "管理员驳回");
        Call<ApiResponse<Object>> call = approve
                ? NetworkClient.getService().approveSubmission(id, request)
                : NetworkClient.getService().rejectSubmission(id, request);

        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    UiUtils.toast(AdminDashboardActivity.this, approve ? "已批准" : "已驳回");
                    fetchData();
                } else {
                    String message = response.body() == null ? "审核失败" : response.body().message;
                    UiUtils.toast(AdminDashboardActivity.this, message);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                UiUtils.toast(AdminDashboardActivity.this, "网络错误");
            }
        });
    }

    private void postAnnouncement() {
        String title = announceTitle.getText().toString().trim();
        String content = announceContent.getText().toString().trim();
        if (title.isEmpty() || content.isEmpty()) {
            UiUtils.toast(this, "请填写完整公告信息");
            return;
        }

        NetworkClient.getService().createAnnouncement(new AdminDtos.AnnouncementRequest(title, content))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            UiUtils.toast(AdminDashboardActivity.this, "公告发布成功");
                            announceTitle.setText("");
                            announceContent.setText("");
                        } else {
                            String message = response.body() == null ? "公告发布失败" : response.body().message;
                            UiUtils.toast(AdminDashboardActivity.this, message);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        UiUtils.toast(AdminDashboardActivity.this, "网络错误");
                    }
                });
    }

    private void updateEmptyState() {
        if (submissionEmptyText != null) {
            submissionEmptyText.setVisibility(submissions.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }
}
