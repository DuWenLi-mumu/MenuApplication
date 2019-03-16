package com.example.imagepicker1.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.isseiaoki.simplecropview.CropImageView;
import com.example.imagepicker1.R;
import com.example.imagepicker1.utils.CropUtil;
import com.example.imagepicker1.utils.FileUtilsIP;
import com.weimu.universalview.core.toolbar.StatusBarManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ImageCropActivity extends SelectorBaseActivity {
    public static final int REQUEST_CROP = 69;

    public static final String DATA_EXTRA_PATH = "data_extra_path";
    public static final String OUTPUT_PATH = "outputPath";

    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;

    private ImageView ivBg;
    private CropImageView cropImageView;


    private Uri sourceUri;//源URI
    private Uri saveUri;//存储URI

    private final Handler handler = new Handler();

    private ToolBarManager toolBarManager;

    public static Intent newIntent(Context context, String path) {
        Intent intent = new Intent(context, ImageCropActivity.class);
        intent.putExtra(DATA_EXTRA_PATH, path);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        initBase();
        initView();
    }

    private void initBase() {
        //data
        String path = getIntent().getStringExtra(DATA_EXTRA_PATH);
        sourceUri = Uri.fromFile(new File(path));
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        //crop setup
        cropImageView.setHandleSizeInDp(8);//设置裁剪四周小圆球的大小
        cropImageView.setFrameStrokeWeightInDp(1);
        cropImageView.setGuideStrokeWeightInDp(1);
        cropImageView.setInitialFrameScale(0.5f);//裁剪区域为原图的一半
        cropImageView.setCropMode(CropImageView.CropMode.SQUARE);//设置裁剪方式为圆形，可换
    }

    public void initView() {


        StatusBarManager.INSTANCE.setColorPro(this, R.color.white);
        toolBarManager = ToolBarManager.with(this, getContentView())
                .setBackgroundColor(R.color.white)
                .setTitle(getString(R.string.crop_picture))
                .setNavigationIcon(R.drawable.toolbar_arrow_back_black)
                .setMenuTextContent(getString(R.string.use))
                .setMenuTextColors(R.color.black_text_selector)
                .setMenuTextEnable(true)
                .setMenuTextClick(new ToolBarManager.OnMenuTextClickListener() {
                    @Override
                    public void onMenuTextClick() {
                        //点击完成
                        //ProgressDialog.show(ImageCropActivity.this, null, getString(R.string.save_ing), true, false);
                        saveUri = Uri.fromFile(FileUtilsIP.createCropFile(ImageCropActivity.this));
                        saveOutput(cropImageView.getCroppedBitmap());
                    }
                });

        Glide.with(this).asBitmap().load(sourceUri).into(ivBg);
        //获取源图片的旋转角度
        int exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri));

        InputStream is = null;
        try {
            int sampleSize = calculateBitmapSampleSize(sourceUri);
            is = getContentResolver().openInputStream(sourceUri);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = sampleSize;
            Bitmap sizeBitmap = BitmapFactory.decodeStream(is, null, option);
            if (sizeBitmap == null) return;
            Matrix matrix = getRotateMatrix(sizeBitmap, exifRotation % 360);
            Bitmap rotated = Bitmap.createBitmap(sizeBitmap, 0, 0, sizeBitmap.getWidth(), sizeBitmap.getHeight(), matrix, true);
            cropImageView.setImageBitmap(rotated);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            CropUtil.closeSilently(is);
        }
    }


    public Matrix getRotateMatrix(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        if (bitmap != null && rotation != 0) {
            int cx = bitmap.getWidth() / 2;
            int cy = bitmap.getHeight() / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(rotation);
            matrix.postTranslate(cx, cy);
        }
        return matrix;
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        } finally {
            CropUtil.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CropUtil.closeSilently(outputStream);
            }
            setResult(RESULT_OK, new Intent().putExtra(OUTPUT_PATH, saveUri.getPath()));
        }
        final Bitmap b = croppedImage;
        handler.post(new Runnable() {
            public void run() {
                b.recycle();
            }
        });
        onBackPressed();
    }
}
