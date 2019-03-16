package com.example.dwl.menuapplication.IM;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

public class DemoMessageHandler extends BmobIMMessageHandler{
    private MessagesListAdapter<IMessage> adapter;
    private MessagesList messagesList;
    @Override
    public void onMessageReceive(final MessageEvent event) {
        EventBus.getDefault().post(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
    }

    private Message addMessage(String userId, String text) {
        Author user = new Author(userId, "Tobi", "http://i.imgur.com/pv1tBmT.png", true);
        return new Message(userId, user, text, new Date());
    }

}
