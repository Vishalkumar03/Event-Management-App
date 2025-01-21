package com.example.eventmanagementapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanagementapp.DetailActivity;
import com.example.eventmanagementapp.MainActivity;
import com.example.eventmanagementapp.R;
import com.example.eventmanagementapp.Utils;
import com.example.eventmanagementapp.auth.LoginActivity;
import com.example.eventmanagementapp.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    List<Event> eventList = new ArrayList<>();
    Context context;
    String eventId;

    public EventAdapter(Context context, List<Event> eventList) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(eventList.get(position).getEventName().toString());
        holder.subtitle.setText(eventList.get(position).getEventDetails().toString());
        holder.eventDate.setText(eventList.get(position).getEventDate());
        holder.eventTime.setText(eventList.get(position).getEventTime());
        holder.eventLocation.setText(eventList.get(position).getEventLocation());


        boolean isEventValid = Utils.compareDates(eventList.get(position).getEventDate().toString());
        if(isEventValid) {
            holder.eventStatus.setText("Upcoming ");
            holder.eventStatusLL.setBackgroundColor(Color.parseColor("#30cf8f"));
        } else {
            holder.eventStatus.setText("Expired ");
            holder.eventStatusLL.setBackgroundColor(Color.parseColor("#fc448b"));
        }

        holder.eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                myIntent.putExtra("title",eventList.get(position).getEventName().toString() );

                eventId = eventList.get(position).getEventId().toString();

                Bundle bundle = new Bundle();
                bundle.putString("event_name", eventList.get(position).getEventName().toString());
                bundle.putString("event_details", eventList.get(position).getEventDetails().toString());
                bundle.putString("event_date", eventList.get(position).getEventDate().toString());
                bundle.putString("event_time", eventList.get(position).getEventTime().toString());
                bundle.putString("event_location", eventList.get(position).getEventLocation().toString());
                bundle.putBoolean("event_status", isEventValid);
                bundle.putString("event_id",  eventId);
                myIntent.putExtras(bundle);

                context.startActivity(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle, eventStatus, eventTime, eventDate, eventLocation;
        LinearLayout eventStatusLL;
        CardView eventCard;
        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.event_name);
            subtitle = view.findViewById(R.id.event_details);
            eventStatus = view.findViewById(R.id.date_check);
            eventTime = view.findViewById(R.id.event_time);
            eventDate = view.findViewById(R.id.event_date);
            eventLocation = view.findViewById(R.id.event_location);
            eventStatusLL = view.findViewById(R.id.status_ll);
            eventCard = view.findViewById(R.id.eventCard);

        }

    }

}
