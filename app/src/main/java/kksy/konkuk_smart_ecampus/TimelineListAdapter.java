package kksy.konkuk_smart_ecampus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TimelineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> data;

    private TimelineItemClick timelineItemClick;
    public interface TimelineItemClick{
        public void onClick(View view, int position);
    }

    public void setItemClick(TimelineItemClick timelineItemClick){
        this.timelineItemClick = timelineItemClick;
    }

    public TimelineListAdapter(List<Item> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.item_timeline_layout, parent, false);
        ListTimelineViewHolder holder = new ListTimelineViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Item item = data.get(position);
        final ListTimelineViewHolder timelineHolder = (ListTimelineViewHolder) holder;

        timelineHolder.headTitle.setText(item.timeline_title);
        timelineHolder.title.setText(item.timeline_title);
        timelineHolder.subject.setText(item.timeline_subject);
        timelineHolder.date.setText(item.timeline_date);
        if(item.isOpen){
            timelineHolder.headToogle.setImageResource(R.drawable.ic_expand_less_24dp);
        }
        else{
            timelineHolder.headToogle.setImageResource(R.drawable.ic_expand_more_24dp);
        }
        timelineHolder.headToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isOpen){
                    timelineHolder.contentView.setVisibility(View.GONE);
                    item.isOpen = false;
                    timelineHolder.headToogle.setImageResource(R.drawable.ic_expand_more_24dp);
                }
                else{
                    timelineHolder.contentView.setVisibility(View.VISIBLE);
                    item.isOpen = true;
                    timelineHolder.headToogle.setImageResource(R.drawable.ic_expand_less_24dp);
                }
            }
        });
        timelineHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timelineItemClick != null){
                    timelineItemClick.onClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListTimelineViewHolder extends  RecyclerView.ViewHolder{
        public TextView headTitle;
        public ImageView headToogle;
        public LinearLayout contentView;
        public TextView title;
        public TextView subject;
        public TextView date;
        public ImageView img;

        public ListTimelineViewHolder(View itemView) {
            super(itemView);
            headTitle = (TextView) itemView.findViewById(R.id.item_timeline_title);
            headToogle = (ImageView) itemView.findViewById(R.id.item_timeline_toggle_btn);
            contentView = (LinearLayout) itemView.findViewById(R.id.timelineContentView);
            title = (TextView) itemView.findViewById(R.id.textViewTimelineTitle);
            subject = (TextView) itemView.findViewById(R.id.textViewTimelineSubject);
            date = (TextView) itemView.findViewById(R.id.textViewTimelineDate);
            img = (ImageView) itemView.findViewById(R.id.imageViewTimeline);
        }
    }

    public static class Item{
        public String timeline_title;
        public String timeline_subject;
        public String timeline_date;
        public boolean isOpen;

        public Item(String timeline_subject, String timeline_title, String timeline_date) {
            this.timeline_subject = timeline_subject;
            this.timeline_title = timeline_title;
            this.timeline_date = timeline_date;
            this.isOpen = false;
        }
    }
}
