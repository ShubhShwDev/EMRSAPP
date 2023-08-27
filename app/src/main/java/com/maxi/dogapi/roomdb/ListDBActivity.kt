package com.maxi.dogapi.roomdb

import android.app.Activity
import android.app.Dialog
import android.database.CursorWindow
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxi.dogapi.R
import com.maxi.dogapi.viewmodel.SubmitSurveyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_listdb.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.notifyAll
import java.lang.reflect.Field


const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST = 2

@AndroidEntryPoint
class ListDBActivity : AppCompatActivity(),ClickListen {

    private val viewModel by viewModels<SubmitSurveyViewModel>()
    private lateinit var vm: NoteViewModel
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listdb)
        vm = ViewModelProviders.of(this)[NoteViewModel::class.java]
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setUpRecyclerView()

        setUpListeners()

        vm = ViewModelProviders.of(this)[NoteViewModel::class.java]

        vm.getAllNotes().observe(this, Observer {
            Log.i("Notes observed", "$it")

            adapter.submitList(it)
        })

    }

    private fun setUpListeners() {

        // swipe listener
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.getNoteAt(viewHolder.adapterPosition)
                vm.delete(note)
            }

        }).attachToRecyclerView(recycler_view)
    }


    private fun showDialog(activity: Activity,note:Note) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window: Window? = dialog.window

        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val body = dialog.findViewById(R.id.btn_Ok) as TextView
//        body.text = title

        body.setOnClickListener {
            dialog.dismiss()
            vm.delete(note)
            recycler_view.adapter?.notifyDataSetChanged()



        }
        dialog.show()

    }


    private fun setUpRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        adapter = NoteAdapter(this)
//        adapter = NoteAdapter { clickedNote ->
//            val intent = Intent(this, AddEditNoteActivity::class.java)
//            intent.putExtra(EXTRA_ID, clickedNote.id)
//            intent.putExtra(EXTRA_TITLE, clickedNote.title)
//            intent.putExtra(EXTRA_DESCRIPTION, clickedNote.description)
//            intent.putExtra(EXTRA_PRIORITY, clickedNote.priority)
//            startActivityForResult(intent, EDIT_NOTE_REQUEST)
//        }
        recycler_view.adapter = adapter
    }

    override fun onClickFun(id: String, position: Int, noteHolder: Note) {

        viewModel.fetchSubmitResponse(
            noteHolder.user_id,
            noteHolder.level_id,
            noteHolder.tpqa_id,
            noteHolder.officer_name,
            noteHolder.designation,
            noteHolder.contact,
            noteHolder.email,
            noteHolder.state_id,
            noteHolder.school_id,
            noteHolder.visit_date,
            noteHolder.visit_time,
            noteHolder.lat,
            noteHolder.lon,
            noteHolder.activity_id,
            noteHolder.observation,
            noteHolder.photo,
            noteHolder.int_plumb,
            noteHolder.int_elec_work,
            noteHolder.ext_service,
            noteHolder.oth_dev_work,
            noteHolder.mat_qual,
            noteHolder.over_observation,
            noteHolder.remarks
        )

        viewModel._response_main_form_submit.observe(this) {
            it?.let {

                showDialog(this,noteHolder)
            }
        }
    }
}
