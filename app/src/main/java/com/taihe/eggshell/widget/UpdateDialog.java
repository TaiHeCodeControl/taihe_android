package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.entity.Professional;

import java.util.List;

/**
 * Created by wang on 2015/8/18.
 */
public class UpdateDialog extends Dialog{

    private Context mContext;

    private Button leftButton,rightButton;
    private TextView titleText;
    private List<String> msglist;
    private View.OnClickListener leftClickListener,rightClickListener;

    public UpdateDialog(Context context,List<String> list, View.OnClickListener leftListener, View.OnClickListener rightListener){
        super(context);

        this.mContext = context;
        this.msglist = list;
        this.leftClickListener = leftListener;
        this.rightClickListener = rightListener;
        initView();
    }

    private void initView(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_dialog);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        setCanceledOnTouchOutside(false);

        titleText = (TextView)findViewById(R.id.id_dialog_title);
        leftButton = (Button)findViewById(R.id.id_dialog_left);
        rightButton = (Button)findViewById(R.id.id_dialog_right);
        ListView listview = (ListView)findViewById(R.id.id_version_list);
        VersionAdapter adapter = new VersionAdapter(mContext);
        listview.setAdapter(adapter);

        leftButton.setOnClickListener(leftClickListener);
        rightButton.setOnClickListener(rightClickListener);
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(Button leftButton) {
        this.leftButton = leftButton;
    }

    public Button getRightButton() {
        return rightButton;
    }

    public void setRightButton(Button rightButton) {
        this.rightButton = rightButton;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    class VersionAdapter extends BaseAdapter {

        private Context context;

        public VersionAdapter(Context mcontext){
            this.context = mcontext;
        }

        @Override
        public int getCount() {
            return msglist.size();
        }

        @Override
        public Object getItem(int position) {
            return msglist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_version,null);
            TextView textView = (TextView)convertView.findViewById(R.id.textViewName);
            textView.setText(msglist.get(position));

            return convertView;
        }
    }
}
