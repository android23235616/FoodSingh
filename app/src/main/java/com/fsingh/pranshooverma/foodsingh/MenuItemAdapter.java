package com.fsingh.pranshooverma.foodsingh;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRANSHOO VERMA on 13/09/2017.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private Context mContext;

    private  List<MenuItems> menuItems = new ArrayList<>();

    private List<String> dish_name=new ArrayList<>();
    private List<String> dish_price=new ArrayList<>();
    private List<String> NA = new ArrayList<>();
    boolean check=false;

    ImageView plus,minus,fav;


/*
    public MenuItemAdapter(Context mContext, List<String> dish_name, List<String> dish_price) {
        this.mContext=mContext;
        this.dish_name=dish_name;
        this.dish_price=dish_price;
    }

*/

    public MenuItemAdapter(Context mContext, List<MenuItems> menuItems) {
        this.mContext = mContext;
        this.menuItems = menuItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView diname,diprice,item_quantity;
        ImageView pl,mi, image,fav;
        public ViewHolder(View itemView) {
            super(itemView);
            diname=(TextView) itemView.findViewById(R.id.dish_name);
            diprice=(TextView) itemView.findViewById(R.id.dish_price);
            item_quantity=(TextView) itemView.findViewById(R.id.item_quantity);
            pl=(ImageView) itemView.findViewById(R.id.plus);
            mi=(ImageView) itemView.findViewById(R.id.minus);
            fav=(ImageView) itemView.findViewById(R.id.fav);
            image=(ImageView) itemView.findViewById(R.id.img_item);
            Typeface t = Typeface.createFromAsset(diname.getContext().getAssets(), "fonts/android.ttf");
            diname.setTypeface(t);
            diprice.setTypeface(t);
            item_quantity.setTypeface(t);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.cardview_category_menu,parent,false);
        ViewHolder vh=new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        plus=(ImageView) holder.itemView.findViewById(R.id.plus);
        minus=(ImageView) holder.itemView.findViewById(R.id.minus);
        fav=(ImageView) holder.itemView.findViewById(R.id.fav);
        /*
        String name=dish_name.get(position);
        String rupees=dish_price.get(position);
        */

        final MenuItems menuItem = menuItems.get(position);
        String name = menuItem.getName();
        String rupees = menuItem.getPrice();

        holder.diname.setText(name);
        int qty = menuItem.getQuantity();
        holder.item_quantity.setText(""+qty);
        holder.diprice.setText("₹"+rupees);
        if(rupees.equals("0")){
            holder.diprice.setText("NA");
           check = true;
        }else{
            check = false;
        }

        String url = menuItem.getImage();
        Glide.with(mContext).load(url).skipMemoryCache(true).thumbnail(0.05f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(holder.image);

        if(constants.item_name_deb.contains(name))
        {
            int index= constants.item_name_deb.indexOf(name);
            int qa= Integer.parseInt(constants.item_quant_deb.get(index));
            holder.item_quantity.setText(String.valueOf(qa));
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItems item = menuItems.get(position);
                if(!holder.diprice.getText().toString().equals("NA")){
                    int quantity = item.getQuantity();
                    quantity++;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    if(!localdatabase.cartList.contains(item)){
                        localdatabase.cartList.add(item);
                    }

                    menu_category_wise.cartitemcount.setText(String.valueOf(localdatabase.cartList.size()));
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                }

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItems item = menuItems.get(position);
                if(!holder.diprice.getText().toString().equals("NA") && item.getQuantity()!=0){
                    int quantity = item.getQuantity();
                    quantity--;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    if(quantity == 0){
                        localdatabase.cartList.remove(item);
                    }

                    menu_category_wise.cartitemcount.setText(String.valueOf(localdatabase.cartList.size()));
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                }
            }
        });



        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.fav.getDrawable().getConstantState()==holder.fav.getResources().getDrawable(R.drawable.ic_fav).getConstantState())
                {
                    holder.fav.setImageDrawable(holder.fav.getResources().getDrawable(R.drawable.ic_not_fav));
                   int i=0;

                    MenuItems menu=menuItems.get(position);
                    String item_name=menu.getName();
                   Toast.makeText(mContext, item_name, Toast.LENGTH_SHORT).show();
                    for( i=0;i<localdatabase.masterList.size();i++)
                   {
                       MasterMenuItems a=localdatabase.masterList.get(i);
                       List<MenuItems> b=a.getMenuList();
                   for(int j=0;j<b.size();j++)
                   {
                       MenuItems c=b.get(j);
                       String name=c.getName();
                       if(name.contains(item_name))
                       {
                           String price=c.getPrice();
                           String img=c.getImage();
                           Favourites as=new Favourites(name,price,img);
                           localdatabase.favouritesList.add(as);
                           Toast.makeText(mContext, "found", Toast.LENGTH_SHORT).show();
                       }
                   }
                   }
      //              Toast.makeText(mContext,String.valueOf(localdatabase.masterList.size()) , Toast.LENGTH_SHORT).show();
                }
                else
                {
                   holder.fav.setImageDrawable(holder.fav.getResources().getDrawable(R.drawable.ic_fav));
                }
            }
        });

