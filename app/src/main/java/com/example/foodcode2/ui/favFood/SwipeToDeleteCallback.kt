package com.example.foodcode2.ui.favFood

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcode2.R

open class SwipeToDeleteCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete_24dp)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    private val background = GradientDrawable()
    private val shadowPaint = Paint()
    private val shadowPath = Path()

    init {
        shadowPaint.color = Color.BLACK
        shadowPaint.alpha = 50 // Ajusta este valor para cambiar la transparencia de la sombra
        shadowPaint.setShadowLayer(
            5f,
            0f,
            0f,
            Color.BLACK
        ) // Ajusta estos valores para cambiar el tamaño y la posición de la sombra
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Tu código existente aquí
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
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Ajusta los límites del fondo
        val backgroundTop =
            itemView.top + itemHeight / 4 // Empieza el fondo en la mitad superior del elemento
        val backgroundBottom =
            itemView.bottom - itemHeight / 4 // Termina el fondo en la mitad inferior del elemento

        // Dibuja la sombra
        shadowPath.reset()
        shadowPath.addRoundRect(
            itemView.right + dX,
            backgroundTop.toFloat(),
            itemView.right.toFloat(),
            backgroundBottom.toFloat(),
            30f, 30f, // Ajusta estos valores para cambiar el radio de las esquinas de la sombra
            Path.Direction.CW
        )
        c.drawPath(shadowPath, shadowPaint)

        // Dibuja el fondo rojo con degradado
        val startColor = Color.parseColor("#b14040")
        val endColor = Color.parseColor("#e73636")

        // Aplica el degradado al fondo
        background.apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
            colors = intArrayOf(startColor, endColor)
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            cornerRadius = 30f // Ajusta este valor para cambiar el radio de las esquinas
        }
        background.setBounds(
            itemView.right + dX.toInt(),
            backgroundTop,
            itemView.right,
            backgroundBottom
        )
        background.draw(c)

        // Calcula la posición del icono de la papelera
        val iconSize = intrinsicHeight!! / 2 // Reduce el tamaño del icono a la mitad
        val deleteIconTop = itemView.top + (itemHeight - iconSize) / 2
        val deleteIconMargin = (itemHeight - iconSize) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - iconSize
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + iconSize

        // Dibuja el icono de la papelera
        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, Paint().apply {
            xfermode = PorterDuffXfermode(
                PorterDuff.Mode.CLEAR
            )
        })
    }
}