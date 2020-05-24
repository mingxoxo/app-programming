package kr.ac.sejong.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {

    //Data를 내부 변수로 받음
    private ArrayList<MyListViewItem> data = new ArrayList<MyListViewItem>();
    //MyListViewItem[] data;와 같은 의미

    public ArrayList<MyListViewItem> getData() {
        return data;
    }

    public void setData(ArrayList<MyListViewItem> data) {
        this.data = data;
    }

    //data의 갯수
   @Override
    public int getCount() {
       //return data.length
       return data.size();
    }

    //각각의 item
    @Override
    public Object getItem(int position) {
       //return data[position];
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //data가 바뀔때마다 UI에 바꿔주는
    //시점은 data가 변동이 있을때마다(추가, 삭제 등등)

    /**
     * position에 위치한 데이터를 listView_item을 팽창(inflate)한 객체에 담아서 반환. (xml->java)
     * @param position
     * @param convertView listView_item
     * @param parent listView
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. 만약 view가 아직 없다면 inflate 시킨다.
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        //2. listview_item에 포함된 view들 (예: imageview, textview)를 변수로 잡기
        ImageView profilePictureImageView = convertView.findViewById(R.id.profilePicture);
        TextView nameTextView = convertView.findViewById(R.id.name);
        TextView idTextView = convertView.findViewById(R.id.identifier);
        TextView updateTimeTextView = convertView.findViewById(R.id.updateTime);
        TextView tweetTextView = convertView.findViewById(R.id.tweet);

        //3. data의 item 가져오기
        MyListViewItem item = data.get(position);

        //4. view에 data를 반영한다.
        profilePictureImageView.setImageDrawable(item.getProfilePicture());
        nameTextView.setText(item.getName());
        idTextView.setText(item.getIdentifier());
        updateTimeTextView.setText(item.getUpdateTime());
        tweetTextView.setText(item.getTweet());

        return convertView;
    }
}
