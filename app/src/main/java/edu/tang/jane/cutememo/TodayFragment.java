package edu.tang.jane.cutememo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.tang.jane.cutememo.DB.JTDBConstant;
import edu.tang.jane.cutememo.DB.JTMemoEntity;



public class TodayFragment extends Fragment {
    private View mPrivateView;
    private MainActivity mParent;
    private JTMemoEntity mEntity;

    private DatePickerDialog mDatePicker;
    private EditText mDateText;

    private EditText mSelectedET;
    private int mSelectedListIndex;
    private PopupWindow mPopupList;
    private ListView mList;
    private SimpleAdapter mAdapter;
    private ArrayList<String> mListData;
    private boolean mEditMode;

    private EditText mSleepText;
    private EditText mWeightText;
    private EditText mDinnerText;
    private EditText mSportsText;
    private EditText mPeriodText;
    private EditText mGradeText;
    private EditText mPeeText;
    private EditText mNotesText;

    private AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mPopupList != null) {
                mPopupList.dismiss();
                mPopupList = null;

                updateDateEntity(i);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_today_tab, container, false);

        mParent = (MainActivity) getActivity();
        Button back = view.findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParent.go2FragmentByTag(MainActivity.TAG_DETAILS);
            }
        });

        mEntity = mParent.getCurrentData();
        if (mEntity == null) {
            mEntity = new JTMemoEntity();
            mEntity.reset();
            back.setVisibility(View.GONE);
            mEditMode = false;
        } else {
            back.setVisibility(View.VISIBLE);
            mEditMode = true;
        }

        mPrivateView = view.findViewById(R.id.private_area);
        CheckBox cb = view.findViewById(R.id.tab_today_cb);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                              mPrivateView.setVisibility(b ? View.VISIBLE : View.GONE);
                                          }
                                      }
        );

        mDateText = view.findViewById(R.id.date_picker);
        mDateText.setText((mEntity.month + 1) + "/" + mEntity.day + "/" + mEntity.year);
        mDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDatePicker == null) {
                    mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mEntity.year = year;
                            mEntity.month = monthOfYear;
                            mEntity.day = dayOfMonth;

                            mEntity.updateDate();
                            mDateText.setText((mEntity.month + 1) + "/" + mEntity.day + "/" + mEntity.year);
                        }
                    }, mEntity.year, mEntity.month, mEntity.day);
                }

                if (mDatePicker != null) {
                    mDatePicker.show();
                }
            }
        });

        mSleepText = view.findViewById(R.id.sleep_picker);
        mSleepText.setText(String.valueOf(mEntity.sleep));
        mSleepText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.sleep = calm(mEntity.sleep, JTDBConstant.MIN_SLEEP, JTDBConstant.MAX_SLEEP - 1);
                mSelectedListIndex = mEntity.sleep -  JTDBConstant.MIN_SLEEP;
                for (int i = JTDBConstant.MIN_SLEEP; i < JTDBConstant.MAX_SLEEP; ++i) {
                    mListData.add(String.valueOf(i));
                }

                mSelectedET = mSleepText;
                showListView(view);
            }
        });

        mWeightText = view.findViewById(R.id.weight_picker);
        mWeightText.setText(String.valueOf(mEntity.weight));
        mWeightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.weight = calm(mEntity.weight, JTDBConstant.MIN_WEIGHT, JTDBConstant.MAX_WEIGHT - 1);
                mSelectedListIndex = mEntity.weight -  JTDBConstant.MIN_WEIGHT;
                for (int i = JTDBConstant.MIN_WEIGHT; i < JTDBConstant.MAX_WEIGHT; ++i) {
                    mListData.add(String.valueOf(i));
                }

                mSelectedET = mWeightText;
                showListView(view);
            }
        });

        mDinnerText = view.findViewById(R.id.dinner_picker);
        mDinnerText.setText(JTDBConstant.DINNERS[mEntity.dinner]);
        mDinnerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.dinner = calm(mEntity.dinner, 0, JTDBConstant.DINNERS.length - 1);
                mSelectedListIndex = mEntity.dinner;
                for (int i = 0; i < JTDBConstant.DINNERS.length; ++i) {
                    mListData.add(JTDBConstant.DINNERS[i]);
                }

                mSelectedET = mDinnerText;
                showListView(view);
            }
        });

        mSportsText = view.findViewById(R.id.sport_picker);
        mSportsText.setText(String.valueOf(mEntity.sports));
        mSportsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.sports = calm(mEntity.sports, JTDBConstant.MIN_SPORTS, JTDBConstant.MAX_SPORTS - 1);
                mSelectedListIndex = mEntity.sports - JTDBConstant.MIN_SPORTS;
                for (int i = JTDBConstant.MIN_SPORTS; i < JTDBConstant.MAX_SPORTS; ++i) {
                    mListData.add(String.valueOf(i));
                }

                mSelectedET = mSportsText;
                showListView(view);
            }
        });

        mPeriodText = view.findViewById(R.id.period_picker);
        mPeriodText.setText( JTDBConstant.PERIOD_COLORS[mEntity.period]);
        mPeriodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.period = calm(mEntity.period, 0, JTDBConstant.PERIOD_COLORS.length - 1);
                mSelectedListIndex = mEntity.period;
                for (int i = 0; i < JTDBConstant.PERIOD_COLORS.length; ++i) {
                    mListData.add(JTDBConstant.PERIOD_COLORS[i]);
                }

                mSelectedET = mPeriodText;
                showListView(view);
            }
        });

        mGradeText = view.findViewById(R.id.grade_picker);
        mGradeText.setText(JTDBConstant.PERIOD_GRADE[mEntity.grade]);
        mGradeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.grade = calm(mEntity.grade, 0, JTDBConstant.PERIOD_GRADE.length - 1);
                mSelectedListIndex = mEntity.grade;
                for (int i = 0; i < JTDBConstant.PERIOD_GRADE.length; ++i) {
                    mListData.add(JTDBConstant.PERIOD_GRADE[i]);
                }

                mSelectedET = mGradeText;
                showListView(view);
            }
        });

        mPeeText = view.findViewById(R.id.pee_picker);
        mPeeText.setText(JTDBConstant.PEE_COLOR[mEntity.peeColor]);
        mPeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListData == null) {
                    mListData = new ArrayList<String>();
                }

                mListData.clear();
                mEntity.peeColor = calm(mEntity.peeColor, 0, JTDBConstant.PEE_COLOR.length - 1);
                mSelectedListIndex = mEntity.peeColor;
                for (int i = 0; i < JTDBConstant.PEE_COLOR.length; ++i) {
                    mListData.add(JTDBConstant.PEE_COLOR[i]);
                }

                mSelectedET = mPeeText;
                showListView(view);
            }
        });

        mNotesText = view.findViewById(R.id.notes);
        if (mEntity.notes != null && mEntity.notes.length() > 0) mNotesText.setText(mEntity.notes);
        Button save = view.findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEntity.notes = mNotesText.getText().toString();
                if (mEditMode) {
                    mParent.updateEntity(mEntity);
                } else {
                    mParent.addEntity(mEntity);
                }

                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                mParent.go2FragmentByTag(MainActivity.TAG_OVERVIEW);
            }
        });
        return view;
    }

    public void setEntity(JTMemoEntity entity) {
        mEntity = entity;
        if (mDatePicker != null) {
            mDatePicker.updateDate(mEntity.year, mEntity.month, mEntity.day);
        }
    }

    private void showListView(View parent) {
        if (mPopupList == null) {
            LayoutInflater lay = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = lay.inflate(R.layout.popup_list, null);

            mList =(ListView) v.findViewById(R.id.popup_lv);
            List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
            for (String str: mListData) {
                HashMap<String, String> obj = new HashMap<String, String>();
                obj.put("value", str);
                data.add(obj);
            }

            mAdapter = new SimpleAdapter(getContext(), data, R.layout.popup_list_item, new String[]{ "value" }, new int[]{ R.id.item_value });
            mList.setAdapter(mAdapter);
            mList.setItemsCanFocus(false);
            mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mList.setOnItemClickListener(mListener);

            mPopupList = new PopupWindow(v, mParent.mScreenWidth, mParent.mScreenHeight);
        }

        mPopupList.setFocusable(true);
        mPopupList.update();
        mPopupList.showAtLocation(parent, Gravity.CENTER, 0, 0);

        mList.post(new Runnable() {
            @Override
            public void run() {
                mList.requestFocusFromTouch();
                mList.setItemChecked(mSelectedListIndex, true);
                mList.setSelection(mSelectedListIndex);
            }
        });
    }

    private void updateDateEntity(int index) {
        if (mSelectedET == mSleepText) {
            mEntity.sleep = index + JTDBConstant.MIN_SLEEP;
            mSelectedET.setText(String.valueOf(mEntity.sleep));
        } else if (mSelectedET == mWeightText) {
            mEntity.weight = index + JTDBConstant.MIN_WEIGHT;
            mSelectedET.setText(String.valueOf(mEntity.weight));
        } else if (mSelectedET == mDinnerText) {
            mEntity.dinner = index;
            mSelectedET.setText(JTDBConstant.DINNERS[mEntity.dinner]);
        } else if (mSelectedET == mSportsText) {
            mEntity.sports = index + JTDBConstant.MIN_SPORTS;
            mSelectedET.setText(String.valueOf(mEntity.sports));
        } else if (mSelectedET == mPeriodText) {
            mEntity.period = index;
            mSelectedET.setText(JTDBConstant.PERIOD_COLORS[mEntity.period]);
        } else if (mSelectedET == mGradeText) {
            mEntity.grade = index;
            mSelectedET.setText(JTDBConstant.PERIOD_GRADE[mEntity.grade]);
        } else if (mSelectedET == mPeeText) {
            mEntity.peeColor = index;
            mSelectedET.setText(JTDBConstant.PEE_COLOR[mEntity.peeColor]);
        }
    }

    private int calm(int value, int min, int max) {
        if (value < min)
            return min;
        else if (value > max)
            return max;
        return value;
    }
}
