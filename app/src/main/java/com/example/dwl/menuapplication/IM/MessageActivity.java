package com.example.dwl.menuapplication.IM;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.example.dwl.menuapplication.Glide4Engine;
import com.example.dwl.menuapplication.MyApp;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.utils.StatusBarUtil;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;


public class MessageActivity extends AppCompatActivity {

    private MessagesList messagesList;
    private MessageInput mMessageInput;
    private MessagesListAdapter<IMessage> adapter;
    private Author user;
    //private Message mMessage;
    private List<Uri> result;
    private static final int REQUEST_CODE_CHOOSE = 23;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    //BmobIMUserInfo info;
    BmobIMConversation mBmobIMConversation;
    private String Input;
    BmobIMUserInfo info = new BmobIMUserInfo();
    String Chatid;
    String ChatAvatar;
    String ChatName;
    BmobIMMessage msg;
    private TextView chat_name;
    private List<BmobIMMessage> mMessage = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_immessage);
        ButterKnife.bind(this);

        Chatid = getIntent().getStringExtra("Chatid");
        Log.e("id",Chatid);
        ChatAvatar = getIntent().getStringExtra("ChatAvatar");
        ChatName = getIntent().getStringExtra("ChatName");
        chat_name = findViewById(R.id.name_message_person);
        chat_name.setText(ChatName);

        info.setAvatar(ChatAvatar);
        info.setUserId(Chatid);
        info.setName(ChatName);

        //queryMessages(null);
        messagesList = findViewById(R.id.messagesList);
        mMessageInput = findViewById(R.id.input);

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //Bitmap bitmap = StringToBitMap(url);
                Glide.with(MessageActivity.this).load(url).into(imageView);
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new MessagesListAdapter<>("1", imageLoader);
        messagesList.setAdapter(adapter);
        initIM();

//        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
//        Log.e("listsize", String.valueOf(list.size()));
//        if(list!=null && list.size()>0){
//            for (final BmobIMConversation item:list) {
//                if(item.getConversationId().equals(Chatid)){
//                    msg = item.getMessages().get(0);
//                    Log.e("msg","yes");
//                }
//            }
//        }
        if(mMessage.size() > 0)
            queryMessages(mMessage.get(0));
        else
            queryMessages(null);
        mMessageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Input = input.toString();
                BmobIMTextMessage msg = new BmobIMTextMessage();
                msg.setContent(Input);
                mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            Log.e("yes", "yes");
                            adapter.addToStart(addMessage("1", Input, new Date()), true);
                        } else {
                        }
                    }
                });


//                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
//                    @Override
//                    public void done(BmobIMConversation c, BmobException e) {
//                        if (e == null) {
//                            //isOpenConversation = true;
//                            //在此跳转到聊天页面或者直接转化
//                            mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
//                            //tv_message.append("发送者："+et_message.getText().toString()+"\n");
//                            BmobIMTextMessage msg = new BmobIMTextMessage();
//                            msg.setContent(Input);
//                            mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
//                                @Override
//                                public void done(BmobIMMessage msg, BmobException e) {
//                                    if (e == null) {
//                                        Log.e("yes", "yes");
//                                        adapter.addToStart(addMessage("1", Input), true);
//                                    } else {
//                                    }
//                                }
//                            });
//                        } else {
//                            //Toast.makeText(MainActivity.this, "开启会话出错", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                //adapter.addToStart(addMessage("2",input.toString()), true);
                return true;
            }
        });

        mMessageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                Matisse.from(MessageActivity.this)
                        .choose(MimeType.ofAll())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(1)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.example.dwl.menuapplication.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new Glide4Engine())//图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE);//
                //dispatchTakePictureIntent();
//                adapter.addToStart(getImageMessage(), true);
            }
        });

        mMessageInput.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {

            }

            @Override
            public void onStopTyping() {

            }
        });

        EventBus.getDefault().register(this);
    }

    private void initIM() {
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
    }


    //MessageKit.logIt("Awesome");
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        Log.e("userid",event.getFromUserInfo().getUserId());
        if(event.getFromUserInfo().getUserId().equals(Chatid))
            adapter.addToStart(addMessage("2", event.getMessage().getContent(), new Date()), true);
        //BmobNotificationManager.getInstance(MessageActivity.this).showNotification(event,pendingIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private Message addMessage(String userId, String text, Date date) {
        user = new Author(userId, ChatName, ChatAvatar, true);
        return new Message(userId, user, text, date);
    }

    public static Message getImageMessage() {
        Message message = new Message("1", new Author("2", "Tobi", "http://i.imgur.com/pv1tBmT.png", true), null);
        message.setImage(new Message.Image("https://cdn.pixabay.com/photo/2017/12/25/17/48/waters-3038803_1280.jpg"));
        return message;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            result = Matisse.obtainResult(data);
            Log.e("-----reason", result.get(0).toString());
        }
    }

    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null) realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }

        return realPath;
    }

    public void queryMessages(BmobIMMessage msg) {
        mBmobIMConversation.queryMessages(msg, 2, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                Log.e("query","yes");
                //sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        for(int i = 0;i < list.size();i++){
                            BmobIMMessage message = list.get(i);
                            mMessage.add(message);
                            Log.e("id",list.get(i).getConversationId());
                            if(list.get(i).getFromId().equals(Chatid))
                                adapter.addToStart(addMessage("2",list.get(i).getContent(),new Date(list.get(i).getCreateTime())),true);
                            else
                                adapter.addToStart(addMessage("1",list.get(i).getContent(),new Date(list.get(i).getCreateTime())),true);

                            //adapter.addToStart(addMessage(message.getFromId(),message.getContent()),true);
                        }
                        //Log.e("no",e.toString());
//                        adapter.addMessages(list);
//                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
//                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                    Log.e("no",e.toString());
                }
            }
        });
    }
}

