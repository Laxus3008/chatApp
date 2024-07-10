package com.example.chatapp.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityGroupsBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.viewmodel.MyViewModel;
import com.example.chatapp.views.adapters.GroupAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroupArrayList;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ActivityGroupsBinding binding;
    private MyViewModel myViewModel;

    // Dialog
    private Dialog chatGroupDialog;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_groups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_groups);

        // define ViewModel
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // recyclerView with data binding
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setup an observer to listen for changes in a LiveData object
        myViewModel.getGroupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                // the updated data is received as "chatGroups" parameter in onChanged()

                chatGroupArrayList = new ArrayList<>();
                chatGroupArrayList.addAll(chatGroups);

                groupAdapter = new GroupAdapter(chatGroupArrayList);
                recyclerView.setAdapter(groupAdapter);
                groupAdapter.notifyDataSetChanged();
            }
        });

        // fab button function call
        fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }


    public void showDialog() {

        chatGroupDialog = new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_layout, null);

        chatGroupDialog.setContentView(view);
        chatGroupDialog.show();

        Button submit = view.findViewById(R.id.submit_btn);
        EditText edt = view.findViewById(R.id.chat_group_edt);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = edt.getText().toString();

                Toast.makeText(GroupsActivity.this,
                        "your chat group: " + groupName,
                        Toast.LENGTH_SHORT).show();

                myViewModel.createNewGroup(groupName);

                chatGroupDialog.dismiss();
            }
        });
    }
}