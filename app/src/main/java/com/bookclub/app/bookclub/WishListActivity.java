package com.bookclub.app.bookclub;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class WishListActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    WishListAdapter adapter;
    ArrayList<WishListContent> wishListContent;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        linearLayout = findViewById(R.id.linearLayout);
        listView = findViewById(R.id.wishList);

        populateWishList();
        adapter = new WishListAdapter(wishListContent, this);
        listView.setAdapter(adapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
                new ListViewAdapter(listView),
                new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListViewAdapter view, int position) {
                        adapter.remove(position);
                    }
                });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(WishListActivity.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });

    }


    private void populateWishList(){
        wishListContent = new ArrayList<>();
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
        wishListContent.add(new WishListContent("Gülben Author", "Mehmet Kitap", 1, null) );
    }

    private static class ViewHolder{
        TextView authorText, bookText;
        ImageButton bookImage;
        ImageView transactionImageButton;
    }

    public class WishListAdapter extends ArrayAdapter<WishListContent> implements View.OnClickListener {

        ArrayList<WishListContent> dataSet;
        Context context;

        public WishListAdapter(ArrayList<WishListContent> data, Context context) {
            super(context, R.layout.wish_list_item, data);
            this.dataSet = data;
            this.context=context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final WishListContent wishListContent= getItem(position);

            WishListActivity.ViewHolder viewHolder;

            final View result;

            if (convertView == null){
                viewHolder = new WishListActivity.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.wish_list_item, parent, false);

                viewHolder.authorText = convertView.findViewById(R.id.authorTextView);
                viewHolder.bookText = convertView.findViewById(R.id.bookTitleTextView);
                viewHolder.transactionImageButton = convertView.findViewById(R.id.transactionButton);
                viewHolder.bookImage = convertView.findViewById(R.id.bookImageView);

                result = convertView;
                convertView.setTag(viewHolder);

            }
            else{
                viewHolder = (WishListActivity.ViewHolder)convertView.getTag();
                result = convertView;
            }

            viewHolder.authorText.setText(wishListContent.getAuthorName());
            viewHolder.bookText.setText(wishListContent.getBookTitle());

            /*
            if (wishListContent.getTransactionType() == 1){
                viewHolder.transactionImageButton.setBackgroundResource(R.drawable.ic_home_black_24dp);
            }
            else
                viewHolder.transactionImageButton.setBackgroundResource(R.drawable.ic_launcher_foreground);
            */
            viewHolder.bookImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WishListActivity.this, BookDetailActivity.class);
                    intent.putExtra("title", wishListContent.getBookTitle());
                    intent.putExtra("author", wishListContent.getAuthorName());
                    startActivity(intent);
                }
            });
            viewHolder.transactionImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wishListContent.getTransactionType() == 1){
                        Snackbar.make(v, "Transaction Type : Sell", Snackbar.LENGTH_SHORT).show();
                    }
                    else{

                        Snackbar.make(v, "Transaction Type : Trade", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            

            // viewHolder.bookImageButton.setImageDrawable(sadasd);
            //viewHolder.transactionImageButton;


            return convertView;
        }

        public void remove(int position) {
            wishListContent.remove(position);
            notifyDataSetChanged();
        }


        @Override
        public void onClick(View v) {

        }
    }


    class WishListContent {

        private String authorName, bookTitle;
        private int transactionType;
        private Drawable bookImage;

        public WishListContent(String authorName, String bookTitle, int transactionType, Drawable bookImage) {
            this.authorName = authorName;
            this.bookTitle = bookTitle;
            this.transactionType = transactionType;
            this.bookImage = bookImage;
        }

        public WishListContent(WishListContent copy){
            authorName = copy.getAuthorName();
            bookTitle = copy.getAuthorName();
            transactionType = copy.transactionType;
            bookImage = copy.bookImage;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public int getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(int transactionType) {
            this.transactionType = transactionType;
        }

        public Drawable getBookImage() {
            return bookImage;
        }

        public void setBookImage(Drawable bookImage) {
            this.bookImage = bookImage;
        }
    }


}
