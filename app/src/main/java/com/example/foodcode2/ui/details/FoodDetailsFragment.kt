import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentFoodDetailsBinding

class FoodDetailsFragment : Fragment() {
    private lateinit var food: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        food = arguments?.getParcelable("food") ?: Food() // Reemplaza Food() con un objeto Food vacío o predeterminado
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_details, container, false)
        val binding = FragmentFoodDetailsBinding.bind(view)
        binding.foodTitleTxt.text = food.title
        binding.foodDescTxt.text = food.strCategory
        binding.foodAreaTxt.text = food.strArea
        val resourceId = resources.getIdentifier(food.strImageSource, "drawable", context?.packageName)
        binding.foodCoverImg.setImageResource(resourceId)
        return view
    }
}