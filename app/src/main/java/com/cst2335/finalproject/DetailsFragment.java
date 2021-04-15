package com.cst2335.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private Bundle dataFromActivity;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private AppCompatActivity parentActivity;
    RadioGroup radioGroup1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2,String param3, String param4,String param5, String param6) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false);


        dataFromActivity = getArguments();
        question = dataFromActivity.getString(Quiz_frame.Question);
        optionA = dataFromActivity.getString(Quiz_frame.OptionA);
        optionB = dataFromActivity.getString(Quiz_frame.OptionB);
        optionC = dataFromActivity.getString(Quiz_frame.OptionC);
        optionD = dataFromActivity.getString(Quiz_frame.OptionD);

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.activity_details_fragment, container, false);

        //show the message

        //show the id:
        TextView QstView = (TextView) result.findViewById(R.id.txt2);
        QstView.setText(question);
        TextView optViewA = (TextView) result.findViewById(R.id.optBtn1);
        optViewA.setText(optionA);
        TextView optViewBCD = (TextView) result.findViewById(R.id.optBtn2);
        optViewBCD.setText(optionB);
        TextView optViewC = (TextView) result.findViewById(R.id.optBtn3);
        optViewC.setText(optionC);
        TextView optViewD = (TextView) result.findViewById(R.id.optBtn4);
        optViewD.setText(optionD);
        return result;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
    }
}