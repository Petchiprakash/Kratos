package com.example.kratos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipantsAdapter(
    private var participantList: ArrayList<ParticipantsData>, var context: Context
) : RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
            R.layout.item_list,
            parent, false
        )
        return ParticipantsViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {
        val currentItem = participantList[position]
        holder.firstName.text = currentItem.firstName
        holder.lastName.text = currentItem.lastName
        holder.emailId.text = currentItem.email
        holder.mobileNum.text = currentItem.mobileNo
        holder.itemView.setOnClickListener {
            val email: String? = currentItem.email
            val intent = Intent(context, RoundCheckActivity::class.java)
            intent.putExtra("email", email)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return participantList.size
    }

    class ParticipantsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val firstName: TextView = itemView.findViewById(R.id.tvFirstName)
        val lastName: TextView = itemView.findViewById(R.id.tvLastName)
        val emailId: TextView = itemView.findViewById(R.id.tvEmail)
        val mobileNum: TextView = itemView.findViewById(R.id.tvMobileNum)

    }
}