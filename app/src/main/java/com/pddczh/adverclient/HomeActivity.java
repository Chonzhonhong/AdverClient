package com.pddczh.adverclient;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/12/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.tv_image)
    TextView tvImage;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_imgurl)
    TextView tvImgurl;
    @BindView(R.id.tv_videourl)
    TextView tvVideourl;
    @BindView(R.id.image)
    ImageView image;

    @Override
    public int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

    }


    @OnClick({R.id.tv_text, R.id.tv_image, R.id.tv_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_text:
                if (etContent.getText().toString().isEmpty()) {
                    showToast("不能为空");
                } else {
                    //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                    EMMessage message = EMMessage.createTxtSendMessage(etContent.getText().toString(), "czh999");
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(message);
                }
                break;
            case R.id.tv_image:
                    //打开相册
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                    intent.setType("image/*");
                    startActivityForResult(intent, 102);
                break;
            case R.id.tv_video:

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 66);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 102:

                if (resultCode == RESULT_OK) { // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case 66:
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String VIDEOPATH = cursor.getString(columnIndex);
                cursor.close();
                tvVideourl.setText(VIDEOPATH);
                EMMessage message = EMMessage.createVideoSendMessage(VIDEOPATH, null, 0, "czh999");
                EMClient.getInstance().chatManager().sendMessage(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        LogUtils.e("成功");
                    }

                    @Override
                    public void onError(int code, String error) {
                        LogUtils.e("onError",code,error);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        LogUtils.e("onProgress",progress,status);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void handleImageOnKitKat(Intent data) {
        LogUtils.e(data.getData()+"***");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // 根据图片路径显示图片
        LogUtils.e(imagePath+"===============");
        displayImage(imagePath);

    }

    /**
     * android 4.4以前的处理方式
     *
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        image.setImageURI(uri);
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        LogUtils.a(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            LogUtils.e(imagePath+"11111111111");
            tvImgurl.setText(imagePath);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            image.setImageBitmap(bitmap);
            //imageUri为图片本地资源标志符，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
            EMMessage message = EMMessage.createImageSendMessage(imagePath, false, "czh999");
            EMClient.getInstance().chatManager().sendMessage(message);
            message.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    LogUtils.e("成功");
                }

                @Override
                public void onError(int code, String error) {
                    LogUtils.e("onError",code,error);
                }

                @Override
                public void onProgress(int progress, String status) {
                    LogUtils.e("onProgress",progress,status);
                }
            });
        } else {
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
