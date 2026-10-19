package com.example.circularscroll

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 *  Creates [MULTIPLIER] copies of the displayed data so circular scrolling
 * can be implemented as a silent scroll back to the middle copy
 */
class StateAdapter(private val states: List<String>) :
    RecyclerView.Adapter<StateAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    /** Number of items in a single copy of the data. */
    val realCount
        get() = states.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state, parent, false) as TextView
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = states[position % states.size]
    }

    override fun getItemCount() = states.size * MULTIPLIER

    companion object {
        const val MULTIPLIER = 3
    }
}
