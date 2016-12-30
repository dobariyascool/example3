package com.arraybit.abposa;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.NotificationMaster;
import com.arraybit.modal.OfferMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.NotificationJSONParser;
import com.arraybit.parser.OfferJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationAddFragment extends Fragment implements View.OnClickListener, NotificationJSONParser.NotificationAddListener,
        OfferJSONParser.OfferRequestListener, CategoryJSONParser.CategoryRequestListener, ItemJSONParser.ItemMasterRequestListener {

    final int PIC_CROP = 1;
    EditText etTitle, etMessage;
    TextView txtOfferCategory;
    //            txtImageName,
    ImageView ivImage;
    //            , ivCancleImage;
    Button btnAddNotification;
    LinearLayout llNotificationAdd;
    RelativeLayout rladdImage;
    //            ,rlCancleImage, rlSpinnerType, rlSpinnerCustomer, rlSpinnerOfferCategory, rlSpinnerProduct;
    LinearLayout llSpinnerType, llSpinnerCustomer, llSpinnerOfferCategory, llSpinnerProduct;
    Spinner spinnerType, spinnerCustomer, spinnerOfferCategory, spinnerProduct;
    String imagePhysicalNameBytes, imageName, strImageName;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);
    ProgressDialog progressDialog = new ProgressDialog();
    int notificationMasterId, customerType;
    ArrayAdapter<String> adapterType, adapterCustomer, adapterOfferCategory, adapterProduct;
    ArrayList<OfferMaster> alOfferMaster;
    ArrayList<CategoryMaster> alCategoryMaster;
    ArrayList<ItemMaster> alItemMaster;
    BroadCastListener broadCastListener;
    Context context;

    public NotificationAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification_add, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(getActivity().getResources().getString(R.string.notification_add_title));
        }
        setHasOptionsMenu(true);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etMessage = (EditText) view.findViewById(R.id.etMessage);
//        txtImageName = (TextView) view.findViewById(R.id.txtImageName);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
//        ivCancleImage = (ImageView) view.findViewById(R.id.ivCancleImage);
        llNotificationAdd = (LinearLayout) view.findViewById(R.id.llNotificationAdd);
        rladdImage = (RelativeLayout) view.findViewById(R.id.rladdImage);
//        rlCancleImage = (RelativeLayout) view.findViewById(R.id.rlCancleImage);
        btnAddNotification = (Button) view.findViewById(R.id.btnAddNotification);

        spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
        spinnerCustomer = (Spinner) view.findViewById(R.id.spinnerCustomer);
        spinnerOfferCategory = (Spinner) view.findViewById(R.id.spinnerOfferCategory);
        spinnerProduct = (Spinner) view.findViewById(R.id.spinnerProduct);

        llSpinnerType = (LinearLayout) view.findViewById(R.id.llSpinnerType);
        llSpinnerCustomer = (LinearLayout) view.findViewById(R.id.llSpinnerCustomer);
        llSpinnerOfferCategory = (LinearLayout) view.findViewById(R.id.llSpinnerOfferCategory);
        llSpinnerProduct = (LinearLayout) view.findViewById(R.id.llSpinnerProduct);

        txtOfferCategory = (TextView) view.findViewById(R.id.txtOfferCategory);

        ArrayList<String> Types = new ArrayList<>();
        ArrayList<String> CustomerType = new ArrayList<>();
        Types.add("- SELECT -");
        for (int i = 1; i <= Globals.NotificationType.values().length; i++) {
            Types.add(Globals.NotificationType.getType(i));
        }
        adapterType = new ArrayAdapter<String>(getActivity(), R.layout.row_month_year, Types);
        spinnerType.setAdapter(adapterType);
        CustomerType.add("- SELECT -");
        for (int i = 1; i <= Globals.CustomerType.values().length; i++) {
            CustomerType.add(Globals.CustomerType.getType(i));
        }
        // Set months
        adapterCustomer = new ArrayAdapter<String>(getActivity(), R.layout.row_month_year, CustomerType);
        spinnerCustomer.setAdapter(adapterCustomer);

        btnAddNotification.setOnClickListener(this);
        ivImage.setOnClickListener(this);
