import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SyncWorker(val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Log for debugging
        Log.d("WorkerClass", "It's Working")

        // Retrieve data from SharedPreferences
        val sharedPreferences =
            appContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val userRole = sharedPreferences.getString("userRole", "")
        val isScrapPicker = userRole != "user"
        Log.d("WorkerClass", "User Id: $userId and user role: $userRole")

        GlobalScope.launch {
            try {
                val response = RetrofitInstance.api.getAllUsers()
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        val db = MySQLiteHelper(appContext, "ScrapBuddy", null, 1)
                        Log.d("WorkerClass", "Pickups loaded")

                        for (user in users) {
                            val existingPickup = db.getUser(user.id!!)
                            if (existingPickup != null) {
                                Log.d("WorkerClass", "Updating pickup ${user.toString()}")
                                db.updateUser(user)
                            } else {
                                Log.d("WorkerClass", "Inserting pickup ${user.toString()}")
                                db.insertUser(user)
                            }
                            SyncAddresses(user.addresses)
                            SyncPickups(user.pickups)
                        }

                        Log.d("SyncWorker", "Pickups successfully synced.")
                    }
                } else {
                    Log.d("SyncWorker", "Server response was not successful.")
                }

            } catch (e: Exception) {
                Log.e("SyncWorker", "Exception during sync: ${e.message}", e)
            }
        }

        return Result.success()
    }

    private fun SyncAddresses(addresses: List<Address>?) {
        val db = MySQLiteHelper(appContext, "ScrapBuddy", null, 1)
        if (addresses != null) {
            for (address in addresses) {
                val response = db.getAddress(address.id!!)
                if (response == null)
                    db.insertAddress(address)
                else
                    db.updateAddress(address)
            }
        }

    }

    private fun SyncPickups(pickups: List<Pickup>?) {
        val db = MySQLiteHelper(appContext, "ScrapBuddy", null, 1)
        if (pickups != null) {
            for (pickup in pickups) {
                val response = db.getPickupById(pickup.id!!)
                if (response == null)
                    db.insertPickup(pickup)
                else
                    db.updatePickup(pickup)
            }
        }

    }
}
