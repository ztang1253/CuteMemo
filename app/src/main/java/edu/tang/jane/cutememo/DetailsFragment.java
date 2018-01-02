package edu.tang.jane.cutememo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.tang.jane.cutememo.DB.JTDBConstant;
import edu.tang.jane.cutememo.DB.JTMemoEntity;



public class DetailsFragment extends Fragment {
    private MainActivity mParent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParent = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.activity_details_tab, container, false);
        Button btn = v.findViewById(R.id.details_edit);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       mParent.go2FragmentByTag(MainActivity.TAG_TODAY);
                                   }
                               }
        );

        btn = v.findViewById(R.id.details_delete);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       mParent.removeCurrentData();
                                       Toast.makeText(mParent, "Memo deleted", Toast.LENGTH_SHORT).show();
                                       mParent.go2FragmentByTag(MainActivity.TAG_OVERVIEW);
                                   }
                               }
        );

        btn = v.findViewById(R.id.details_back);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       mParent.go2FragmentByTag(MainActivity.TAG_OVERVIEW);
                                   }
                               }
        );

        JTMemoEntity entity = mParent.getCurrentData();
        TextView data = v.findViewById(R.id.details_tv_date);
        data.setText(entity.year + " - " + (entity.month + 1) + " - " + entity.day);

        data = v.findViewById(R.id.details_tv_others);
        StringBuilder sb = new StringBuilder();
        sb.append("Sleeping Hours: Slept " + entity.sleep + (entity.sleep > 1 ? " hours." : " hour."));
        sb.append("\n");
        sb.append("Weight: " + entity.weight + " Kg.");
        sb.append("\n");
        int dinnerCt = JTDBConstant.dinnerEnum2DinnerCt(entity.dinner);
        sb.append("Diet: Had " + dinnerCt + (dinnerCt > 1 ? " Meals." : " Meal."));
        sb.append("\n");
        sb.append("Sports: Done " + entity.sports + (entity.sports > 1 ? " hours" : " hour") + " sports.");
        sb.append("\n");

        sb.append("Notes:");
        sb.append("\n");
        sb.append(entity.notes);

        data.setText(sb);
        return v;
    }
}
