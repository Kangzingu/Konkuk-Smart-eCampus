package kksy.konkuk_smart_ecampus;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;

    public ExpandableListAdapter(List<Item> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType){
            case HEADER:
                view = inflater.inflate(R.layout.item_head_layout, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = inflater.inflate(R.layout.item_subject_layout, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type){
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.headTitle.setText(item.text);
                if(item.invisibleChildren == null){
                    itemController.headToogle.setImageResource(R.drawable.ic_expand_less_24dp);
                }
                else{
                    itemController.headToogle.setImageResource(R.drawable.ic_expand_more_24dp);
                }
                itemController.headToogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.invisibleChildren == null){
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while(data.size() > pos + 1 && data.get(pos + 1).type == CHILD){
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.headToogle.setImageResource(R.drawable.ic_expand_more_24dp);
                        }
                        else{
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for(Item i : item.invisibleChildren){
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.headToogle.setImageResource(R.drawable.ic_expand_less_24dp);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final ListChildViewHolder childViewHolder = (ListChildViewHolder) holder;
                childViewHolder.childTitle.setText(data.get(position).text);
                childViewHolder.childNum.setText(data.get(position).number);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headTitle;
        public ImageView headToogle;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            headTitle = (TextView) itemView.findViewById(R.id.item_head_title);
            headToogle = (ImageView) itemView.findViewById(R.id.item_head_toggle_btn);
        }
    }

    private static class ListChildViewHolder extends  RecyclerView.ViewHolder{
        public TextView childTitle;
        public TextView childNum;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            childTitle = (TextView) itemView.findViewById(R.id.subjectName);
            childNum = (TextView) itemView.findViewById(R.id.subjectNum);
        }
    }

    public static class Item{
        public int type;
        public String text;
        public String number;
        public List<Item> invisibleChildren;

        public Item(){

        }

        public Item(int type, String text) { // Header Item
            this.type = type;
            this.text = text;
        }

        public Item(int type, String text, String number) { // Child Item
            this.type = type;
            this.text = text;
            this.number = number;
        }
    }
}
