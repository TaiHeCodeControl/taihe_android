package com.taihe.eggshell.base.utils.httprequest;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * 三合一的网络链接类7月14删除不必要的代码
 *
 * @创建时间 2014-7-14下午3:33:20
 */
public abstract class MSCOpenUrlRunnable extends Handler implements Runnable {


    public void onControl(MSCJSONObject jsonObject) throws JSONException {

        String errcode;

        if ((errcode = jsonObject.optString("errcode")).length() == 4) {
//			MSCTool.NowActivity.viewTool.getResult(errcode);
        }

    }


    public void onControl(Bitmap obj) {

    }

    /** 访问失败调用的方法 **/
    public void onStop(Exception e) {
        if (e == null) {
            e = new Exception("null:e");
        }
//		MSCTool.NowActivity.toast("onStop:" + e.getMessage());
    }

    private MSCUrlManager url;
    private int type;

    /***
     * @param url
     *            网址链接<br>
     * @param
     *
     * @see
     * **/
    public MSCOpenUrlRunnable(MSCUrlManager url, int type) {
        this.url = url;
        this.type = type;
    }

    public static final int 图片类型 = 123;
    public static final int post类型 = 111;

    /***
     * @param url
     *            网址链接<br>
     *
     * **/
    public MSCOpenUrlRunnable(MSCUrlManager url) {
        System.out.println(url.toString());
        this.url = url;
        this.type = 0;

        //this.urlParams = getloginmode(urlParams);
    }

    public List<MSCPostUrlParam> urlParams;

    /***
     * @param url
     *            网址链接<br>
     * @param
     *
     * @see
     *
     *
     * **/
    public MSCOpenUrlRunnable(MSCUrlManager url, List<MSCPostUrlParam> urlParams) {
        this.url = url;
        this.type = post类型;
        this.urlParams = getloginmode(urlParams);
        //System.out.println(url.toString());
    }


    private List<MSCPostUrlParam> getloginmode(List<MSCPostUrlParam> truemode) {

//		if (MSCTool.user == null || MSCTool.user.token == null) {
//			MSCTool.user = new User();
//		}
//		if (MSCTool.user.token.length() > 5) {
//			MSCPostUrlParam token = new MSCPostUrlParam("token",
//					MSCTool.user.token);
//			truemode.add(token);
//		}

        return truemode;

    }

    @Override
    public void run() {
//		if (MSCTool.NowActivity.viewTool.GetWebType() == 0) {
//			Dialog_tool.duanwang(MSCTool.NowActivity, this);
//			return;
//		}
//
//		MSCTool.ShowDialog();

        Message msg = new Message();
        switch (type) {

            // Post专用连接
            case post类型:
                try {
                    msg.obj = MSCUrlManager.NewopenpostUrlreString(url.GetUrl(),
                            urlParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 图片类型:

                try {
                    msg.obj = getfileforbitmap(url);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            default:
                JSONObject jsonobject = new JSONObject();
                try {
                    jsonobject = new JSONObject(MSCUrlManager.openUrlreString(url
                            .GetUrl()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg.obj = jsonobject;
                break;
        }
        sendMessage(msg);
//		MSCTool.DismissDialog();
    }

    private Bitmap getfileforbitmap(MSCUrlManager imgurl) throws Exception {
        InputStream sre = imgurl.GetUrl().openStream();
        Bitmap bitmap = BitmapFactory.decodeStream(sre);
        sre.close();
        return bitmap;

    }

    public void chongshi() {
        run();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (type) {
            case 图片类型:

                onControl((Bitmap) msg.obj);

                break;

            default:
                JSONObject jsonObject = (JSONObject) msg.obj;

                try {
                    onControl(new MSCJSONObject(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace();
                    onStop(e);
                }

                break;
        }

        super.handleMessage(msg);
    }

}