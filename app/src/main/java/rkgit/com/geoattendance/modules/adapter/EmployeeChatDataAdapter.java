package rkgit.com.geoattendance.modules.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.models.ChatMessage;
import rkgit.com.geoattendance.utility.GeoPreference;

/**
 * Created by Ayush Kulshrestha
 * on 20-04-2018.
 */

public class EmployeeChatDataAdapter extends RecyclerView.Adapter<EmployeeChatDataAdapter.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessageList;

    public EmployeeChatDataAdapter(Context context, List<ChatMessage> chatMessageList) {
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        String username = GeoPreference.getInstance().getUsername();
        if (chatMessageList.get(position).messageUser.equalsIgnoreCase(username))
            return R.layout.message_cell;
        else
            return R.layout.others_message_cell;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setVisibility(View.GONE);
        String username = GeoPreference.getInstance().getUsername();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (holder.messageLayout).getLayoutParams();
        if (chatMessageList.get(position).messageUser.equalsIgnoreCase(username))
            holder.senderName.setText("You");
        else
            holder.senderName.setText(chatMessageList.get(position).messageUser);
        (holder.messageLayout).setLayoutParams(params);
        holder.message.setText(chatMessageList.get(position).messageText);
        holder.time.setText(chatMessageList.get(position).messageTime);
        if (position > 0 && chatMessageList.get(position).messageDate != null && chatMessageList.get(position).messageDate.equals(chatMessageList.get(position - 1).messageDate)) {
            holder.date.setVisibility(View.GONE);
        } else {
            if (chatMessageList.get(position).messageDate != null && chatMessageList.get(position).messageDate.trim().length() > 0)
                holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(chatMessageList.get(position).messageDate);
        }
    }

    @Override
    public int getItemCount() {
        if (chatMessageList == null)
            return 0;
        else return chatMessageList.size();
    }

    public void updateAdapter(List<ChatMessage> responseDataList) {
        this.chatMessageList = responseDataList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_cell_layout)
        RelativeLayout messageLayout;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.sender_name)
        TextView senderName;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.date_of_message)
        TextView date;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
