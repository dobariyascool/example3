package com.arraybit.abposa;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.adapter.NotificationAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.NotificationMaster;
import com.arraybit.parser.NotificationJSONParser;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

//import com.github.clans.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationListFragment extends Fragment implements NotificationJSONParser.NotificationRequestListener, NotificationAdapter.OnClickListener {

    RecyclerView rvNotification;
    LinearLayoutManager linearLayoutManager;
    int CurrentPage = 1;
    ProgressDialog progressDialog = new ProgressDialog();
    LinearLayout errorLayout;
    NotificationAdapter adapter;
    FloatingActionButton fabAdd;

    public NotificationListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        try {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
            if (toolbar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setTitle(getActivity().getResources().getString(R.string.notification_title));
            }
            setHasOptionsMenu(true);

            fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);

            rvNotification = (RecyclerView) view.findViewById(R.id.rvNotification);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

            rvNotification.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (current_page > CurrentPage) {
                        CurrentPage = current_page;
                        if (Service.CheckNet(getActivity())) {
                            RequestOfferMaster();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NotificationActivity) getActivity()).ReplaceAddFragment();
                    CurrentPage = 1;
                }
            });

            if (Service.CheckNet(getActivity())) {
                RequestOfferMaster();
            } else {
                fabAdd.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                Globals.ShowSnackBar(errorLayout, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
            getActivity().overridePendingTransition(0, R.anim.right_exit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void NotificationResponse(ArrayList<NotificationMaster> alNotificationMasters, NotificationMaster objNotificationMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alNotificationMasters);
    }


    @Override
    public void OnRemoveClick(NotificationMaster objNotificationMaster, int position) {
        adapter.NotificationDataRemove(position);
    }

    //region Private Methods
    private void RequestOfferMaster() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        NotificationJSONParser objNotificationJSONParser = new NotificationJSONParser();
        objNotificationJSONParser.SelectAllNotificationMasterPageWise(String.valueOf(CurrentPage), getActivity(), this);
    }

    private void SetRecyclerView(ArrayList<NotificationMaster> lstNotificationMasters) {
        if (lstNotificationMasters == null) {
            if (CurrentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvNotification, 0);
            }
        } else if (lstNotificationMasters.size() == 0) {
            if (CurrentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), getResources().getString(R.string.notification)), rvNotification, 0);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvNotification, 0);
            if (CurrentPage > 1) {
                adapter.NotificationDataChanged(lstNotificationMasters);
                return;
            } else if (lstNotificationMasters.size() < 10) {
                CurrentPage += 1;
            }
            adapter = new NotificationAdapter(getActivity(), lstNotificationMasters, this);
            rvNotification.setAdapter(adapter);
            rvNotification.setLayoutManager(linearLayoutManager);
        }
    }

    //endregion

}
