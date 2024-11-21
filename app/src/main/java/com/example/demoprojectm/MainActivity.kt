package com.example.demoprojectm
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val selectedItemsTextView: TextView = findViewById(R.id.selectedItemsTextView)

        val items = List(10) { "Item ${it + 1}" }
         adapter = RecyclerViewAdapter(items,
            onLongPress = { position ->
                adapter.toggleSelection(position)
                selectedItemsTextView.text = "Selected: ${adapter.getSelectedItems()}"
            }
         )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


       /* // Initialize the SwipeController with a button click listener
        val swipeController = SwipeController(this) { buttonName ->
            Toast.makeText(this, "$buttonName clicked", Toast.LENGTH_SHORT).show()
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.setOnTouchListener { _, event ->
            swipeController.onTouchEvent(event) // Call the SwipeController's onTouchEvent
            false
        }*/
        val swipeController = SwipeController(this) { buttonName ->
            // This is where the button click is handled
            Toast.makeText(this, "$buttonName clicked", Toast.LENGTH_SHORT).show()
        }

// Attach SwipeController to RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

// Optionally attach the onTouchEvent for button clicks
        swipeController.attachToRecyclerView(recyclerView)

    }
}
