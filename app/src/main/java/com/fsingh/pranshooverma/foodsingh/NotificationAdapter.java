package com.fsingh.pranshooverma.foodsingh;

import android.app.Notification;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 20-10-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<NotificationItem> notificationItems = new ArrayList<>();
    Context c;

    public NotificationAdapter(List<NotificationItem> mainList,Context c){
        this.notificationItems = mainList;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationcard, parent,false);



        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NotificationItem item = notificationItems.get(holder.getAdapterPosition());
        if(item.getNotificationType().equals("1")){
            holder.l1.setVisibility(View.GONE);
            holder.l11.setVisibility(View.GONE);
            holder.l111.setVisibility(View.GONE);
            holder.l2.setVisibility(View.VISIBLE);
            holder.l22.setVisibility(View.VISIBLE);
            holder.l222.setVisibility(View.VISIBLE);

            holder.title1.setText(item.getTitle());
            holder.body1.setText(item.getBody());
            Glide.with(holder.img1.getContext()).load(item.getImg()).into(holder.img1);
        }else if(item.getNotificationType().equals("2")){
            if(item.getNotificationType().equals("1")){
                holder.l2.setVisibility(View.GONE);
                holder.l22.setVisibility(View.GONE);
                holder.l222.setVisibility(View.GONE);
                holder.l1.setVisibility(View.VISIBLE);
                holder.l11.setVisibility(View.VISIBLE);
                holder.l111.setVisibility(View.VISIBLE);

                holder.title2.setText(item.getTitle());
                holder.body2.setText(item.getBody());
                Glide.with(holder.img2.getContext()).load(item.getImg()).into(holder.img2);
            }
        }

    }



    @Override
    public int getItemCount() {
        return notificationItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout l1,l2,l11,l111,l22,l222;
        ImageView img1, img2;
        TextView title1, title2, body1, body2, copycode, knowmore1, knowmore2;

        public ViewHolder(View itemView) {
            super(itemView);
            l1 = (LinearLayout)itemView.findViewById(R.id.l1);
            l2 = (LinearLayout)itemView.findViewById(R.id.l2);
            l11 = (LinearLayout)itemView.findViewById(R.id.l11);
            l111 = (LinearLayout)itemView.findViewById(R.id.l111);
            l22 = (LinearLayout)itemView.findViewById(R.id.l22);
            l22 = (LinearLayout)itemView.findViewById(R.id.l222);
            img1 = (ImageView)itemView.findViewById(R.id.image1);
            img2= (ImageView)itemView.findViewById(R.id.image2);
            title1 = (TextView) itemView.findViewById(R.id.title1);
            title2 = (TextView)itemView.findViewById(R.id.title2);
            body1 = (TextView)itemView.findViewById(R.id.content1text);
            body2 = (TextView)itemView.findViewById(R.id.content2text);
            copycode = (TextView)itemView.findViewById(R.id.copycode);
            knowmore1 = (TextView)itemView.findViewById(R.id.knowmore1);
            knowmore2 = (TextView)itemView.findViewById(R.id.knowmore2);
        }
    }

    private void Display(String s){
        Toast.makeText(c,s,Toast.LENGTH_LONG).show();
    }

}
