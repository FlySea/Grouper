package cn.com.libbasic.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 刷新加载中的
 *
 */
public class BaseRefreshAdapter extends BaseAdapter {

    @SuppressWarnings("rawtypes")
    public List list;
    public Context ctx;
    public Activity act;

    public BaseRefreshAdapter(Context con) {
        ctx = con;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        if (list != null) {
            list.remove(position);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}