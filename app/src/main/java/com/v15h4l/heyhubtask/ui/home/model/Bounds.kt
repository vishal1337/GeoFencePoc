import com.google.gson.annotations.SerializedName

data class Bounds (

	@SerializedName("minlat") val minLat : Double,
	@SerializedName("minlon") val minLon : Double,
	@SerializedName("maxlat") val maxLat : Double,
	@SerializedName("maxlon") val maxLon : Double
)