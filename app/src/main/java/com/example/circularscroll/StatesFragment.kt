package com.example.circularscroll

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class StatesFragment : Fragment(R.layout.fragment_states) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val states = resources.getStringArray(R.array.us_states).sorted()
        view.findViewById<RecyclerView>(R.id.states_list).adapter = StateAdapter(states)
    }
}
