package com.fsingh.pranshooverma.foodsingh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by PRANSHOO VERMA on 13/09/2017.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private Context mContext;
    public static List<MenuItems> menuItems = new ArrayList<>();
    MenuItems menuItem;

    Gson gson = new Gson();

    private List<MenuItems> FavItems = new ArrayList<>();

    boolean check=false;

    int p;

    ImageView plus,minus,fav,image;

    SharedPreferences store;





    public MenuItemAdapter(Context mContext, List<MenuItems> menuItems,int p) {
        this.mContext = mContext;
        this.menuItems = menuItems;
        this.mContext=mContext;
        this.gson = new Gson();
        this.store = mContext.getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
        this.p = p;
        String tempJson = store.getString(constants.fav,"");

        if(!tempJson.equals("")){
            FavouritesList f = gson.fromJson(tempJson,FavouritesList.class);
            this.FavItems = f.getFavouriteList();
        }
    }


    private void refreshList(){

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView diname,diprice,item_quantity, unavailable;
        ImageView pl,mi, image,fav;
        public ViewHolder(View itemView) {
            super(itemView);
            diname=(TextView) itemView.findViewById(R.id.dish_name_slide);
            diprice=(TextView) itemView.findViewById(R.id.dish_price);
            unavailable=(TextView) itemView.findViewById(R.id.txt_unavailable);
            item_quantity=(TextView) itemView.findViewById(R.id.item_quantity_slide);
            pl=(ImageView) itemView.findViewById(R.id.plus_slide);
            mi=(ImageView) itemView.findViewById(R.id.minus_slide);
            fav=(ImageView) itemView.findViewById(R.id.fav);
            image=(ImageView) itemView.findViewById(R.id.img_item_slide);
            Typeface t = Typeface.createFromAsset(diname.getContext().getAssets(), "fonts/gadugib.ttf");
            diname.setTypeface(t);
            diprice.setTypeface(t);
            item_quantity.setTypeface(t);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisplayList(FavItems);
        View v= LayoutInflater.from(mContext).inflate(R.layout.cardview_category_menu,parent,false);

        ViewHolder vh=new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if(menu_category_wise.position==-1){
            holder.fav.setVisibility(View.VISIBLE);
            holder.fav.setClickable(true);
        }


         menuItem = menuItems.get(holder.getAdapterPosition());

        if(Exists(menuItem)>-1){
            holder.fav.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_fav));
        }else{
            holder.fav.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_not_fav));

        }

        String name = menuItem.getName();
        String rupees = menuItem.getPrice();
        final String status = menuItem.getStatus();
        holder.diname.setText(name);
        int qty = menuItem.getQuantity();
        holder.item_quantity.setText("" + qty);
        holder.diprice.setText("₹" + rupees);
        Log.d("sdsd", name + "=" + status);
       // if((rupees!=null&&status!=null)&&menu_category_wise.position!=-1) {
            if (rupees.equals("0") || !status.equals("live")) {
                holder.diprice.setText("NA");
                holder.unavailable.setVisibility(View.VISIBLE);
                check = true;
            } else {
                check = false;
                holder.unavailable.setVisibility(View.GONE);
            }
       // }else{
        //        status.equals("");
        //}
        String url = menuItem.getImage();
        Glide.with(mContext).load(url).skipMemoryCache(true).thumbnail(0.05f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(holder.image);

        if (constants.item_name_deb.contains(name)) {
            int index = constants.item_name_deb.indexOf(name);
            int qa = Integer.parseInt(constants.item_quant_deb.get(index));
            holder.item_quantity.setText(String.valueOf(qa));
        }

        holder.pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItems item = menuItems.get(position);
                if (!holder.diprice.getText().toString().equals("NA") && status.equals("live")) {
                    int quantity = item.getQuantity();
                    quantity++;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    if (checkCart(item) == -1) {
                        localdatabase.cartList.add(item);
                        Toast.makeText(mContext, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContext, "found", Toast.LENGTH_SHORT).show();
                    }

                    menu_category_wise.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                } else {
                    Toast.makeText(mContext, "Sorry, item not available.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItems item = menuItems.get(holder.getAdapterPosition());
                if (!holder.diprice.getText().toString().equals("NA") && item.getQuantity() != 0) {
                    int quantity = item.getQuantity();
                    quantity--;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    if (quantity == 0) {
                        //localdatabase.cartList.remove(item);
                        int pos = checkCart(item);
                        if(pos != -1){
                            localdatabase.cartList.remove(pos);
                        }
                    }

                    menu_category_wise.cartitemcount.setText(String.valueOf(localdatabase.cartList.size()));
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                }
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(mContext, menu_item_details.class);
                MenuItems item = menuItems.get(holder.getAdapterPosition());
                if(!holder.diprice.getText().toString().equals("NA")) {
                    Bundle b=new Bundle();
                    MenuItems men=menuItems.get(holder.getAdapterPosition());
                    b.putString("item_name",men.getName());
                    b.putString("item_image",men.getImage());
                    b.putString("item_price",men.getPrice());
                    b.putParcelable("object",item);
                    b.putString("item_quantity",String.valueOf(men.getQuantity()));
                    b.putInt("position",holder.getAdapterPosition());
                    boolean isfav = false;

                    MenuItems i = menuItems.get(holder.getAdapterPosition());
                    if(Exists(i)>-1){
                        isfav = true;
                    }
                    b.putBoolean("isfav", isfav);
                    b.putParcelable("mainobject", item);
                    b.putInt("mainposition",p);
                    b.putInt("avail",0);
                    s.putExtras(b);
                    mContext.startActivity(s);

                    ((Activity)mContext).finish();
                }else {
                    Bundle b=new Bundle();
                    MenuItems men=menuItems.get(holder.getAdapterPosition());
                    b.putString("item_name",men.getName());
                    b.putString("item_image",men.getImage());
                    b.putString("item_price",men.getPrice());
                    b.putParcelable("object",item);
                    b.putString("item_quantity",String.valueOf(men.getQuantity()));
                    b.putInt("position",holder.getAdapterPosition());
                    b.putInt("avail",1);
                    boolean isfav = false;

                    MenuItems i = menuItems.get(holder.getAdapterPosition());
                    if(Exists(i)>-1){
                        isfav = true;
                    }
                    b.putBoolean("isfav", isfav);
                    b.putParcelable("mainobject", item);
                    b.putInt("mainposition",p);
                    s.putExtras(b);
                    mContext.startActivity(s);

                    ((Activity)mContext).finish();
                }
              // mContext.startActivity(s);
            }
        });

    holder.fav.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MenuItems item = menuItems.get(holder.getAdapterPosition());

             if(Exists(item)>-1){
                holder.fav.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_not_fav));
                FavItems.remove(Exists(item));
                Log.i("favvvv","exists");
            }else{
                Log.i("favvvv","called");
                holder.fav.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_fav));
                FavItems.add(item);
            }
            DisplayList(FavItems);
            FavouritesList f = new FavouritesList(FavItems);
            String tempJson = gson.toJson(f);
            SharedPreferences.Editor edit = store.edit();
            edit.putString(constants.fav,tempJson);
            edit.apply();

        }
    });
    }


    private void DisplayList(List<MenuItems> m){
        for(int i=0; i<m.size(); i++){
            Log.i("activityhere"+i, m.get(i).getName()+" , "+m.get(i).getId());
        }
    }


    private int Exists(MenuItems item){
        refreshList();
        for(int i=0; i<FavItems.size(); i++){
            if(FavItems.get(i).getId().equals(item.getId())
                    &&FavItems.get(i).getName().equals(item.getName())
                    ){
                return i;
            }
        }
        return -1;


    }

    private int checkCart(MenuItems item){
        for(int i=0; i<localdatabase.cartList.size(); i++){
            if(localdatabase.cartList.get(i).getId().equals(item.getId())
                    && localdatabase.cartList.get(i).getName().equals(item.getName())
                    ){
                return i;
            }
        }
        return -1;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}


///adlasdjlakdjlaskdkj