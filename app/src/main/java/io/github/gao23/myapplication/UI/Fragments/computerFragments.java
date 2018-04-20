package io.github.gao23.myapplication.UI.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.github.gao23.myapplication.R;

/**
 * Created by xgao on 4/19/18.
 */

 public class computerFragments extends Fragment{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public computerFragments() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static computerFragments newInstance(int sectionNumber) {
        computerFragments fragment = new computerFragments();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.computer_fragment, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("This is computer tip");
        return rootView;
    }
}
