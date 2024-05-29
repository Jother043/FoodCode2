import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.foodcode2.databinding.FragmentImageDialogBinding
import android.view.ScaleGestureDetector
import kotlin.math.max
import kotlin.math.min

class ImageDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentImageDialogBinding
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentImageDialogBinding.inflate(layoutInflater)

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(MATCH_PARENT, MATCH_PARENT)

        val imageUrl = arguments?.getString("image_url")
        binding.imageView.load(imageUrl)

        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        binding.imageView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }

        return dialog
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            binding.imageView.scaleX = scaleFactor
            binding.imageView.scaleY = scaleFactor
            return true
        }
    }

    companion object {
        fun newInstance(imageUrl: String): ImageDialogFragment {
            val fragment = ImageDialogFragment()
            val args = Bundle()
            args.putString("image_url", imageUrl)
            fragment.arguments = args
            return fragment
        }
    }
}