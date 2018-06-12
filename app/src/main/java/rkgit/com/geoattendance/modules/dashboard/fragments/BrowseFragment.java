package rkgit.com.geoattendance.modules.dashboard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rkgit.com.geoattendance.MarkAttendanceActivity;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.modules.MonthlyAttendanceActivity;
import rkgit.com.geoattendance.modules.dashboard.ChatActivity;

/**
 * Created by Ayush Kulshrestha
 * on 06-03-2018.
 */

public class BrowseFragment extends Fragment {

    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.mark_attendance)
    void onMarkAttendance() {
        Intent intent = new Intent(getActivity(), MarkAttendanceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.employee_chat)
    void onEmployeeChat() {
         /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert").setMessage("Under Development").setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

            }
        });
        builder.create().show();
*/
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.view_attendance)
    void onViewAttendance() {
        Intent intent = new Intent(getActivity(), ViewAttendance.class);
        startActivity(intent);
    }

    @OnClick(R.id.monthly_analysis)
    void onMonthlyAttendance() {
        Intent intent = new Intent(getActivity(), MonthlyAttendanceActivity.class);
        startActivity(intent);
    }
}
