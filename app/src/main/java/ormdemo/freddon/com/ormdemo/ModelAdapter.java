package ormdemo.freddon.com.ormdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 16/7/8.
 */
public class ModelAdapter extends BaseAdapter {

    private Context mContext;
    private List<Model> mData;

    public ModelAdapter(Context context, List<Model> mData) {
        this.mContext = context;
        if (mData == null) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = mData;
        }
    }

    public void notifyData(List<Model> mData){
        if(mData==null){
            this.mData.clear();
        }else{
            this.mData=mData;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mData.size();
    }

    @Override
    public Model getItem(int position) {
        return this.mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Model item = getItem(position);
        vh.title.setText(item.toString());
        vh.title.setTag(item);
        vh.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(modelClickListener!=null){
                    modelClickListener.onLongClick(v.getTag());
                    return true;
                }
                return false;
            }
        });
        vh.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelClickListener!=null){
                    modelClickListener.onClick(v.getTag());
                }
            }
        });
        return convertView;
    }

    public void setModelClickListener(ModelClick modelClickListener) {
        this.modelClickListener = modelClickListener;
    }

    ModelClick modelClickListener;
    public interface ModelClick{

        void onLongClick(Object tag);

        void onClick(Object tag);
    }

    static class ViewHolder {

        public TextView title;

        public ViewHolder() {

        }
    }
}
