package com.example.chatapp.Repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.views.GroupsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    MutableLiveData<List<ChatGroup>> chatGroupMutableLiveData;

    FirebaseDatabase database;
    DatabaseReference reference;

    // database reference for messages
    DatabaseReference groupReference;

    // mutable livedata to monitor messages
    MutableLiveData<List<ChatMessage>> messageLiveData;

    public Repository() {

        this.chatGroupMutableLiveData = new MutableLiveData<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();// the root reference

        messageLiveData = new MutableLiveData<>();
    }

    // Auth
    public void firebaseAnonymousAuth(Context context) {

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // Authentication is successful
                        Intent i = new Intent(context, GroupsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                    }
                });
    }

    // getting current userID
    public String getCurrentUserID() {
        return FirebaseAuth.getInstance().getUid();
    }

    // signOut funtion
    public void signOUT() {

        FirebaseAuth.getInstance().signOut();
    }

    // Getting chatGroups available from Firebase realtime database


    public MutableLiveData<List<ChatGroup>> getChatGroupMutableLiveData() {

        List<ChatGroup> groupList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groupList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ChatGroup group = new ChatGroup(dataSnapshot.getKey());
                    groupList.add(group);
                }

                chatGroupMutableLiveData.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatGroupMutableLiveData;
    }

    // creating a new group in the firebase database
    public void createNewChatGroup(String groupName) {

        reference.child(groupName).setValue(groupName);
    }

    // getting messages from firebase realtime database using message LiveData
    public  MutableLiveData<List<ChatMessage>> getMessageLiveData(String groupName) {

        // child(groupName) : used to specify a child node under the root reference
        groupReference = database.getReference().child(groupName);

        List<ChatMessage> messageList = new ArrayList<>();

        // fetching the data from the database
        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }

                messageLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return messageLiveData;

    }

    // sending messages to firebase realtime database
    public void sendMessage(String messageText, String chatGroup) {

        DatabaseReference ref = database.getReference(chatGroup);

        if(!messageText.trim().equals("")) {

            ChatMessage msg = new ChatMessage(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    messageText,
                    System.currentTimeMillis()
            );

            //setting a random key in the database to store the message
            String randomKey = ref.push().getKey();
            // storing the msg in form of value to the key in the database
            ref.child(randomKey).setValue(msg);


        }
    }

}
