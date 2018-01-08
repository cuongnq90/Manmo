package vn.manmo.search;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import vn.manmo.search.utils.DialogUtils;
import vn.manmo.search.utils.getApi.BookRoomRequest;
import vn.manmo.search.utils.getApi.GetFurnitureInfo;


public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Toolbar mToolbar;
    private TextView mTvHotelName;
    private EditText mEdtName;
    private EditText mEdtPhone;
    private EditText mEdtEmail;
    private EditText mEdtStartDate;
    private EditText mEdtEndDate;
    private EditText mEdtNumPeople;
    private EditText mEdtNumRooms;
    private TextView mBtnSendBook;
    private DatePickerDialog mEndDatePickerDialog, mStartDatePickerDialog;
    SimpleDateFormat mDateFormatter;

    private String mHotelId;
    private String mHotelName;
    private String mHotelEmail;
    private String IdHotelManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mContext = this;
        mToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đặt phòng");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupView();
    }

    private void setupView() {
        mHotelId = getIntent().getStringExtra("hotel_id");
        mHotelName = getIntent().getStringExtra("hotel_name");
        mHotelEmail = (getIntent().getStringExtra("hotel_email")!= null) ? getIntent().getStringExtra("hotel_email"): "";
        IdHotelManager = (getIntent().getStringExtra("id_manager") != null) ? getIntent().getStringExtra("id_manager") : "";
        mTvHotelName = findViewById(R.id.tv_hotel_name);
        mEdtName = findViewById(R.id.edt_name);
        mEdtPhone = findViewById(R.id.edt_phone);
        mEdtEmail = findViewById(R.id.edt_email);
        mEdtStartDate = findViewById(R.id.edt_start_date);
        mEdtEndDate = findViewById(R.id.edt_end_date);
        mEdtNumPeople = findViewById(R.id.edt_num_people);
        mEdtNumRooms = findViewById(R.id.edt_num_rooms);
        mBtnSendBook = findViewById(R.id.btn_send_booking);
        mTvHotelName.setText(mHotelName);
        mEdtStartDate.setInputType(InputType.TYPE_NULL);
        mEdtEndDate.setInputType(InputType.TYPE_NULL);
        mEdtStartDate.setOnClickListener(this);
        mEdtEndDate.setOnClickListener(this);
        mBtnSendBook.setOnClickListener(this);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        mStartDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEdtStartDate.setText(mDateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mEndDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEdtEndDate.setText(mDateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_start_date:
                hideSoftKeyboard();
                mStartDatePickerDialog.show();
                break;
            case R.id.edt_end_date:
                hideSoftKeyboard();
                mEndDatePickerDialog.show();
                break;
            case R.id.btn_send_booking:
                hideSoftKeyboard();
                bookRoom();
                break;
        }
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void bookRoom() {
        String name = mEdtName.getText().toString();
        String email = mEdtEmail.getText().toString();
        String phone = mEdtPhone.getText().toString();
        String num_room = mEdtNumRooms.getText().toString();
        String num_per = mEdtNumPeople.getText().toString();
        String start_date = mEdtStartDate.getText().toString();
        String end_date = mEdtEndDate.getText().toString();
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final ProgressDialog pr = DialogUtils.loadingDialog(mContext, "Xin vui lòng chờ giây lát !");
        pr.show();
        String link = getResources().getString(R.string.orderHotel);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    if (code.equals("0")) {
                        pr.dismiss();
                        Toast.makeText(mContext, "Bạn đã đặt phòng thành công !", Toast.LENGTH_LONG).show();
                        finish();
                    } else if (code.equals("2")) {
                        pr.dismiss();
                        Toast.makeText(getApplicationContext(), "Bạn nhập sai thời gian !", Toast.LENGTH_SHORT).show();
                    } else if (code.equals("-2")) {
                        pr.dismiss();
                        Toast.makeText(getApplicationContext(), "Đặt phòng không thành công!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), "Bạn nhập sai thời gian !", Toast.LENGTH_SHORT).show();
                    } else {
                        pr.dismiss();
                        Toast.makeText(getApplicationContext(), "Bạn vui lòng nhập đủ dữ liệu !", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };
//        BookRoomRequest gcr = new BookRoomRequest(link, response);
        BookRoomRequest gcr = new BookRoomRequest(start_date,
                end_date, "2", name,
                phone, email, num_room,
                num_per, mHotelId, mHotelName, mHotelEmail, IdHotelManager, link, response);
        RequestQueue qe = Volley.newRequestQueue(this);
        qe.add(gcr);

//        if (name.matches("")) {
//            invalid = true;
//            pr.dismiss();
//            namebook.setError("Bạn chưa nhập tên");
//        } else if (fone.equals("")) {
//            invalid = true;
//            pr.dismiss();
//            fondbook.setError("Bạn chưa nhập số điện thoại");
//        } else if (fone.length() < 9 || fone.length() > 11) {
//            invalid = true;
//            pr.dismiss();
//            fondbook.setError("Bạn nhập thiếu/thừa số điện thoại");
//
//        } else if (!email.matches(emailPattern)) {
//            invalid = true;
//            pr.dismiss();
//            emailbook.setError("Nhập email sai định dạng");
//
//        } else if (date_from == null) {
//            invalid = true;
//            pr.dismiss();
//            fromday.setError("Bạn chưa nhập ngày đến");
//        } else if (date_to == null) {
//            invalid = true;
//            pr.dismiss();
//            today.setError("Bạn chưa nhập ngày đi");
//        } else if (!num_room.equals("") && Integer.parseInt(num_room) <= 0) {
//            invalid = true;
//            pr.dismiss();
//            numberroom.setError("Bạn nhập sai kiểu số phòng");
//        } else if (!num_per.equals("") && (Integer.parseInt(num_per) <= 0)) {
//            invalid = true;
//            pr.dismiss();
//            numberper.setError("Bạn nhập sai kiểu số người");
//        } else {
//            invalid = false;
//            String link = getResources().getString(R.string.orderHotel);
//            Response.Listener<String> response = new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jo = new JSONObject(response);
//                        String code = jo.getString("code");
//                        if (code.equals("0")) {
//                            pr.dismiss();
//                            Toast.makeText(getApplicationContext(), "Bạn đã đặt phòng thành công !", Toast.LENGTH_LONG).show();
//                            Intent i = new Intent(getBaseContext(), RoomInfor.class);
//                            i.putExtra("id", idhotel);
//                            startActivity(i);
//                            finish();
//                        } else if (code.equals("2")) {
//                            pr.dismiss();
//                            Toast.makeText(getApplicationContext(), "Bạn nhập sai thời gian !", Toast.LENGTH_SHORT).show();
//                        } else if (code.equals("-2")) {
//                            pr.dismiss();
//                            Toast.makeText(getApplicationContext(), "Bạn nhập sai thời gian !", Toast.LENGTH_SHORT).show();
//                        } else {
//                            pr.dismiss();
//                            Toast.makeText(getApplicationContext(), "Bạn vui lòng nhập đủ dữ liệu !", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                    }
//
//                }
//            };
//            BookRoomRequest gcr = new BookRoomRequest(start_date,
//                    end_date, "", name,
//                    phone, email, num_room,
//                    num_per, mHotelId, mHotelName, mHotelEmail, IdHotelManager, link, response);
//            RequestQueue qe = Volley.newRequestQueue(getApplicationContext());
//            qe.add(gcr);
//        }

    }
}
