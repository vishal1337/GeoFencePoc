import com.google.gson.annotations.SerializedName

data class Hub (

	@SerializedName("type") val type : String,
	@SerializedName("id") val id : Int,
	@SerializedName("bounds") val bounds : Bounds,
	@SerializedName("geometry") val geometry : List<List<Geometry>>,
	@SerializedName("tags") val tags : Tags,
	@SerializedName("osm_type") val osmType : String,
	@SerializedName("db_id") val dbId : String,
	@SerializedName("hub_id") val hubId : String

)