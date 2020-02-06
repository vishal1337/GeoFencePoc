import com.google.gson.annotations.SerializedName

data class Tags (

	@SerializedName("alt_name") val altName : String,
	@SerializedName("amenity") val amenity : String,
	@SerializedName("building") val building : String,
	@SerializedName("denomination") val denomination : String,
	@SerializedName("name") val name : String,
	@SerializedName("religion") val religion : String,
	@SerializedName("wikipedia") val wikipedia : String
)