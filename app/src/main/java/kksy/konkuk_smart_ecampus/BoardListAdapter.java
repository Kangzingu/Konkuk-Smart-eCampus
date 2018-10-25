package kksy.konkuk_smart_ecampus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.ViewHolder> {
    List<Board> boardList;

    public BoardListAdapter(List<Board> boardList) {
        this.boardList = boardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.boardTitle.setText(boardList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView boardTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            boardTitle = itemView.findViewById(R.id.textViewBoardTitle);
        }
    }
}
