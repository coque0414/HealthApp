package com.example.mobile_program;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreFragment extends Fragment {
    private ListView listView;
    private MovieAdapter adapter;
    private List<Movie> movieList;


    private USER_DB db;
    private ExecutorService executorService;
    private String loggedInUserId; // 이 부분은 실제 로그인된 유저의 ID로 대체되어야 합니다.


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.store, container, false);

        listView = rootView.findViewById(R.id.list);

        // 데이터를 초기화합니다.
        movieList = new ArrayList<>();
        movieList.add(new Movie("문화상품권 10000원", "포인트", "100000P", "Best", R.drawable.moo));
        movieList.add(new Movie("문화상품권 5000원", "포인트", "50000P", "", R.drawable.mo50));
        movieList.add(new Movie("종이 상자", "포인트", "1000P", "", R.drawable.box1));
        movieList.add(new Movie("고급 상자", "포인트", "1000P", "", R.drawable.box2));
        movieList.add(new Movie("마크 상자", "포인트", "1000P", "", R.drawable.box3));
        movieList.add(new Movie("테스트용임", "포인트", "5P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));
        movieList.add(new Movie("Comming soon", "포인트", "0P", "", R.drawable.com));

        // 어댑터를 설정합니다.
        adapter = new MovieAdapter(getContext(), movieList);
        listView.setAdapter(adapter);

        // DB 설정
        db = Room.databaseBuilder(requireContext(), USER_DB.class, "USER_DB")
                .fallbackToDestructiveMigration()
                .build();
        executorService = Executors.newSingleThreadExecutor();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie selectedMovie = movieList.get(position);
            String pointsString = selectedMovie.getPoints();
            if (pointsString != null) {
                int requiredPoints = Integer.parseInt(pointsString.replace("P", "").replace(",", ""));

                executorService.execute(() -> {
                    USER_ENTITY user = db.userDao().getUserByID(loggedInUserId);
                    if (user == null) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show());
                    } else if (user.point >= requiredPoints) {
                        requireActivity().runOnUiThread(() -> showConfirmationDialog(selectedMovie, user, requiredPoints));
                    } else {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "포인트가 부족합니다!", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(requireContext(), "아이템의 포인트 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

//        // 리스트 아이템 클릭 이벤트
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Movie selectedMovie = movieList.get(position);
//            int requiredPoints = Integer.parseInt(selectedMovie.getGenre().replace("P", "").replace(",", ""));
//
//            executorService.execute(() -> {
//                USER_ENTITY user = db.userDao().getUserByID(loggedInUserId);
//                if (user.point >= requiredPoints) {
//                    requireActivity().runOnUiThread(() -> showConfirmationDialog(selectedMovie, user, requiredPoints));
//                } else {
//                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "포인트가 부족합니다!", Toast.LENGTH_SHORT).show());
//                }
//            });
//        });
//
//        return rootView;
//    }

    private void showConfirmationDialog(Movie movie, USER_ENTITY user, int requiredPoints) {
        new AlertDialog.Builder(requireContext())
                .setTitle("구매 확인")
                .setMessage(movie.getTitle() + "을(를) " + requiredPoints + "포인트에 구매하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> completePurchase(user, requiredPoints))
                .setNegativeButton("아니오", null)
                .show();
    }

    private void completePurchase(USER_ENTITY user, int points) {
        executorService.execute(() -> {
            user.point -= points;
            db.userDao().updateUser(user);
            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "구매 완료!", Toast.LENGTH_SHORT).show());
        });
    }
}