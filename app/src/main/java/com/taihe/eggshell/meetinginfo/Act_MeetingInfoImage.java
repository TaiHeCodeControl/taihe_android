package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Act_MeetingInfoImage extends BaseActivity {
    private ImageView meetinginfo_img;
    private Context mContext;
    private String title,pic,content;
    private TextView meetinginfo_content;
    private String url = "";
    private String id;

    @Override
    public void initView() {
        setContentView(R.layout.activity_meetinginfo_info);
        super.initView();
        mContext = this;
        meetinginfo_img = (ImageView)findViewById(R.id.meetinginfo_img);
        meetinginfo_content = (TextView)findViewById(R.id.meetinginfo_content);
    }

    public void initData(){
        super.initData();

        id = getIntent().getStringExtra("rqCode");
        title = getIntent().getStringExtra("title");
        pic = getIntent().getStringExtra("pic");
//        pic = "http://ww3.sinaimg.cn/bmiddle/7085f3c4jw1f0cnp79j6yj20k00qo409.jpg";
        content = getIntent().getStringExtra("content");
        initTitle(title);
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(meetinginfo_img,pic);
        meetinginfo_content.setText(Html.fromHtml(content));

        url = Urls.getMopHostUrl()+"/Share/shareqrcode?id="+id;

        meetinginfo_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showCancelDialog();
                return false;
            }
        });

    }

    public void showCancelDialog() {

        final UMImage im = new UMImage(mContext,pic);

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_share_pic, null);
        TextView shareqq = (TextView) view.findViewById(R.id.id_share_qq);
        TextView sharewechat = (TextView) view.findViewById(R.id.id_share_wechat);
        TextView savepic = (TextView) view.findViewById(R.id.id_save_phone);
        TextView cancel = (TextView) view.findViewById(R.id.tvCancel);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mystyle);
        // 在底部显示
        window.showAtLocation(meetinginfo_img, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        window.setOnDismissListener(new poponDismissListener());
        shareqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                new ShareAction(Act_MeetingInfoImage.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                        .withTitle(title)
                        .withText(content)
                        .withTargetUrl(url)
                        .withMedia(im)
                        .share();
            }
        });
        sharewechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                new ShareAction(Act_MeetingInfoImage.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle(title)
                        .withText(content)
                        .withTargetUrl(url)
                        .withMedia(im)
                        .share();

            }
        });

        savepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Drawable drawable = meetinginfo_img.getDrawable();
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();
                Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                Bitmap bitmap = Bitmap.createBitmap(w,h,config);
                //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, w, h);
                drawable.draw(canvas);
                savePic(bitmap);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void savePic(Bitmap bitmap){

        long timestap = System.currentTimeMillis();
        File qrCode = new File(Environment.getExternalStorageDirectory() + "/eggker/");
        if(!qrCode.exists()){
            qrCode.mkdir();
        }
        File file = new File(qrCode,timestap+".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.flush();
            out.close();
            ToastUtils.show(mContext,"保存成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
