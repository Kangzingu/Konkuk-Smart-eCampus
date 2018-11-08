package kksy.konkuk_smart_ecampus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AttendanceContentsAdapter extends RecyclerView.Adapter<AttendanceContentsAdapter.ViewHolder> {

    final static int ATTEND = 1;
    final static int ABSENT = 2;
    final static int LATE = 3;

    int COLORATTEND;
    int COLORABSENT;
    int COLORLATE;

    private List<Item> data;

    public AttendanceContentsAdapter(List<Item> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public AttendanceContentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_attendance_contents, parent, false);
        ViewHolder holder = new ViewHolder(view);

        COLORATTEND = view.getResources().getColor(R.color.colorGreenAttendPrimary);
        COLORABSENT = view.getResources().getColor(R.color.colorRedAbsentPrimary);
        COLORLATE = view.getResources().getColor(R.color.colorYellowLatePrimary);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceContentsAdapter.ViewHolder holder, int position) {
        Item item = data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        switch (item.state){
            case ATTEND:
                viewHolder.attendanceContents.setBackgroundColor(COLORATTEND);
                break;
            case ABSENT:
                viewHolder.attendanceContents.setBackgroundColor(COLORABSENT);
                break;
            case LATE:
                viewHolder.attendanceContents.setBackgroundColor(COLORLATE);
                break;
        }

        viewHolder.attendanceRound.setText((position + 1) + "차시");
        viewHolder.attendanceDate.setText(item.date);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Item{
        public int state;
        public String date;

        public Item(int state, String date) {
            this.state = state;
            this.date = date;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout attendanceContents;
        public TextView attendanceRound;
        public TextView attendanceDate;

        public ViewHolder(View itemView) {
            super(itemView);
            attendanceContents = (LinearLayout) itemView.findViewById(R.id.layoutAttendanceContents);
            attendanceRound = (TextView) itemView.findViewById(R.id.textViewAttendanceRound);
            attendanceDate = (TextView) itemView.findViewById(R.id.textViewAttendanceDate);
        }
    }
}
