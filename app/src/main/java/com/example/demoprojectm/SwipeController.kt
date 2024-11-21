package com.example.demoprojectm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeController(
    private val context: Context,
    private val buttonClickListener: (String) -> Unit
) : ItemTouchHelper.Callback() {

    private var buttonWidth = 300f
    private var buttonA: RectF? = null
    private var buttonB: RectF? = null
    private var buttonC: RectF? = null
    private var buttonD: RectF? = null

    enum class ButtonsState {
        GONE, LEFT_VISIBLE, RIGHT_VISIBLE
    }

    private var buttonShowedState = ButtonsState.GONE

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // When the cell is swiped, show the appropriate buttons
        when (direction) {
            ItemTouchHelper.LEFT -> showLeftSwipe()
            ItemTouchHelper.RIGHT -> showRightSwipe()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                // Left swipe
                showLeftSwipe()
                drawButtons(c, viewHolder, true)
            } else if (dX > 0) {
                // Right swipe
                showRightSwipe()
                drawButtons(c, viewHolder, false)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder, isLeftSwipe: Boolean) {
        val itemView = viewHolder.itemView
        val paint = Paint()

        val buttonWidthWithoutPadding = buttonWidth - 20
        val corners = 16f

        if (isLeftSwipe) {
            // Left swipe buttons (ButA, ButB)
            buttonA = RectF(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left + buttonWidthWithoutPadding,
                itemView.bottom.toFloat()
            )
            paint.color = Color.BLUE
            c.drawRoundRect(buttonA!!, corners, corners, paint)
            drawText("ButA", c, buttonA!!, paint)

            buttonB = RectF(
                itemView.left + buttonWidthWithoutPadding,
                itemView.top.toFloat(),
                itemView.left + 2 * buttonWidthWithoutPadding,
                itemView.bottom.toFloat()
            )
            paint.color = Color.GREEN
            c.drawRoundRect(buttonB!!, corners, corners, paint)
            drawText("ButB", c, buttonB!!, paint)
        } else {
            // Right swipe buttons (ButC, ButD)
            buttonC = RectF(
                itemView.right - 2 * buttonWidthWithoutPadding,
                itemView.top.toFloat(),
                itemView.right - buttonWidthWithoutPadding,
                itemView.bottom.toFloat()
            )
            paint.color = Color.YELLOW
            c.drawRoundRect(buttonC!!, corners, corners, paint)
            drawText("ButC", c, buttonC!!, paint)

            buttonD = RectF(
                itemView.right - buttonWidthWithoutPadding,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            paint.color = Color.RED
            c.drawRoundRect(buttonD!!, corners, corners, paint)
            drawText("ButD", c, buttonD!!, paint)
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint) {
        val textSize = 60f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(
            text,
            button.centerX() - (textWidth / 2),
            button.centerY() + (textSize / 2),
            p
        )
    }

    // Detect button clicks based on the touch event
    // Detect button clicks based on the touch event
    fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_UP) {
            when (buttonShowedState) {
                ButtonsState.LEFT_VISIBLE -> {
                    // Check for button A (ButA)
                    buttonA?.let {
                        if (it.contains(event.x, event.y)) {
                            showToast("ButA")
                        }
                    }
                    // Check for button B (ButB)
                    buttonB?.let {
                        if (it.contains(event.x, event.y)) {
                            showToast("ButB")
                        }
                    }
                }
                ButtonsState.RIGHT_VISIBLE -> {
                    // Check for button C (ButC)
                    buttonC?.let {
                        if (it.contains(event.x, event.y)) {
                            showToast("ButC")
                        }
                    }
                    // Check for button D (ButD)
                    buttonD?.let {
                        if (it.contains(event.x, event.y)) {
                            showToast("ButD")
                        }
                    }
                }
                else -> {}
            }
        }
    }

    // Show Toast message for button click
    private fun showToast(buttonName: String) {
        Toast.makeText(context, "$buttonName clicked", Toast.LENGTH_SHORT).show()
    }


    // Show left swipe buttons (ButA, ButB)
    private fun showLeftSwipe() {
        buttonShowedState = ButtonsState.LEFT_VISIBLE
    }

    // Show right swipe buttons (ButC, ButD)
    private fun showRightSwipe() {
        buttonShowedState = ButtonsState.RIGHT_VISIBLE
    }

    // Attach this method to the RecyclerView to detect touch events
    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setOnTouchListener { _, event ->
            onTouchEvent(event)
            false
        }
    }
}
