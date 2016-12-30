package com.arraybit.abposa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.adapter.BookingAdapter;
import com.arraybit.adapter.OrderAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BookingMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.BookingJSONParser;
import com.arraybit.parser.ItemJSONParser;

import java.util.ArrayList;

public class OrderBookingActivity extends AppCompatActivity implements ItemJSONParser.ItemMasterRequestListener, OrderAdapter.OrderOnClickListener, ItemJSONParser.OrderMasterRequestListener, ConfirmDialog.ConfirmationResponseListener, BookingJSONParser.BookingRequestListener, BookingAdapter.BookingOnClickListener {

    RecyclerView rvOrdersBookings;
    LinearLayout errorLayout;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog = new ProgressDialog();
    int currentPage = 1, position;
    ArrayList<ItemMaster> alItemMaster;
    ArrayList<OrderMaster> alOrderMaster;
    ArrayList<BookingMaster> alBookingMaster = new ArrayList<>();
    SharePreferenceManage objSharePreferenceManage;
    OrderMaster objOrder;
    OrderAdapter adapter;
    BookingAdapter bookingAdapter;
    boolean IsCancelled;
    String activityName;
    BookingMaster objBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_booking);
        try {
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(app_bar);
            if (app_bar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
            }

            Intent intent = getIntent();
            IsCancelled = intent.getBooleanExtra("IsCancelled", false);
            activityName = intent.getStringExtra("activityName");
            LinearLayout llEventFragment = (LinearLayout) findViewById(R.id.orderBookingFragment);

            getSupportActionBar().setTitle(activityName);

            rvOrdersBookings = (RecyclerView) findViewById(R.id.rvOrdersBookings);
            errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
            linearLayoutManager = new LinearLayoutManager(this);

            if (Service.CheckNet(this)) {
                if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
                    RequestOrderMasterOrderItem();
                } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
                    RequestBookingMaster();
                }
            } else {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvOrdersBookings, R.drawable.wifi_off);
            }

            rvOrdersBookings.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
                        if (!adapter.isItemAnimate) {
                            adapter.isItemAnimate = true;
                        }
                    } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
                        if (!bookingAdapter.isItemAnimate) {
                            bookingAdapter.isItemAnimate = true;
                        }
                    }

                }
            });

            rvOrdersBookings.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
                        if (!adapter.isItemAnimate) {
                            adapter.isItemAnimate = true;
                        }
                    } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
                        if (!bookingAdapter.isItemAnimate) {
                            bookingAdapter.isItemAnimate = true;
                        }
                    }
                    if (current_page > currentPage) {
                        currentPage = current_page;
                        if (Service.CheckNet(OrderBookingActivity.this)) {
                            if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
                                RequestOrderMasterOrderItem();
                            } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
                                RequestBookingMaster();
                            }
                        } else {
                            Globals.ShowSnackBar(rvOrdersBookings, getResources().getString(R.string.MsgCheckConnection), OrderBookingActivity.this, 1000);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, R.anim.right_exit);
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        progressDialog.dismiss();
        this.alItemMaster = alItemMaster;
        SetRecyclerView();
    }

    @Override
    public void CancelOnClick(OrderMaster objOrderMaster, int position) {
        this.position = position;
        this.objOrder = objOrderMaster;
        ConfirmDialog confirmDialog = new ConfirmDialog(true, String.format(getResources().getString(R.string.cdfCancelMsg), "order no (" + objOrder.getOrderNumber() + ")"));
        confirmDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void CancelClickListener(BookingMaster objBookingMaster, int position) {
        this.position = position;
        this.objBooking = objBookingMaster;
        ConfirmDialog confirmDialog = new ConfirmDialog(true, String.format(getResources().getString(R.string.cdfCancelMsg), "booking of " + objBookingMaster.getBookingPersonName() + " on " + objBookingMaster.getToDate()));
        confirmDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void ConfirmResponse() {
        progressDialog.show(getSupportFragmentManager(), "");
        if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
            ItemJSONParser itemJSONParser = new ItemJSONParser();
            itemJSONParser.UpdateOrderMasterStatus(String.valueOf(objOrder.getOrderMasterId()), OrderBookingActivity.this);
        } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
            BookingJSONParser objBookingJSONParser = new BookingJSONParser();
            objBookingJSONParser.UpdateBookingMaster(objBooking, this);
        }
    }

    @Override
    public void OrderMasterResponse(String errorCode, OrderMaster objOrderMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void BookingResponse(String errorCode, ArrayList<BookingMaster> alBookingMaster) {
        progressDialog.dismiss();
        if (errorCode != null) {
            SetError(errorCode);
        } else if (alBookingMaster != null) {
            this.alBookingMaster = alBookingMaster;
            SetRecyclerView();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvOrdersBookings, 0);
        }
    }

    private void RequestOrderMasterOrderItem() {
        progressDialog.show(getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllOrderMasterOrderItem(this, String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId), IsCancelled);
    }

    private void RequestBookingMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectAllBookingMaster(this, String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId), IsCancelled);
    }

    private void SetRecyclerView() {
        try {
            if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
                SetOrderItemList();
                if (alOrderMaster == null) {
                    if (currentPage == 1) {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvOrdersBookings, 0);
                    }
                } else if (alOrderMaster.size() == 0) {
                    if (currentPage == 1) {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvOrdersBookings, 0);
                    }
                } else {
                    Globals.SetErrorLayout(errorLayout, false, null, rvOrdersBookings, 0);
                    if (currentPage > 1) {
                        adapter.OrderDataChanged(alOrderMaster);
                        return;
                    } else if (alOrderMaster.size() < 10) {
                        currentPage += 1;
                    }
                    adapter = new OrderAdapter(OrderBookingActivity.this, alOrderMaster, this);
                    rvOrdersBookings.setAdapter(adapter);
                    rvOrdersBookings.setLayoutManager(linearLayoutManager);
                }
            } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
                if (alBookingMaster == null) {
                    if (currentPage == 1) {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvOrdersBookings, 0);
                    }
                } else if (alBookingMaster.size() == 0) {
                    if (currentPage == 1) {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvOrdersBookings, 0);
                    }
                } else {
                    Globals.SetErrorLayout(errorLayout, false, null, rvOrdersBookings, 0);
                    if (currentPage > 1) {
                        bookingAdapter.BookingDataChanged(alBookingMaster);
                        return;
                    } else if (alBookingMaster.size() < 10) {
                        currentPage += 1;
                    }
                    bookingAdapter = new BookingAdapter(this, alBookingMaster);
                    rvOrdersBookings.setAdapter(bookingAdapter);
                    rvOrdersBookings.setLayoutManager(linearLayoutManager);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetOrderItemList() {
        try {
            alOrderMaster = new ArrayList<>();
            ArrayList<ItemMaster> alOrderItem = new ArrayList<>();
            OrderMaster objOrderMaster = new OrderMaster();
            int orderId = -1;
            int cnt = 0;
            for (ItemMaster objItemMaster : alItemMaster) {
                if (orderId == -1) {
                    orderId = objItemMaster.getLinktoOrderMasterId();
                    objOrderMaster.setOrderMasterId(objItemMaster.getLinktoOrderMasterId());
                    objOrderMaster.setOrderNumber(objItemMaster.getOrderNumber());
                    objOrderMaster.setTotalAmount(objItemMaster.getTotalAmount());
                    objOrderMaster.setTotalTax(objItemMaster.getTotalTax());
                    objOrderMaster.setOrderDateTime(objItemMaster.getCreateDateTime());
                    objOrderMaster.setIsPreOrder(objItemMaster.getPaymentStatus());
                    objOrderMaster.setlinktoOrderStatusMasterId(objItemMaster.getLinktoOrderStatusMasterId());
                    alOrderItem.add(objItemMaster);
                    if (cnt == alItemMaster.size() - 1) {
                        objOrderMaster.setAlOrderItemTran(alOrderItem);
                        alOrderMaster.add(objOrderMaster);
                    }
                } else {
                    if (orderId == objItemMaster.getLinktoOrderMasterId()) {
                        orderId = objItemMaster.getLinktoOrderMasterId();
                        alOrderItem.add(objItemMaster);
                        if (cnt == alItemMaster.size() - 1) {
                            objOrderMaster.setAlOrderItemTran(alOrderItem);
                            alOrderMaster.add(objOrderMaster);
                        }
                    } else {
                        objOrderMaster.setAlOrderItemTran(alOrderItem);
                        alOrderMaster.add(objOrderMaster);
                        orderId = objItemMaster.getLinktoOrderMasterId();
                        alOrderItem = new ArrayList<>();
                        objOrderMaster = new OrderMaster();
                        objOrderMaster.setOrderMasterId(objItemMaster.getLinktoOrderMasterId());
                        objOrderMaster.setOrderNumber(objItemMaster.getOrderNumber());
                        objOrderMaster.setTotalAmount(objItemMaster.getTotalAmount());
                        objOrderMaster.setTotalTax(objItemMaster.getTotalTax());
                        objOrderMaster.setOrderDateTime(objItemMaster.getCreateDateTime());
                        objOrderMaster.setIsPreOrder(objItemMaster.getPaymentStatus());
                        objOrderMaster.setlinktoOrderStatusMasterId(objItemMaster.getLinktoOrderStatusMasterId());
                        alOrderItem.add(objItemMaster);
                        if (cnt == alItemMaster.size() - 1) {
                            objOrderMaster.setAlOrderItemTran(alOrderItem);
                            alOrderMaster.add(objOrderMaster);
                        }
                    }
                }
                cnt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetError(String errorCode) {
        if (activityName.equals(getResources().getString(R.string.golfNewOrders)) || activityName.equals(getResources().getString(R.string.golfCancelledOrders))) {
            switch (errorCode) {
                case "1":
                    Globals.ShowSnackBar(rvOrdersBookings, getResources().getString(R.string.MsgCancelOrder), OrderBookingActivity.this, 1000);
                    break;
                case "-1":
                    Globals.ShowSnackBar(rvOrdersBookings, getResources().getString(R.string.MsgServerNotResponding), OrderBookingActivity.this, 1000);
                    break;
                default:
                    adapter.RemoveOrderData(position);
                    if (adapter.alOrderMaster.size() == 0) {
                        alOrderMaster = adapter.alOrderMaster;
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvOrdersBookings, 0);
//                        SetRecyclerView();
                    }
                    break;
            }
        } else if (activityName.equals(getResources().getString(R.string.golfNewBookings)) || activityName.equals(getResources().getString(R.string.golfCancelledBookings))) {
            switch (errorCode) {
                case "1":
                    Globals.ShowSnackBar(rvOrdersBookings, getResources().getString(R.string.MsgCancelBooking), OrderBookingActivity.this, 1000);
                    break;
                case "-1":
                    Globals.ShowSnackBar(rvOrdersBookings, getResources().getString(R.string.MsgServerNotResponding), OrderBookingActivity.this, 1000);
                    break;
                default:
                    //Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.ybAddBookingSuccessMsg), getActivity(), 1000);
                    bookingAdapter.RemoveBookingItem(position);
                    if (bookingAdapter.alBookingMaster.size() == 0) {
                        alBookingMaster = bookingAdapter.alBookingMaster;
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvOrdersBookings, 0);
//                        SetRecyclerView();
                    }
                    //(getActivity()).setResult(Activity.RESULT_OK);
                    //(getActivity()).finish();
                    break;
            }
        }
    }
}
