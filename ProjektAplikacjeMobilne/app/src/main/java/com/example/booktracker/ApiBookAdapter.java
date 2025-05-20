package com.example.booktracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booktracker.model.BookResponse;
import java.util.List;

public class ApiBookAdapter extends RecyclerView.Adapter<ApiBookAdapter.BookViewHolder> {
    private List<BookResponse.Item> books;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(BookResponse.Item book);
    }

    public ApiBookAdapter(List<BookResponse.Item> books, OnBookClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookResponse.Item book = books.get(position);
        holder.title.setText(book.volumeInfo.title);
        holder.author.setText(
                book.volumeInfo.authors != null && !book.volumeInfo.authors.isEmpty()
                        ? book.volumeInfo.authors.get(0)
                        : "Unknown"
        );
        holder.description.setText(
                book.volumeInfo.description != null ? book.volumeInfo.description : ""
        );
        holder.itemView.setContentDescription(
                book.volumeInfo.title + " by " +
                        (book.volumeInfo.authors != null && !book.volumeInfo.authors.isEmpty() ? book.volumeInfo.authors.get(0) : "Unknown") +
                        ". " + (book.volumeInfo.description != null ? book.volumeInfo.description : "")
        );
        holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, description;
        BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
            author = itemView.findViewById(R.id.bookAuthor);
            description = itemView.findViewById(R.id.bookDescription);
        }
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public void updateBooks(List<BookResponse.Item> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_search, parent, false);
        return new BookViewHolder(view);
    }

}