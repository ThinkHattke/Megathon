package com.example.gaurav.joveo_megathon;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    View view;
    private Context context;
    private List<Messages> mMessageList;
    public TextView messageTime;
    Messages message_time;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);


        return new MessageViewHolder(v);

    }


    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        message_time = new Messages();

        if (from_user.equals("me")) {

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
            viewHolder.messageText.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams params = (new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); viewHolder.messageText.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(0,0,150,0);
            viewHolder.messageText.setLayoutParams(params);
        }
        else {

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background_1);
            viewHolder.messageText.setTextColor(Color.BLACK);
            viewHolder.messageText.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams params = (new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder.messageText.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            viewHolder.messageText.setLayoutParams(params);
        }

        viewHolder.messageText.setText(c.getMessage());
        viewHolder.messageImage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {

        return mMessageList.size();

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView displayName;
        public ImageView messageImage;


        public MessageViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
//            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
            messageTime = view.findViewById(R.id.message_time1);

        }
    }
}