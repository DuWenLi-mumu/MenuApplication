package com.example.dwl.menuapplication.IM;

//import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dwl.menuapplication.R;
//import com.example.dwl.menuapplication.activity.UserPage;
import com.example.dwl.menuapplication.bean.User;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class DialogActivity extends AppCompatActivity {

    protected DialogsListAdapter<Dialog> dialogsAdapter;
    private DialogsList dialogsList;
    private String ChatTitle;
    private String ChatAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        dialogsList = findViewById(R.id.dialogsList);

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //Bitmap bitmap = StringToBitMap(url);
                Glide.with(DialogActivity.this).load(url).into(imageView);
            }
        };

        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsList.setAdapter(dialogsAdapter);
        //BmobIM.getInstance().clearAllConversation();
        getDialogs();
        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                Intent intent = new Intent(DialogActivity.this,MessageActivity.class);
                intent.putExtra("Chatid",dialog.getId());
                Log.e("chatid",dialog.getId());
                intent.putExtra("ChatAvatar",dialog.getDialogPhoto());
                intent.putExtra("ChatName",dialog.getDialogName());
                //intent.putExtra("msg",dialog.getLastMessage());
                startActivity(intent);
                //Log.e("nihao",dialog.getLastMessage().getText());
            }
        });
        dialogsAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(Dialog dialog) {
                BmobIM.getInstance().deleteConversation(dialog.getId());
                Toast.makeText(DialogActivity.this,"delete!",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getDialogs() {
        final int[] i = {0};
        final ArrayList<Dialog> chats = new ArrayList<>();
        final List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        //Log.e("num", String.valueOf(list.size()));
        if(list != null && list.size()>0){
            for (final BmobIMConversation item:list){
                if(item.getMessages().size() == 0) {
                    i[0]++;
                    continue;
                }
                BmobQuery<User> bmobQuery = new BmobQuery<User>();
                Log.e("title", item.getConversationTitle());
                bmobQuery.getObject(item.getConversationTitle(), new QueryListener<User>() {
                    @Override
                    public void done(User object,BmobException e) {
                        i[0]++;
                        if(e==null){
                            ChatTitle = object.getUsername();
                            ChatAvatar = object.getPerson_pic();
                            chats.add(getDialog(new Date(item.getUpdateTime()), item, ChatTitle, ChatAvatar));
                            if (i[0] == list.size()) {
                                dialogsAdapter.setItems(chats);
                            }
                            //toast("查询成功");
                        }else{
                            chats.add(getDialog(new Date(item.getUpdateTime()), item, item.getConversationTitle(), item.getConversationIcon()));
                            Log.e("i", String.valueOf(i[0]));
                            if (i[0] == list.size()) {
                                dialogsAdapter.setItems(chats);
                            }
                            //Log.e("null",item.getConversationTitle());
                        }
                    }
                });
            }
        }

    }

    private Dialog getDialog(Date lastMessageCreatedAt,BmobIMConversation item,String ChatTitle,String ChatAvatar) {
        return new Dialog(item.getConversationTitle(), ChatTitle, ChatAvatar, new ArrayList<Author>(), new Message("2", new Author(item.getMessages().get(0).getFromId(), item.getConversationTitle(), item.getConversationIcon(), true), item.getMessages().get(0).getContent(), lastMessageCreatedAt), item.getUnreadCount());
    }
}
