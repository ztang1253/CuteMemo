package edu.tang.jane.cutememo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



public class AboutFragment extends Fragment {
    private MainActivity mParent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_about_tab, container, false);

        mParent = (MainActivity) getActivity();
        Button btn = v.findViewById(R.id.about_phone);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:226-989-9559"));
                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(intent);
                                   }
                               }
        );

        btn = v.findViewById(R.id.about_email);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent data = new Intent(Intent.ACTION_SENDTO);
                                       data.setData(Uri.parse("mailto:ztang1253@conestogac.on.ca"));
                                       startActivity(data);
                                   }
                               }
        );

        btn = v.findViewById(R.id.about_website);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Uri uri = Uri.parse("http://www.conestogac.on.ca");
                                       Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                       startActivity(it);
                                   }
                               }
        );

        btn = v.findViewById(R.id.about_reset);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       createAlertDialog();
                                   }
                               }
        );
        return v;
    }

    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Really want to reset? This will clear all memos and locations. All data will be lost!");
        builder.setTitle("Confirm");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mParent.clearData();
                    }
                }).start();

                Toast.makeText(getContext(), "Reset Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }
}
