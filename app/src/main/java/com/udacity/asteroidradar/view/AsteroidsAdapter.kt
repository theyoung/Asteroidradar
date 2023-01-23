package com.udacity.asteroidradar.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.model.Asteroid
import com.udacity.asteroidradar.view.main.MainFragment

class AsteroidsAdapter(val listener: MainFragment.ClickListener) : RecyclerView.Adapter<AsteroidsAdapter.ViewHolder>() {
    var asteroids = listOf<Asteroid>()
        set(data){
            field=data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asteroids[position]
        holder.name.text = item.codename
        holder.date.text = item.closeApproachDate
        holder.image.setImageResource(
            when(item.isPotentiallyHazardous){
                false-> R.drawable.ic_status_normal
                else -> R.drawable.ic_status_potentially_hazardous
            }
        )
        holder.itemView.setOnClickListener{
            listener.click(item)
        }

    }

    override fun getItemCount() = asteroids.size


    class ViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nameItem)
        val date: TextView = itemView.findViewById(R.id.dateItem)
        val image : ImageView = itemView.findViewById(R.id.status)
    }
}