//        ivCancleImage.setOnClickListener(this);

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etMessage.getText().toString().length() > 150) {
                    etMessage.setText(etMessage.getText().toString().substring(0, etMessage.getText().toString().length() - 1));
                }
            }
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etTitle.getText().toString().length() > 35) {
                    etTitle.setText(etTitle.getText().toString().substring(0, etTitle.getText().toString().length() - 1));
                }
            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Offer.getValue()) {
                    RequestOfferMaster();
                } else if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Item.getValue() || spinnerType.getSelectedItemPosition() == Globals.NotificationType.Category.getValue()) {
                    RequestCategoryMaster();
                } else{
                    llSpinnerOfferCategory.setVisibility(View.GONE);
                    llSpinnerProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerOfferCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Item.getValue()) {
                    if (spinnerOfferCategory.getSelectedItemPosition() > 0) {
                        RequestItemMaster(alCategoryMaster.get(spinnerOfferCategory.getSelectedItemPosition() - 1).getCategoryMasterId());
                    }
                }else {
//                if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Offer.getValue()
//                        || spinnerType.getSelectedItemPosition() == Globals.NotificationType.Category.getValue()
//                        || spinnerType.getSelectedItemPosition() == Globals.NotificationType.General.getValue()) {
                    llSpinnerProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

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
            getActivity().getSupportFragmentManager().popBackStack(getResources().getString(R.string.notification_add_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                String picturePath = "";
                if (requestCode == 100) {
                    strImageName = "CameraImage_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length()) + ".jpg";
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "CameraImage.jpg");
                    picturePath = f.getAbsolutePath();
                    File f1 = new File(picturePath);
                    Uri contentUri = Uri.fromFile(f1);
                    performCrop(contentUri);
                } else if (requestCode == 101 && data != null && data.getData() != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    File f = new File(picturePath);
                    Uri contentUri = Uri.fromFile(f);
                    performCrop(contentUri);

                } else if (requestCode == PIC_CROP) {
                    if (data != null) {
                        // get the returned data
                        Bundle extras = data.getExtras();
                        // get the cropped bitmap
                        Bitmap selectedBitmap = extras.getParcelable("data");

//                        imgView.setImageBitmap(selectedBitmap);
//                        if (!picturePath.equals("")) {
                        rladdImage.setVisibility(View.GONE);
//                    rlCancleImage.setVisibility(View.VISIBLE);
//                            File imgFile = new File(picturePath);
                        long millis = System.currentTimeMillis();
                        imageName = String.valueOf(millis) + "." + MimeTypeMap.getFileExtensionFromUrl(picturePath);

//                            Bitmap bitmap = null;
//                            if (imgFile.exists()) {
//                                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                                ivImage.setImageBitmap(bitmap);
//                            }
//                            txtImageName.setText(imageName);

                        ivImage.setImageBitmap(selectedBitmap);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] bytedata = bos.toByteArray();
                        imagePhysicalNameBytes = Base64.encodeToString(bytedata, Base64.DEFAULT);
                        return;
//                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddNotification) {
            Globals.HideKeyBoard(getActivity(), v);
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                broadCastListener = (BroadCastListener) getActivity();
                broadCastListener.BroadCastOnclick();
            }
        } else if (v.getId() == R.id.ivImage) {
            Globals.HideKeyBoard(getActivity(), v);
            Globals.SelectImage(getActivity(), this, 100, 101);
//        }else if (v.getId() == R.id.ivCancleImage) {
//            Globals.HideKeyBoard(getActivity(), v);
////            Globals.SelectImage(getActivity(), this, 100, 101);
//            ivImage.setImageBitmap(null);
//            txtImageName.setText("Image Name");
//            rladdImage.setVisibility(View.VISIBLE);
////            rlCancleImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void NotificationAddResponse(String errorCode, NotificationMaster notificationMaster) {
        progressDialog.dismiss();
        if (!errorCode.equals("-1")) {
            Toast.makeText(getActivity(), "Notification sent.", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            if (notificationMaster != null) {
                notificationMasterId = notificationMaster.getNotificationMasterId();
                customerType = spinnerCustomer.getSelectedItemPosition();
                new NotificationSendTask().execute();
            }
        } else {
            Toast.makeText(getActivity(), "Notification not send.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OfferResponse(ArrayList<OfferMaster> alOfferMaster, OfferMaster objOfferMaster) {
        progressDialog.dismiss();
        if (alOfferMaster != null && alOfferMaster.size() > 0) {
            llSpinnerOfferCategory.setVisibility(View.VISIBLE);
            txtOfferCategory.setText("Offer");
            this.alOfferMaster = alOfferMaster;
            ArrayList<String> Offers = new ArrayList<>();
            Offers.add("- SELECT OFFER -");
            for (int i = 0; i < alOfferMaster.size(); i++) {
                Offers.add(alOfferMaster.get(i).getOfferTitle());
                Log.e("offer", " " + alOfferMaster.get(i).getOfferTitle());
            }
            adapterOfferCategory = new ArrayAdapter<String>(getActivity(), R.layout.row_month_year, Offers);
            spinnerOfferCategory.setAdapter(adapterOfferCategory);
        }
    }

    @Override
    public void CategoryResponse(ArrayList<CategoryMaster> alCategoryMaster) {
        progressDialog.dismiss();
        if (alCategoryMaster != null && alCategoryMaster.size() > 0) {
            llSpinnerOfferCategory.setVisibility(View.VISIBLE);
            txtOfferCategory.setText("Category");
            this.alCategoryMaster = alCategoryMaster;
            ArrayList<String> Category = new ArrayList<>();
            Category.add("- SELECT CATEGORY -");
            for (int i = 0; i < alCategoryMaster.size(); i++) {
                Category.add(alCategoryMaster.get(i).getCategoryName());
                Log.e("category", " " + alCategoryMaster.get(i).getCategoryName());
            }
            adapterOfferCategory = new ArrayAdapter<String>(getActivity(), R.layout.row_month_year, Category);
            spinnerOfferCategory.setAdapter(adapterOfferCategory);
        }
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        progressDialog.dismiss();
        if (alItemMaster != null && alItemMaster.size() > 0) {
            llSpinnerProduct.setVisibility(View.VISIBLE);
            this.alItemMaster = alItemMaster;
            ArrayList<String> Items = new ArrayList<>();
            Items.add("- SELECT ITEM -");
            for (int i = 0; i < alItemMaster.size(); i++) {
                Items.add(alItemMaster.get(i).getItemName());
                Log.e("category", " " + alItemMaster.get(i).getItemName());
            }
            adapterProduct = new ArrayAdapter<String>(getActivity(), R.layout.row_month_year, Items);
            spinnerProduct.setAdapter(adapterProduct);
        }

    }

    public void AddNotification() {
        if (Service.CheckNet(getActivity())) {
            AddNotificationRequest();
        } else {
            Globals.ShowSnackBar(llNotificationAdd, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
    }

    //region Private Methods and Interface
    private void AddNotificationRequest() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {
            NotificationJSONParser objNotificationJSONParser = new NotificationJSONParser();
            NotificationMaster objNotificationMaster = new NotificationMaster();
            objNotificationMaster.setNotificationTitle(etTitle.getText().toString());
            objNotificationMaster.setNotificationText(etMessage.getText().toString());
            objNotificationMaster.setIsDeleted(false);
            objNotificationMaster.setLinktoBusinessMasterId(Globals.linktoBusinessMasterId);
            objNotificationMaster.setType((short) spinnerType.getSelectedItemPosition());
            if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Offer.getValue()) {
                objNotificationMaster.setID(alOfferMaster.get(spinnerOfferCategory.getSelectedItemPosition() - 1).getOfferMasterId());
            } else if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Item.getValue()) {
                objNotificationMaster.setID(alItemMaster.get(spinnerOfferCategory.getSelectedItemPosition() - 1).getItemMasterId());
            } else if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.Category.getValue()) {
                objNotificationMaster.setID(alCategoryMaster.get(spinnerOfferCategory.getSelectedItemPosition() - 1).getCategoryMasterId());
            } else if (spinnerType.getSelectedItemPosition() == Globals.NotificationType.General.getValue()) {

            }
            SharePreferenceManage sharePreferenceManage = new SharePreferenceManage();
            if (sharePreferenceManage.GetPreference("LoginPreference", "UserMasterId", getActivity()) != null && !sharePreferenceManage.GetPreference("LoginPreference", "UserMasterId", getActivity()).equals("")) {
                objNotificationMaster.setlinktoUserMasterIdCreatedBy(Short.parseShort(sharePreferenceManage.GetPreference("LoginPreference", "UserMasterId", getActivity())));
            }
            if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                strImageName = imageName;
                objNotificationMaster.setNotificationImageName(strImageName);
                objNotificationMaster.setNotificationImageNameBytes(imagePhysicalNameBytes);
            }
            objNotificationJSONParser.InsertNotificationMaster(objNotificationMaster, getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid;
        boolean IsValidSpinner = false;

        if (etTitle.getText().toString().equals("") && etTitle.getText().toString().equals("")) {
            etTitle.setError("Enter Title");
            etMessage.setError("Enter Message");
            IsValid = false;
        } else if (etMessage.getText().toString().equals("") && !etTitle.getText().toString().equals("")) {
            etMessage.setError("Enter Message");
            etTitle.clearError();
            IsValid = false;
        } else if (etTitle.getText().toString().equals("") && !etMessage.getText().toString().equals("")) {
            etTitle.setError("Enter Title");
            etMessage.clearError();
            IsValid = false;
        } else {
            etMessage.clearError();
            etTitle.clearError();
            IsValid = true;
        }

        if (spinnerCustomer.getSelectedItemPosition() == 0 && spinnerType.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please select Notification type and Customer type", Toast.LENGTH_SHORT).show();
            IsValidSpinner = false;
        } else if (spinnerCustomer.getSelectedItemPosition() == 0 && spinnerType.getSelectedItemPosition() > 0) {
            if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.Offer.getType())) {
                if (spinnerOfferCategory.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Offer and Customer type", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please select Customer type", Toast.LENGTH_SHORT).show();
                }
            } else if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.Item.getType())) {
                if (spinnerOfferCategory.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Item Category and Customer type", Toast.LENGTH_SHORT).show();
                } else {
                    if (spinnerProduct.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "Please select Item and Customer type", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please select Customer type", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.Category.getType())) {
                if (spinnerOfferCategory.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Item Category and Customer type", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please select Customer type", Toast.LENGTH_SHORT).show();
                }
            } else if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.General.getType())) {
                Toast.makeText(getActivity(), "Please select Customer type", Toast.LENGTH_SHORT).show();
            }
            IsValidSpinner = false;
        } else if (spinnerCustomer.getSelectedItemPosition() > 0 && spinnerType.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please select Notification type", Toast.LENGTH_SHORT).show();
            IsValidSpinner = false;
        } else if (spinnerCustomer.getSelectedItemPosition() > 0 && spinnerType.getSelectedItemPosition() > 0) {
            if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.Offer.getType())) {
                if (spinnerOfferCategory.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Offer", Toast.LENGTH_SHORT).show();
                    IsValidSpinner = false;
                } else {
                    IsValidSpinner = true;
                }
            } else if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.Item.getType())) {
                if (spinnerOfferCategory.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Item Category", Toast.LENGTH_SHORT).show();
                    IsValidSpinner = false;
                } else {
                    if (spinnerProduct.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "Please select Item", Toast.LENGTH_SHORT).show();
                        IsValidSpinner = false;
                    } else {
                        IsValidSpinner = true;
                    }
                }
            } else if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.Category.getType())) {
                if (spinnerOfferCategory.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Item Category", Toast.LENGTH_SHORT).show();
                    IsValidSpinner = false;
                } else {
                    IsValidSpinner = true;
                }
//            } else if (spinnerType.getSelectedItem().toString().equals(Globals.NotificationType.General.getType())) {
            } else {
                IsValidSpinner = true;
            }
        } else {
            IsValidSpinner = true;
        }

        Log.e("validate", " " + (IsValid && IsValidSpinner));
        return (IsValid && IsValidSpinner);
    }

    private void RequestOfferMaster() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        OfferJSONParser objOfferJSONParser = new OfferJSONParser();
        objOfferJSONParser.SelectAllOfferMaster(String.valueOf(Globals.linktoBusinessMasterId), getActivity(), NotificationAddFragment.this);
    }

    private void RequestCategoryMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        CategoryJSONParser objCategoryJSONParser = new CategoryJSONParser();

        objCategoryJSONParser.SelectAllCategoryMaster(getActivity(), NotificationAddFragment.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void RequestItemMaster(int categoryMasterId) {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemMaster(NotificationAddFragment.this, getActivity(), String.valueOf(categoryMasterId), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputY", 1080);
            cropIntent.putExtra("outputX", 540);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public interface BroadCastListener {
        void BroadCastOnclick();
    }
    //endregion

    //region LoadingTask
    class NotificationSendTask extends AsyncTask {
        public String SendNotificationsToAll = "SendNotificationsToAll";

        @Override
        protected Object doInBackground(Object[] objects) {
            Service.HttpGetService(Service.Url + this.SendNotificationsToAll + "/" + Globals.linktoBusinessMasterId + "/" + notificationMasterId + "/" + customerType);
            return null;
        }

    }

    //endregion

}
