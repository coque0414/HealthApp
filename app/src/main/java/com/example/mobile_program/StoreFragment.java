package com.example.mobile_program;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile_program.Movie;
import com.example.mobile_program.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends Fragment {
    private ListView listView;
    private MovieAdapter adapter;
    private List<Movie> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.store, container, false);

        listView = rootView.findViewById(R.id.list);

        // 데이터를 초기화합니다.
        movieList = new ArrayList<>();
        movieList.add(new Movie("문화상품권 10000원", "포인트", "10만P", "Bast", R.drawable.moo));
        movieList.add(new Movie("문화상품권 5000원", "포인트", "5만P", "", R.drawable.mo50));
        movieList.add(new Movie("종이 상자", "포인트", "1000P", "", R.drawable.box1));
        movieList.add(new Movie("고급 상자", "포인트", "1000P", "", R.drawable.box2));
        movieList.add(new Movie("마크 상자", "포인트", "1000P", "", R.drawable.box3));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));

        // 어댑터를 설정합니다.
        adapter = new MovieAdapter(getContext(), movieList);
        listView.setAdapter(adapter);

        return rootView;
    }
}
