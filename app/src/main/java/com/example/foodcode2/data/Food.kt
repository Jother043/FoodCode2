import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField


data class Food(
    var id: Int = 0,
    var title: String? = "",
    var strDrinkAlternate: String? = null,
    var strCategory: String? = null,
    var strArea: String? = null,
    var instructions: String = "",
    var strMealThumb: String? = null,
    var strTags: String? = null,
    var strYoutube: String? = null,
    var ingredient1: String? = null,
    var ingredient2: String? = null,
    var ingredient3: String? = null,
    var ingredient4: String? = null,
    var ingredient5: String? = null,
    var ingredient6: String? = null,
    var ingredient7: String? = null,
    var ingredient8: String? = null,
    var ingredient9: String? = null,
    var ingredient10: String? = null,
    var ingredient11: String? = null,
    var ingredient12: String? = null,
    var ingredient13: String? = null,
    var ingredient14: String? = null,
    var ingredient15: String? = null,
    var ingredient16: String? = null,
    var ingredient17: String? = null,
    var ingredient18: String? = null,
    var ingredient19: String? = null,
    var ingredient20: String? = null,
    var measure1: String? = null,
    var measure2: String? = null,
    var measure3: String? = null,
    var measure4: String? = null,
    var measure5: String? = null,
    var measure6: String? = null,
    var measure7: String? = null,
    var measure8: String? = null,
    var measure9: String? = null,
    var measure10: String? = null,
    var measure11: String? = null,
    var measure12: String? = null,
    var measure13: String? = null,
    var measure14: String? = null,
    var measure15: String? = null,
    var measure16: String? = null,
    var measure17: String? = null,
    var measure18: String? = null,
    var measure19: String? = null,
    var measure20: String? = null,
    var strSource: String? = null,
    var strImageSource: String? = null,
    var strCreativeCommonsConfirmed: String? = null,
    var dateModified: String? = null,
    var isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(strDrinkAlternate)
        parcel.writeString(strCategory)
        parcel.writeString(strArea)
        parcel.writeString(instructions)
        parcel.writeString(strMealThumb)
        parcel.writeString(strTags)
        parcel.writeString(strYoutube)
        parcel.writeString(ingredient1)
        parcel.writeString(ingredient2)
        parcel.writeString(ingredient3)
        parcel.writeString(ingredient4)
        parcel.writeString(ingredient5)
        parcel.writeString(ingredient6)
        parcel.writeString(ingredient7)
        parcel.writeString(ingredient8)
        parcel.writeString(ingredient9)
        parcel.writeString(ingredient10)
        parcel.writeString(ingredient11)
        parcel.writeString(ingredient12)
        parcel.writeString(ingredient13)
        parcel.writeString(ingredient14)
        parcel.writeString(ingredient15)
        parcel.writeString(ingredient16)
        parcel.writeString(ingredient17)
        parcel.writeString(ingredient18)
        parcel.writeString(ingredient19)
        parcel.writeString(ingredient20)
        parcel.writeString(measure1)
        parcel.writeString(measure2)
        parcel.writeString(measure3)
        parcel.writeString(measure4)
        parcel.writeString(measure5)
        parcel.writeString(measure6)
        parcel.writeString(measure7)
        parcel.writeString(measure8)
        parcel.writeString(measure9)
        parcel.writeString(measure10)
        parcel.writeString(measure11)
        parcel.writeString(measure12)
        parcel.writeString(measure13)
        parcel.writeString(measure14)
        parcel.writeString(measure15)
        parcel.writeString(measure16)
        parcel.writeString(measure17)
        parcel.writeString(measure18)
        parcel.writeString(measure19)
        parcel.writeString(measure20)
        parcel.writeString(strSource)
        parcel.writeString(strImageSource)
        parcel.writeString(strCreativeCommonsConfirmed)
        parcel.writeString(dateModified)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}


