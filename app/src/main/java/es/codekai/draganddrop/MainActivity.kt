package es.codekai.draganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import es.codekai.draganddrop.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var miSelected: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBoy.setOnLongClickListener(longClickListener)



        binding.ivBoy.tag = "boy"
        binding.ivCart.setOnDragListener(dragListener)
        binding.ivCart.tag = "cart"
    }

    private val longClickListener = View.OnLongClickListener { v ->
        val item = ClipData.Item(v.tag as? CharSequence)

        miSelected = v

        Log.d("Juanra", v.tag.toString())

        val dragData = ClipData(
            v.tag as? CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            item
        )

        val myShadow = MyShadowBuilder(v)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(dragData, myShadow, null, 0)
        }

        true
    }

    private val dragListener = View.OnDragListener { v, event ->
        val receiverView: ImageView = v as ImageView
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                binding.tvTitulo.text = "me muevo"
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                binding.tvTitulo.text = "has entrado nano"
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                binding.tvTitulo.text = "has salido nano"
                true
            }
            DragEvent.ACTION_DROP -> {
                binding.tvTitulo.text =
                    "has soltado dentro ${receiverView.tag}, ${event.clipDescription.label}"

                miSelected.isVisible = false

                true
            }
            else -> false
        }
    }

    private class MyShadowBuilder(val v: View) : View.DragShadowBuilder(v) {

        init {
            Log.d("Juanra2", view.tag.toString())
        }


        override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
            outShadowSize.set(view.width, view.height)
            outShadowTouchPoint.set(view.width / 2, view.height / 2)
        }

        override fun onDrawShadow(canvas: Canvas?) {
            v.draw(canvas)
        }
    }
}