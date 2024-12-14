import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
data class LoginRequest(val email: String, val password: String)


interface UserApiService {

    @POST("api/User/authenticate")
    suspend fun authenticateUser(@Body loginCredentials:LoginRequest): retrofit2.Response<User>

    @GET("api/User/getAllUsers")
    suspend fun getAllUsers(): retrofit2.Response<List<User>>
    @GET("api/User/getAllPickups")
    suspend fun getAllPickups(): retrofit2.Response<List<Pickup>>



    @POST("api/User/add")
    suspend fun postUser(@Body user:User): retrofit2.Response<Int>

    @POST("api/User/addPickup")
    suspend fun postPickup(@Body pickup: Pickup): retrofit2.Response<Int>

    @POST("api/User/addAddress")
    suspend fun postAddress(@Body address: Address): retrofit2.Response<Int>

    @DELETE("api/User/deleteAddress")
    suspend fun deleteAddress(@Query("id") id: Int): retrofit2.Response<Unit>

    @POST("api/User/updateAddress")
    suspend fun updateAddress(@Body address: Address): retrofit2.Response<Unit>

    @PUT("api/User/update")
    suspend fun updateUser(@Body user: User): retrofit2.Response<Unit>

    @PUT("api/User/updatePickup")
    suspend fun updatePickup(@Body pickup: Pickup): retrofit2.Response<Unit>

    @POST("api/User/addPickup")
    suspend fun addPickup(@Body pickup: Pickup): retrofit2.Response<Unit>
}