package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umbcapp.gaurk.snapeve.Adapters.CreateGroupAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;
import java.util.List;

public class RequestPendingFragment extends Fragment implements Listview_communicator {

    private Parcelable state;

    private ListView add_mem_frag_listview;
    private CreateGroupAdapter createGroupAdapter;

    public RequestPendingFragment() {

    }

    ArrayList<CreateGroupListItem> groupList = new ArrayList<CreateGroupListItem>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.req_pending_fragment, container, false);

        add_mem_frag_listview = (ListView) rootView.findViewById(R.id.req_pending_frag_listview);

        groupList.add(new CreateGroupListItem("Ajinkya Gaurkar", "GU1", 2, "user_1@gmail.com", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
        groupList.add(new CreateGroupListItem("Siddhrth Ptro", "GU1", 2, "user_1@gmail.com", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
        groupList.add(new CreateGroupListItem("Pranav rana", "GU1", 2, "user_1@gmail.com", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
        groupList.add(new CreateGroupListItem("Neha reddy", "GU1", 2, "user_1@gmail.com", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
        groupList.add(new CreateGroupListItem("Rushabh mehta", "GU1", 2, "user_1@gmail.com", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));

        createGroupAdapter = new CreateGroupAdapter(getActivity(), groupList);
        add_mem_frag_listview.setAdapter(createGroupAdapter);

        return rootView;
    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {

// Restore previous state (including selected item index and scroll position)
        state = add_mem_frag_listview.onSaveInstanceState();

        modifyRequestStatus(position);

    }

    private void modifyRequestStatus(int position) {

        CreateGroupListItem selectedCreateGroupListItem = groupList.get(position);

        int tempStatus = selectedCreateGroupListItem.getUserReqStatus();
        Log.d("tempStatus before", tempStatus + "");

        if (tempStatus == 0) {
            selectedCreateGroupListItem.setUserReqStatus(1);
        }
        if (tempStatus == 1) {
            selectedCreateGroupListItem.setUserReqStatus(0);
        }
        groupList.set(position, selectedCreateGroupListItem);

        //Update listview and set selection
        createGroupAdapter.notifyDataSetChanged();
        add_mem_frag_listview.onRestoreInstanceState(state);

    }

}
