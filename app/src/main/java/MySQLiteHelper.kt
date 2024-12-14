import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        private const val DATABASE_NAME = "ScrapBuddy"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_TABLE = """
            CREATE TABLE user (
                id INTEGER PRIMARY KEY,
                name TEXT,
                email TEXT,
                password TEXT,
                phoneNumber TEXT,
                role TEXT,
                createdAt TEXT,
                updatedAt TEXT
            )
        """.trimIndent()

        val CREATE_ADDRESS_TABLE = """
            CREATE TABLE address (
                id INTEGER PRIMARY KEY,
                userId INTEGER,
                street TEXT,
                city TEXT,
                zipcode TEXT,
                FOREIGN KEY(userId) REFERENCES user(id)
            )
        """.trimIndent()

        val CREATE_PICKUP_TABLE = """
            CREATE TABLE pickup (
                id INTEGER PRIMARY KEY,
                customerId INTEGER,
                riderId INTEGER,
                addressId INTEGER,
                pickupDate TEXT,
                pickupTime TEXT,
                status TEXT,
                totalWeight TEXT,
                createdAt TEXT,
                updatedAt TEXT,
                FOREIGN KEY(customerId) REFERENCES user(id),
                FOREIGN KEY(riderId) REFERENCES user(id),
                FOREIGN KEY(addressId) REFERENCES address(id)
            )
        """.trimIndent()


        val CREATE_SCRAP_ITEM_TABLE = """
            CREATE TABLE scrap_item (
                id INTEGER PRIMARY KEY,
                name TEXT,
                scrapType TEXT,
                description TEXT,
                pricePerKg REAL,
                createdAt TEXT,
                updatedAt TEXT
            )
        """.trimIndent()

        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_ADDRESS_TABLE)
        db.execSQL(CREATE_PICKUP_TABLE)
        db.execSQL(CREATE_SCRAP_ITEM_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS address")
        db.execSQL("DROP TABLE IF EXISTS pickup")
        db.execSQL("DROP TABLE IF EXISTS pickup_scrap_item")
        db.execSQL("DROP TABLE IF EXISTS scrap_item")
        onCreate(db)
    }

    //Insert User
    fun insertUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", user.id)
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("phoneNumber", user.phoneNumber)
            put("role", user.role)
            put("createdAt", user.createdAt)
            put("updatedAt", user.updatedAt)
        }
        db.insert("user", null, values)
    }

    //Update user
    fun updateUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("phoneNumber", user.phoneNumber)
            put("updatedAt", user.updatedAt)
        }
        db.update("user", values, "id = ?", arrayOf(user.id.toString()))
    }

    //Insert Address
    fun insertAddress(address: Address) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", address.id)
            put("userId", address.userId)
            put("street", address.street)
            put("city", address.city)
            put("zipcode", address.zipcode)
        }
        db.insert("address", null, values)
    }


    //Update Address
    fun updateAddress(address: Address) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("userId", address.userId)
            put("street", address.street)
            put("city", address.city)
            put("zipcode", address.zipcode)
        }
        db.update("address", values, "id = ?", arrayOf(address.id.toString()))
    }

    //Delte Address
    fun deleteAddress(id: Int): Unit {
        val db = this.writableDatabase
        db.delete("address", "id = ?", arrayOf(id.toString()))
        db.close()

    }


    //Insert Pickup
    fun insertPickup(pickup: Pickup) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", pickup.id)
            put("customerId", pickup.customerId)
            put("addressId", pickup.addressId)
            put("riderId", pickup.riderId)
            put("pickupDate", pickup.pickupDate)
            put("pickupTime", pickup.pickupTime)
            put("status", pickup.status)
            put("totalWeight", pickup.totalWeight)
            put("createdAt", pickup.createdAt)
            put("updatedAt", pickup.updatedAt)
        }
        db.insert("pickup", null, values)
    }

    //Update Pickup
    fun updatePickup(pickup: Pickup) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("customerId", pickup.customerId)
            put("addressId", pickup.addressId)
            put("riderId", pickup.riderId)
            put("pickupDate", pickup.pickupDate)
            put("pickupTime", pickup.pickupTime)
            put("status", pickup.status)
            put("totalWeight", pickup.totalWeight)
            put("createdAt", pickup.createdAt)
            put("updatedAt", pickup.updatedAt)
        }
        db.update("pickup", values, "id = ?", arrayOf(pickup.id.toString()))
    }

    //insert pickupScrapItem
    fun insertPickupScrapItem(pickupScrapItem: PickupScrapItem) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("pickupId", pickupScrapItem.pickupId)
            put("scrapItemId", pickupScrapItem.scrapItemId)
            put("quantityKg", pickupScrapItem.quantityKg)
        }
        db.insert("pickup_scrap_item", null, values)
    }


    //Insert ScrapItem:
    fun insertScrapItem(scrapItem: ScrapItem) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", scrapItem.id)
            put("name", scrapItem.name)
            put("scrapType", scrapItem.scrapType)
            put("description", scrapItem.description)
            put("pricePerKg", scrapItem.pricePerKg)
            put("createdAt", scrapItem.createdAt)
            put("updatedAt", scrapItem.updatedAt)
        }
        db.insert("scrap_item", null, values)
    }

    //Update ScrapItem
    fun updateScrapItem(scrapItem: ScrapItem) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", scrapItem.name)
            put("scrapType", scrapItem.scrapType)
            put("description", scrapItem.description)
            put("pricePerKg", scrapItem.pricePerKg)
            put("createdAt", scrapItem.createdAt)
            put("updatedAt", scrapItem.updatedAt)
        }
        db.update("scrap_item", values, "id = ?", arrayOf(scrapItem.id.toString()))
    }

    //Get User
    fun getUser(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query("user", null, "id = ?", arrayOf(userId.toString()), null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")),
                role = cursor.getString(cursor.getColumnIndexOrThrow("role")),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt")),
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
            )
        } else null
    }

    //GetAll Users
    fun getAllUsers(): List<User> {
        val db = this.readableDatabase
        val cursor = db.query("user", null, null, null, null, null, null)
        val users = mutableListOf<User>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")),
                    role = cursor.getString(cursor.getColumnIndexOrThrow("role")),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt")),
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }


    //Get addresses for user:
    fun getAddressesForUser(userId: Int): List<Address> {
        val db = this.readableDatabase
        val cursor =
            db.query("address", null, "userId = ?", arrayOf(userId.toString()), null, null, null)
        val addresses = mutableListOf<Address>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val address = Address(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    street = cursor.getString(cursor.getColumnIndexOrThrow("street")),
                    city = cursor.getString(cursor.getColumnIndexOrThrow("city")),
                    zipcode = cursor.getString(cursor.getColumnIndexOrThrow("zipcode"))
                )
                addresses.add(address)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return addresses
    }

    //Fetch single address:
    fun getAddress(addressId: Int): Address? {
        val db = this.readableDatabase
        val cursor =
            db.query("address", null, "id = ?", arrayOf(addressId.toString()), null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            Address(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                street = cursor.getString(cursor.getColumnIndexOrThrow("street")),
                city = cursor.getString(cursor.getColumnIndexOrThrow("city")),
                zipcode = cursor.getString(cursor.getColumnIndexOrThrow("zipcode"))
            )
        } else null
    }

    // Fetch pickups for user:
    fun getPickupsForCustomer(userId: Int, completed: Boolean): List<Pickup> {
        val db = this.readableDatabase
        val cursor = if (completed) {
            // If status is empty, query only by customerId
            db.query(
                "pickup",
                null,  // Select all columns
                "customerId = ? AND status == ?",  // WHERE clause
                arrayOf(userId.toString(), "completed"),  // Arguments for WHERE clause
                null,  // Group By
                null,  // Having
                null   // Order By
            )
        } else {
            // If status is provided, query by customerId and status
            db.query(
                "pickup",
                null,  // Select all columns
                "customerId = ? AND status != ?",  // WHERE clause
                arrayOf(userId.toString(), "completed"),  // Arguments for WHERE clause
                null,  // Group By
                null,  // Having
                null   // Order By
            )
        }

        val pickups = mutableListOf<Pickup>()
        if (cursor.moveToFirst()) {
            do {
                val pickup = Pickup(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("id")) },
                    customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customerId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("customerId")) },
                    riderId = cursor.getInt(cursor.getColumnIndexOrThrow("riderId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("riderId")) },
                    addressId = cursor.getInt(cursor.getColumnIndexOrThrow("addressId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("addressId")) },
                    pickupDate = cursor.getString(cursor.getColumnIndexOrThrow("pickupDate"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupDate")) },
                    pickupTime = cursor.getString(cursor.getColumnIndexOrThrow("pickupTime"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupTime")) },
                    status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("status")) }
                        ?: "pending",
                    totalWeight = cursor.getString(cursor.getColumnIndexOrThrow("totalWeight"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("totalWeight")) },
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("createdAt")) },
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("updatedAt")) }
                )
                pickups.add(pickup)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pickups
    }

    //Get Pickups for rider
    fun getPickupsForRider(userId: Int, status: String): List<Pickup> {
        val db = this.readableDatabase
        val cursor = db.query(
            "pickup",
            null,  // Select all columns
            "status == ?",  // WHERE clause
            arrayOf(status),  // Arguments for WHERE clause
            null,  // Group By
            null,  // Having
            null   // Order By
        )


        val pickups = mutableListOf<Pickup>()
        if (cursor.moveToFirst()) {
            do {
                val pickup = Pickup(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("id")) },
                    customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customerId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("customerId")) },
                    riderId = cursor.getInt(cursor.getColumnIndexOrThrow("riderId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("riderId")) },
                    addressId = cursor.getInt(cursor.getColumnIndexOrThrow("addressId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("addressId")) },
                    pickupDate = cursor.getString(cursor.getColumnIndexOrThrow("pickupDate"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupDate")) },
                    pickupTime = cursor.getString(cursor.getColumnIndexOrThrow("pickupTime"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupTime")) },
                    status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("status")) }
                        ?: "pending",
                    totalWeight = cursor.getString(cursor.getColumnIndexOrThrow("totalWeight"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("totalWeight")) },
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("createdAt")) },
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("updatedAt")) }
                )
                pickups.add(pickup)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pickups
    }


    // Fetch pickups for user:
