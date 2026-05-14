package edu.thu.canteen;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.MockRepository;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        RecyclerView submissionList = findViewById(R.id.submission_list);
        submissionList.setLayoutManager(new LinearLayoutManager(this));
        submissionList.setAdapter(new SubmissionAdapter(MockRepository.getAdminSubmissions()));

        RecyclerView supportList = findViewById(R.id.support_list);
        supportList.setLayoutManager(new LinearLayoutManager(this));
        supportList.setAdapter(new SupportAdapter(MockRepository.getSupportMessages()));

        findViewById(R.id.batch_upload_button)
                .setOnClickListener(v -> UiUtils.toast(this, "Batch upload"));
    }
}

