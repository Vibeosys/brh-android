package com.businessreviewshub.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.businessreviewshub.R;
import com.businessreviewshub.data.responseDataDTO.SmsHistoryResponseDTO;
import com.businessreviewshub.utils.DateUtils;
import com.businessreviewshub.views.RobotoTextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shrinivas on 28-12-2016.
 */
public class SmsHistoryAdaptor extends BaseAdapter {

    private ArrayList<SmsHistoryResponseDTO> smsHistoryResponseDTOs;
    private Context context;
    DateUtils dateUtils;


    public SmsHistoryAdaptor(ArrayList<SmsHistoryResponseDTO> smsHistoryResponseDTOs, Context context) {
        this.smsHistoryResponseDTOs = smsHistoryResponseDTOs;
        this.context = context;
        dateUtils = new DateUtils();
    }

    @Override
    public int getCount() {
        return smsHistoryResponseDTOs.size();
    }

    @Override
    public Object getItem(int i) {
        return smsHistoryResponseDTOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder viewHolder = null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.history_row_child, null);
            viewHolder = new ViewHolder();
            viewHolder.mUserName = (RobotoTextView) row.findViewById(R.id.user_name);
            viewHolder.mUserPhoneNo = (RobotoTextView) row.findViewById(R.id.Phone_no);
            viewHolder.mUserOnlyDate = (RobotoTextView) row.findViewById(R.id.date_month);
            viewHolder.mUserOnlyTime = (RobotoTextView) row.findViewById(R.id.time_details);
            row.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        SmsHistoryResponseDTO historyResponseDTO = smsHistoryResponseDTOs.get(i);
        String customerName = historyResponseDTO.getCustomerName();
        String customerPhoneNumber = historyResponseDTO.getPhoneNo();

        String customerDate = historyResponseDTO.getSmsDate();
        Date tempDate = dateUtils.getFormattedDate(customerDate);
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", tempDate);
        String day = (String) android.text.format.DateFormat.format("dd", tempDate);
        String timeStr = (String) android.text.format.DateFormat.format("h:mm a", tempDate);

        viewHolder.mUserName.setText(customerName);
        viewHolder.mUserPhoneNo.setText(customerPhoneNumber);
        viewHolder.mUserOnlyDate.setText("" + day + "\t" + "" + stringMonth);
        viewHolder.mUserOnlyTime.setText(timeStr);

        return row;
    }

    private class ViewHolder {
        com.businessreviewshub.views.RobotoTextView mUserName, mUserPhoneNo, mUserOnlyDate, mUserOnlyTime;
    }

    public void addItems(ArrayList<SmsHistoryResponseDTO> smsHistoryResponseDTOs) {
        this.smsHistoryResponseDTOs = smsHistoryResponseDTOs;
        notifyDataSetChanged();
    }
}
