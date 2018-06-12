package rkgit.com.geoattendance.modules.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.models.ChatMessage;
import rkgit.com.geoattendance.modules.adapter.EmployeeChatDataAdapter;
import rkgit.com.geoattendance.utility.GeoPreference;

/**
 * Created by Ayush Kulshrestha
 * on 29-03-2018.
 */

public class ChatActivity extends AppCompatActivity {

    EditText messageInputText;
    Button send;
    private DatabaseReference databaseReference;
    private RecyclerView messageList;
    FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> FBRA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send = findViewById(R.id.send_button);
        messageInputText = findViewById(R.id.message_input_text);
        messageList = findViewById(R.id.recycler_view);
        setupToolbar();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message");
        databaseReference.keepSynced(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageInputText != null && messageInputText.length() > 0) {
                    String message = messageInputText.getText().toString();
                    DatabaseReference newPost = databaseReference.push();
                    newPost.child("sender").setValue(GeoPreference.getInstance().getUsername());
                    newPost.child("content").setValue(message);
                    newPost.child("time").setValue(getTime());
                    newPost.child("date").setValue(getDate());
                    messageInputText.setText("");
                }
            }
        });
    }

    private String getDate() {
        String date = "";
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        date = dateFormat.format(new Date());
        return date;
    }


    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Employee Chat Room");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setValueEventListener();
    }

    private void setValueEventListener() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatMessage> responseDataList = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String message = (String) messageSnapshot.child("content").getValue();
                    String sender = (String) messageSnapshot.child("sender").getValue();
                    String time = (String) messageSnapshot.child("time").getValue();
                    String date = "23 Apr 2018";
                    if (messageSnapshot.child("date") != null)
                        date = (String) messageSnapshot.child("date").getValue();
                    ChatMessage chatMessage = new ChatMessage(message, sender, time, date);
                    responseDataList.add(chatMessage);
                }
                setDataToRecyclerView(responseDataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Chat server is under maintenance, \nPlease try after some times.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        databaseReference.addValueEventListener(postListener);
    }

    private void setDataToRecyclerView(List<ChatMessage> responseDataList) {
        EmployeeChatDataAdapter adapter = (EmployeeChatDataAdapter) messageList.getAdapter();
        if (adapter == null) {
            adapter = new EmployeeChatDataAdapter(this, responseDataList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            messageList.setLayoutManager(linearLayoutManager);
            messageList.setHasFixedSize(true);
            messageList.setAdapter(adapter);
        } else {
            adapter.updateAdapter(responseDataList);
            messageList.scrollToPosition(responseDataList.size() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getTime() {
        String time = "";
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        time = dateFormat.format(new Date());
        return time;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView userName, message, time;

        public MessageViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.username);
            message = view.findViewById(R.id.message_text_view);
            time = view.findViewById(R.id.time);
        }
    }
}
