package softagi.androidworkshop1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import softagi.androidworkshop1.Models.BookModel;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;

    bookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);

        layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


    }

    public void search(View view)
    {
        new back().execute("https://www.googleapis.com/books/v1/volumes?q=cars");
    }

    class back extends AsyncTask<String, Void, List<BookModel>>
    {
        List<BookModel> bb;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<BookModel> doInBackground(String... strings)
        {
            bb = Utils.utils(strings[0]);
            return bb;
        }

        @Override
        protected void onPostExecute(List<BookModel> bookModels)
        {
            progressBar.setVisibility(View.GONE);
            adapter = new bookAdapter(bookModels);
            recyclerView.setAdapter(adapter);
        }
    }

    class bookAdapter extends RecyclerView.Adapter<bookAdapter.bookVH>
    {
        List<BookModel> bookModels;

        bookAdapter(List<BookModel> bookModels)
        {
            this.bookModels = bookModels;
        }

        @NonNull
        @Override
        public bookVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.book_item, parent, false);
            return new bookVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull bookVH holder, int position)
        {
            BookModel bookModel = bookModels.get(position);

            String t = bookModel.getTitle();
            String a = bookModel.getAuthor();
            String i = bookModel.getImageurl();

            holder.bookTitle.setText(t);
            holder.bookAuthor.setText(a);

            if (i.isEmpty())
            {
                holder.bookImage.setImageResource(R.drawable.ic_launcher_background);
            } else
                {
                    Picasso.get()
                            .load(i)
                            .into(holder.bookImage);
                }
        }

        @Override
        public int getItemCount()
        {
            return bookModels.size();
        }

        class bookVH extends RecyclerView.ViewHolder
        {
            ImageView bookImage;
            TextView bookTitle,bookAuthor;

            bookVH(@NonNull View itemView)
            {
                super(itemView);

                bookImage = itemView.findViewById(R.id.book_img);
                bookTitle = itemView.findViewById(R.id.book_title);
                bookAuthor = itemView.findViewById(R.id.book_author);
            }
        }
    }
}
