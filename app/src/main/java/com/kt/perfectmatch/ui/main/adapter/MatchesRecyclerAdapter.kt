package com.kt.perfectmatch.ui.main.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import coil.ImageLoader
import com.kt.perfectmatch.R
import com.kt.perfectmatch.db.AppDatabase
import com.kt.perfectmatch.db.Matches
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL


class MatchesRecyclerAdapter(mCtx: Context, taskList: List<Matches?>?) :
    RecyclerView.Adapter<MatchesRecyclerAdapter.TasksViewHolder>() {

    private lateinit var dbClient: AppDatabase
    private val mCtx: Context = mCtx
    private var matchesList: List<Matches?>? = taskList
    private val find = Matches()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view: View =
            LayoutInflater.from(mCtx).inflate(
                R.layout.layout_item_matches,
                parent, false
            )
        dbClient = Room.databaseBuilder(
            mCtx,
            AppDatabase::class.java, "MyMatches"
        ).allowMainThreadQueries().build()

        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val t: Matches? = matchesList?.get(position)
        holder.id = position
        holder.name.text = t?.getFullName()
        holder.email.text = t?.getEmail()
        holder.age.text = t?.getAge()
        holder.location.text = t?.getLocation()

        val imageLoader: ImageLoader

        LoadBackground(t?.getPicture(),
        t?.getNat(), holder.clMain).execute()
        matchesList = dbClient.matchDao()?.getAll()
        when {
            t?.getInterested().equals(mCtx.getString(R.string.str_member_accepted)) -> {
                holder.btnAccept.text = mCtx.getString(R.string.str_member_accepted)
                holder.btnReject.isEnabled = false
                holder.btnAccept.isEnabled = false
            }
            t?.getInterested().equals(mCtx.getString(R.string.str_member_declined)) -> {
                holder.btnReject.text = mCtx.getString(R.string.str_member_declined)
                holder.btnReject.isEnabled = false
                holder.btnAccept.isEnabled = false
            }
            t?.getInterested() == null -> {
                holder.btnReject.isEnabled = true
                holder.btnAccept.isEnabled = true
                holder.btnAccept.text = mCtx.getString(R.string.str_accept)
                holder.btnReject.text = mCtx.getString(R.string.str_reject)
            }
        }
        holder.btnAccept.setOnClickListener {
            t?.setInterested(mCtx.getString(R.string.str_member_accepted))
            dbClient.matchDao()?.update(t)
            holder.btnReject.isEnabled = false
            holder.btnAccept.isEnabled = false
            holder.btnAccept.text = mCtx.getString(R.string.str_member_accepted)
        }

        holder.btnReject.setOnClickListener {
            t?.setInterested(mCtx.getString(R.string.str_member_declined))
            dbClient.matchDao()?.update(t)
            holder.btnReject.isEnabled = false
            holder.btnAccept.isEnabled = false
            holder.btnReject.text = mCtx.getString(R.string.str_member_declined)
        }
    }

    override fun getItemCount(): Int {
        return matchesList?.size!!
    }

    class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var id : Int = 0

        val clMain: ConstraintLayout = itemView.findViewById(R.id.cl_main)
        val btnAccept: Button = itemView.findViewById(R.id.btn_accept)
        val btnReject: Button = itemView.findViewById(R.id.btn_reject)
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val age: TextView = itemView.findViewById(R.id.tv_age_height)
        val email: TextView = itemView.findViewById(R.id.tv_email)
        val location: TextView = itemView.findViewById(R.id.tv_place)

    }

    private class LoadBackground(private val imageUrl: String?,
                                 private val imageName: String?, private val view: View) :
        AsyncTask<String?, Void?, Drawable?>() {

        override fun doInBackground(vararg p0: String?): Drawable? {
            return try {
                val `is`: InputStream = fetch(imageUrl.toString()) as InputStream
                Drawable.createFromStream(`is`, imageName)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                null
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        @Throws(MalformedURLException::class, IOException::class)
        private fun fetch(address: String): Any {
            val url = URL(address)
            return url.content
        }

        override fun onPostExecute(result: Drawable?) {
            super.onPostExecute(result)
            view.setBackgroundDrawable(result)
        }
    }
}