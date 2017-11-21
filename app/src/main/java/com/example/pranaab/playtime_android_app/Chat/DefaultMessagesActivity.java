package com.example.pranaab.playtime_android_app.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pranaab.playtime_android_app.Chat.fixtures.MessagesFixtures;
import com.example.pranaab.playtime_android_app.Chat.model.Message;
import com.example.pranaab.playtime_android_app.Chat.util.AppUtils;
import com.example.pranaab.playtime_android_app.R;
import com.example.pranaab.playtime_android_app.WebSocket.ChatWebSocketListener;
import com.example.pranaab.playtime_android_app.WebSocket.SingletonWebSocketListener;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranaab on 2017-11-20.
 */

public class DefaultMessagesActivity extends ChatActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener {

    private ChatWebSocketListener webSocketListener;

    public static void open(Context context) {
        context.startActivity(new Intent(context, DefaultMessagesActivity.class));
    }

    private MessagesList messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        this.webSocketListener = SingletonWebSocketListener.get_Instance();
        this.webSocketListener.chatActivity = this;

        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();

        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        webSocketListener.send_chat_message(input.toString());
        super.messagesAdapter.addToStart(
                MessagesFixtures.getSendTextMessage(input.toString()),true);
        return true;
    }

    @Override
    public void onAddAttachments() {
        super.messagesAdapter.addToStart(
                MessagesFixtures.getImageMessage(), true);
    }

    public void showReceivedText(String text){
        //List<Message> messages = new ArrayList<>();
        //messages.add(MessagesFixtures.getMessageFromText(text));
        Log.i("ACTIVITY", "Here");
        //Log.i("ACTIVITY", messages.get(0).getText());
        super.messagesAdapter.addToStart(MessagesFixtures.getReceiveTextMessage(text),true);
    }

    private void initAdapter() {
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        super.messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, Message message) {
                        AppUtils.showToast(DefaultMessagesActivity.this,
                                message.getUser().getName() + " avatar click",
                                false);
                    }
                });
        this.messagesList.setAdapter(super.messagesAdapter);
    }
}
