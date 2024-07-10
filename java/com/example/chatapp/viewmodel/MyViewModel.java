package com.example.chatapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.Repository.Repository;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.model.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    // auth
    public void sighUpAnonymousUser() {

        Context c = this.getApplication();
        repository.firebaseAnonymousAuth(c);
    }

    public String getCurrentUserId() {

        return repository.getCurrentUserID();
    }

    public void signOut() {

        repository.signOUT();
    }


    // getting chat group
    public MutableLiveData<List<ChatGroup>> getGroupList() {

        return repository.getChatGroupMutableLiveData();
    }

    // creating the new chatgroup
    public void createNewGroup(String groupName) {

        repository.createNewChatGroup(groupName);
    }

    // messages
    public MutableLiveData<List<ChatMessage>> getMessageLiveData(String groupName) {

        return repository.getMessageLiveData(groupName);
    }

    public void sendMessage(String msg, String chatGroup) {

        repository.sendMessage(msg, chatGroup);
    }
}
