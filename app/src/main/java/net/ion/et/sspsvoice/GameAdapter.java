package net.ion.et.sspsvoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyGameHolder>
{
    public List<GameEntity> mGameList;

    public static class MyGameHolder extends RecyclerView.ViewHolder {

        public TextView tvGameName;
        public TextView tvSubGameName;
        public TextView tvStartTime;

        // 경기 식별자
        public int subGameId;

        public MyGameHolder(View v) {
            super(v);

            tvGameName = (TextView)v.findViewById(R.id.tvGameName);
            tvSubGameName = (TextView)v.findViewById(R.id.tvSubGameName);
            tvStartTime = (TextView)v.findViewById(R.id.tvStartTime);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("subGameId", subGameId);

                    Intent intent = new Intent(v.getContext(), VoiceActivity.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public GameAdapter(List<GameEntity> gameList)
    {
        this.mGameList = gameList;
    }

    @NonNull
    @Override
    public MyGameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_game_view, parent, false);
        MyGameHolder viewHolder = new MyGameHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyGameHolder holder, int position) {

        GameEntity entity = mGameList.get(position);

        holder.tvGameName.setText(entity.gameName);
        holder.tvSubGameName.setText(entity.subGameName);
        holder.tvStartTime.setText(entity.startTime);
        holder.subGameId = entity.subGameId;
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }
}