/*
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.diprice.getText().toString().equals("NA")){

                }else{

                //adding to the list
                constants.items_name.add(dish_name.get(position));
                constants.items_price.add(dish_price.get(position));

                //setting the value to the textView for having the quantity
                int a=Integer.parseInt((String) holder.item_quantity.getText())+1;
                holder.item_quantity.setText(String.valueOf(a));

                //showing at the toolbar
                menu_category_wise.cartitemcount.setText(String.valueOf(constants.items_name.size()));
                menu.cartitemcount1.setText(String.valueOf(constants.items_name.size()));
            //    Toast.makeText(mContext, "Added to cart", Toast.LENGTH_SHORT).show();


                //making the quantity count
                if(constants.item_name_deb.contains(dish_name.get(position)))
                {

                    int index= constants.item_name_deb.indexOf(dish_name.get(position));
                    int prev_value= Integer.parseInt(constants.item_quant_deb.get(index));
                    constants.item_quant_deb.set(index,String.valueOf(prev_value+1));
              //      Toast.makeText(mContext, constants.item_name_deb.toString()+"\n"+constants.item_quant_deb, Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //add that item to the arraylist
                    constants.item_name_deb.add(dish_name.get(position));
                    constants.item_quant_deb.add("1");
          //          Toast.makeText(mContext, "added 1", Toast.LENGTH_SHORT).show();
                }

                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.diprice.getText().toString().equals("NA")){

                }else{
                int a=Integer.parseInt((String) holder.item_quantity.getText());
                if(a!=0)
                {
                    a=a-1;
                    holder.item_quantity.setText(String.valueOf(a));
                }


                if(constants.item_name_deb.contains(dish_name.get(position)))
                {

                    int index= constants.item_name_deb.indexOf(dish_name.get(position));
                    int prev_value= Integer.parseInt(constants.item_quant_deb.get(index));

                    if(prev_value==1)
                    {
                        constants.item_name_deb.remove(dish_name.get(position));
                        constants.item_quant_deb.remove(index);
                    }
                    else {
                        constants.item_quant_deb.set(index, String.valueOf(prev_value - 1));
                    }
          //          Toast.makeText(mContext, constants.item_name_deb.toString() + "\n" + constants.item_quant_deb, Toast.LENGTH_SHORT).show();

                }


                if(constants.items_name.contains(dish_name.get(position)))
                {
                    constants.items_name.remove(dish_name.get(position));
                    constants.items_price.remove(dish_price.get(position));
                    menu_category_wise.cartitemcount.setText(String.valueOf(constants.items_name.size()));
                    menu.cartitemcount1.setText(String.valueOf(constants.items_name.size()));
            //        Toast.makeText(mContext, "Removed from cart", Toast.LENGTH_SHORT).show();
                }
                else {
                    //        Toast.makeText(mContext, "You dont have this item in cart", Toast.LENGTH_SHORT).show();
                }        }
            }
        });
        */
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