// Fetch pickups for user:
    fun getAllPickups(): List<Pickup> {
        val db = this.readableDatabase
        val cursor =
            // If status is empty, query only by customerId
            db.query(
                "pickup",
                null,  // Select all columns
                "status ==?",  // WHERE clause
                arrayOf("pending"), // Arguments for WHERE clause
                null,  // Group By
                null,  // Having
                null   // Order By
            )

        val pickups = mutableListOf<Pickup>()
        if (cursor.moveToFirst()) {
            do {
                val pickup = Pickup(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("id")) },
                    customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customerId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("customerId")) },
                    riderId = cursor.getInt(cursor.getColumnIndexOrThrow("riderId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("riderId")) },
                    addressId = cursor.getInt(cursor.getColumnIndexOrThrow("addressId"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("addressId")) },
                    pickupDate = cursor.getString(cursor.getColumnIndexOrThrow("pickupDate"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupDate")) },
                    pickupTime = cursor.getString(cursor.getColumnIndexOrThrow("pickupTime"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupTime")) },
                    status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("status")) }
                        ?: "pending",
                    totalWeight = cursor.getString(cursor.getColumnIndexOrThrow("totalWeight"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("totalWeight")) },
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("createdAt")) },
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
                        .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("updatedAt")) }
                )
                pickups.add(pickup)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pickups
    }

    //Fetch pickup by id:
    fun getPickupById(pickupId: Int): Pickup? {
        val db = this.readableDatabase
        val cursor = db.query(
            "pickup",
            null,  // Select all columns
            "id = ?",  // WHERE clause to filter by id
            arrayOf(pickupId.toString()),  // Argument for WHERE clause
            null,  // Group By
            null,  // Having
            null   // Order By
        )

        var pickup: Pickup? = null
        if (cursor.moveToFirst()) {
            pickup = Pickup(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("id")) },
                customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customerId"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("customerId")) },
                riderId = cursor.getInt(cursor.getColumnIndexOrThrow("riderId"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("riderId")) },
                addressId = cursor.getInt(cursor.getColumnIndexOrThrow("addressId"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("addressId")) },
                pickupDate = cursor.getString(cursor.getColumnIndexOrThrow("pickupDate"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupDate")) },
                pickupTime = cursor.getString(cursor.getColumnIndexOrThrow("pickupTime"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("pickupTime")) },
                status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("status")) }
                    ?: "pending",
                totalWeight = cursor.getString(cursor.getColumnIndexOrThrow("totalWeight"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("totalWeight")) },
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("createdAt")) },
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
                    .takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow("updatedAt")) }
            )
        }
        cursor.close()
        return pickup
    }

    /*
    //Fetch pickup for user:
    fun getPickupsForUser(userId: Int): List<Pickup> {
        val db = this.readableDatabase
        val cursor = db.query(
            "pickup",
            null,
            "customerId = ? OR riderId = ?",
            arrayOf(userId.toString(), userId.toString()),
            null,
            null,
            null
        )
        val pickups = mutableListOf<Pickup>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val pickup = Pickup(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customerId")),
                    riderId = cursor.getInt(cursor.getColumnIndexOrThrow("riderId")),
                    pickupDate = cursor.getString(cursor.getColumnIndexOrThrow("pickupDate")),
                    pickupTime = cursor.getString(cursor.getColumnIndexOrThrow("pickupTime")),
                    status = cursor.getString(cursor.getColumnIndexOrThrow("status")),
                    totalWeight = cursor.getDouble(cursor.getColumnIndexOrThrow("totalWeight")),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt")),
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
                )
                pickups.add(pickup)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pickups
    }

    //Fetch single pickup
    fun getPickup(pickupId: Int): Pickup? {
        val db = this.readableDatabase
        val cursor =
            db.query("pickup", null, "id = ?", arrayOf(pickupId.toString()), null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            Pickup(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customerId")),
                riderId = cursor.getInt(cursor.getColumnIndexOrThrow("riderId")),
                pickupDate = cursor.getString(cursor.getColumnIndexOrThrow("pickupDate")),
                pickupTime = cursor.getString(cursor.getColumnIndexOrThrow("pickupTime")),
                status = cursor.getString(cursor.getColumnIndexOrThrow("status")),
                totalWeight = cursor.getDouble(cursor.getColumnIndexOrThrow("totalWeight")),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt")),
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
            )
        } else null
    }
*/
    //Fetch pickupScrapitem
    fun getPickupScrapItemsForPickup(pickupId: Int): List<PickupScrapItem> {
        val db = this.readableDatabase
        val cursor = db.query(
            "pickup_scrap_item",
            null,
            "pickupId = ?",
            arrayOf(pickupId.toString()),
            null,
            null,
            null
        )
        val scrapItems = mutableListOf<PickupScrapItem>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val scrapItem = PickupScrapItem(
                    pickupId = cursor.getInt(cursor.getColumnIndexOrThrow("pickupId")),
                    scrapItemId = cursor.getInt(cursor.getColumnIndexOrThrow("scrapItemId")),
                    quantityKg = cursor.getDouble(cursor.getColumnIndexOrThrow("quantityKg")),
                    scrapItem = getScrapItem(cursor.getInt(cursor.getColumnIndexOrThrow("scrapItemId")))
                )
                scrapItems.add(scrapItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return scrapItems
    }


    //Fetch single pickukpsra
    fun getPickupScrapItem(pickupId: Int, scrapItemId: Int): PickupScrapItem? {
        val db = this.readableDatabase
        val cursor = db.query(
            "pickup_scrap_item",
            null,
            "pickupId = ? AND scrapItemId = ?",
            arrayOf(pickupId.toString(), scrapItemId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            PickupScrapItem(
                pickupId = cursor.getInt(cursor.getColumnIndexOrThrow("pickupId")),
                scrapItemId = cursor.getInt(cursor.getColumnIndexOrThrow("scrapItemId")),
                quantityKg = cursor.getDouble(cursor.getColumnIndexOrThrow("quantityKg")),
                scrapItem = getScrapItem(cursor.getInt(cursor.getColumnIndexOrThrow("scrapItemId")))
            )
        } else null
    }

    //Fetch Scrapitem:
    fun getScrapItem(scrapItemId: Int): ScrapItem? {
        val db = this.readableDatabase
        val cursor = db.query(
            "scrap_item",
            null,
            "id = ?",
            arrayOf(scrapItemId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            ScrapItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                scrapType = cursor.getString(cursor.getColumnIndexOrThrow("scrapType")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                pricePerKg = cursor.getDouble(cursor.getColumnIndexOrThrow("pricePerKg")),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt")),
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))
            )
        } else null
    }
}
