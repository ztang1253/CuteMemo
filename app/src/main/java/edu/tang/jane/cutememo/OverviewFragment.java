package edu.tang.jane.cutememo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.tang.jane.cutememo.DB.JTDBConstant;
import edu.tang.jane.cutememo.DB.JTMemoEntity;



public class OverviewFragment extends Fragment {
    private ListView mList;
    private MainActivity mParent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_overview_tab, container, false);
        mList = v.findViewById(R.id.overview_list);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParent = (MainActivity) getActivity();
        ArrayList<JTMemoEntity> data = mParent.getAllData();
        List<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
        int i = 0;
        for (JTMemoEntity entity : data) {
            HashMap<String, String> obj = new HashMap<String, String>();
            obj.put("day", entity.year + "-" + (entity.month + 1) + "-" + entity.day);
            obj.put("details", "Slept " + entity.sleep + " hours. Had " + JTDBConstant.dinnerEnum2DinnerCt(entity.dinner) + " meals, " + entity.sports + " hour(s) sports.");
            listData.add(obj);
            i++;
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), listData, R.layout.overview_list_item, new String[]{ "day", "details" }, new int[]{ R.id.item_date, R.id.item_details });
        mList.setAdapter(adapter);
        mList.setItemsCanFocus(false);
        mList.setClickable(true);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mParent.setCurrentData(i);
                mParent.go2FragmentByTag(MainActivity.TAG_DETAILS);
            }
        });
    }
}
