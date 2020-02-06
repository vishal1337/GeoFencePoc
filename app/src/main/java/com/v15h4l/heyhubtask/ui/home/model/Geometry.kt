import com.google.gson.annotations.SerializedName

data class Geometry (

	@SerializedName("lat") val lat : Double,
	@SerializedName("lon") val lon : Double
)