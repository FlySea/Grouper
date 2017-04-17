package cn.com.libbasic.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.libbasic.R;

/**
 * 刷新加载时空数据的默认view
 *
 */
public class ListNullView extends LinearLayout {

    TextView text;

    public ListNullView(Context context) {
        super(context);
        init();
    }

    public ListNullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.list_null_view, this, true);
        text = (TextView) viewGroup.findViewById(R.id.tip);

    }

    public void setTip(int resid) {
        text.setText(resid);
    }

    public void setTip(String con) {
        text.setText(con);
    }

}
