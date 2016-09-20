package com.arraybit.abposa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.adapter.EventAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.Events;
import com.arraybit.parser.EventJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements View.OnClickListener, EventJSONParser.EventsListener {

    RecyclerView rvTodaysEvent, rvUpcomingEvent;
    ImageView ivTodaysEvent, ivUpcomingEvent;
    boolean isTEventExpand = true, isUEventExpand = true;
    int currentPageToday = 1, currentPageUpcoming = 1;
    EventAdapter adapterTodaysEvents, adapterUpcomingEvents;
    TextView txtUpcomingNoEvent, txtTodayNoEvent;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        try {

            rvTodaysEvent = (RecyclerView) rootView.findViewById(R.id.rvTodaysEvent);
            rvUpcomingEvent = (RecyclerView) rootView.findViewById(R.id.rvUpcomingEvent);

            ivTodaysEvent = (ImageView) rootView.findViewById(R.id.ivTodaysEvent);
            ivUpcomingEvent = (ImageView) rootView.findViewById(R.id.ivUpcomingEvent);

            txtTodayNoEvent = (TextView) rootView.findViewById(R.id.txtTodayNoEvent);
            txtUpcomingNoEvent = (TextView) rootView.findViewById(R.id.txtUpcomingNoEvent);

//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            ivTodaysEvent.setOnClickListener(this);
            ivUpcomingEvent.setOnClickListener(this);

            RequestEvents(getActivity().getResources().getString(R.string.todays_event));
            ivTodaysEvent.setImageResource(R.drawable.menu_up);

            rvTodaysEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    adapterTodaysEvents.isItemAnimate = true;
                }
            });

            rvTodaysEvent.addOnScrollListener(new EndlessRecyclerOnScrollListener(new LinearLayoutManager(getActivity())) {
                @Override
                public void onLoadMore(int current_page) {
                    adapterTodaysEvents.isItemAnimate = true;
                    if (current_page > currentPageToday) {
                        currentPageToday = current_page;
                        if (Service.CheckNet(getActivity())) {
                            RequestEvents(getActivity().getString(R.string.todays_event));
                        } else {
                            Globals.ShowSnackBar(rootView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                        }
                    }
                }
            });

            rvUpcomingEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    adapterUpcomingEvents.isItemAnimate = true;
                }
            });

            rvUpcomingEvent.addOnScrollListener(new EndlessRecyclerOnScrollListener(new LinearLayoutManager(getActivity())) {
                @Override
                public void onLoadMore(int current_page) {
                    adapterUpcomingEvents.isItemAnimate = true;
                    if (current_page > currentPageUpcoming) {
                        currentPageUpcoming = current_page;
                        if (Service.CheckNet(getActivity())) {
                            RequestEvents(getActivity().getString(R.string.upcoming_event));
                        } else {
                            Globals.ShowSnackBar(rootView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivTodaysEvent) {
            if (!isTEventExpand) {
                isTEventExpand = true;
                ivTodaysEvent.setImageResource(R.drawable.menu_up);
                currentPageToday = 1;
                RequestEvents(getActivity().getString(R.string.todays_event));
            } else {
                rvTodaysEvent.setVisibility(View.GONE);
                txtTodayNoEvent.setVisibility(View.GONE);
                isTEventExpand = false;
                ivTodaysEvent.setImageResource(R.drawable.menu_down);
            }
        } else if (v.getId() == R.id.ivUpcomingEvent) {
            if (!isUEventExpand) {
                isUEventExpand = true;
                ivUpcomingEvent.setImageResource(R.drawable.menu_up);
                currentPageUpcoming = 1;
                RequestEvents(getActivity().getString(R.string.upcoming_event));
            } else {
                rvUpcomingEvent.setVisibility(View.GONE);
                txtUpcomingNoEvent.setVisibility(View.GONE);
                isUEventExpand = false;
                ivUpcomingEvent.setImageResource(R.drawable.menu_down);
            }
        }
    }

    @Override
    public void SetEvents(ArrayList<Events> lstEvents, String eventType) {
        SetEventRecyclerView(lstEvents, eventType);
    }

    private void RequestEvents(String eventType) {
//        progressDialog = new ProgressDialog();
//        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        EventJSONParser objEventJSONParser = new EventJSONParser();
        if (eventType.equals(getActivity().getString(R.string.todays_event))) {
            objEventJSONParser.SelectEventsPageWise(getActivity(), this, String.valueOf(currentPageToday), String.valueOf(Globals.linktoBusinessMasterId), eventType);
        } else if (eventType.equals(getActivity().getString(R.string.upcoming_event))) {
            objEventJSONParser.SelectEventsPageWise(getActivity(), this, String.valueOf(currentPageUpcoming), String.valueOf(Globals.linktoBusinessMasterId), eventType);
        }
    }

    private void SetEventRecyclerView(ArrayList<Events> lstEvents, String eventType) {
        try {
            if (eventType.equals(getActivity().getString(R.string.todays_event))) {

                if (lstEvents == null) {
                    if (currentPageToday == 1) {
//                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvGallery, 0);
                        rvTodaysEvent.setVisibility(View.GONE);
                        txtTodayNoEvent.setVisibility(View.VISIBLE);

                    }
                } else if (lstEvents.size() == 0) {
                    if (currentPageToday == 1) {
//                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgGallery), rvGallery, 0);
                        if (eventType.equals(getActivity().getString(R.string.todays_event))) {
                            rvTodaysEvent.setVisibility(View.GONE);
                            txtTodayNoEvent.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
//            Globals.SetErrorLayout(errorLayout, false, null, rvGallery, 0);

                    if (currentPageToday > 1) {
                        adapterTodaysEvents.EventsDataChanged(lstEvents);
                        return;
                    } else if (lstEvents.size() < 10) {
                        currentPageToday += 1;
                    }
                    adapterTodaysEvents = new EventAdapter(getActivity(), lstEvents, eventType);
                    rvTodaysEvent.setVisibility(View.VISIBLE);
                    rvTodaysEvent.setAdapter(adapterTodaysEvents);
                    rvTodaysEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            } else if (eventType.equals(getActivity().getString(R.string.upcoming_event))) {
                if (lstEvents == null) {
                    if (currentPageUpcoming == 1) {
                        rvUpcomingEvent.setVisibility(View.GONE);
                        txtUpcomingNoEvent.setVisibility(View.VISIBLE);
                    }
                } else if (lstEvents.size() == 0) {
                    if (currentPageUpcoming == 1) {
                        txtUpcomingNoEvent.setVisibility(View.VISIBLE);
                        rvUpcomingEvent.setVisibility(View.GONE);
                    }
                } else {
                    if (currentPageUpcoming > 1) {
                        adapterUpcomingEvents.EventsDataChanged(lstEvents);
                        return;
                    } else if (lstEvents.size() < 10) {
                        currentPageUpcoming += 1;
                    }
                    adapterUpcomingEvents = new EventAdapter(getActivity(), lstEvents, eventType);
                    rvUpcomingEvent.setVisibility(View.VISIBLE);
                    rvUpcomingEvent.setAdapter(adapterUpcomingEvents);
                    rvUpcomingEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
