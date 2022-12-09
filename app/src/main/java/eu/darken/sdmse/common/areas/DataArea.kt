package eu.darken.sdmse.common.areas

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import eu.darken.sdmse.common.files.core.APath
import eu.darken.sdmse.common.user.UserHandle2
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataArea(
    val path: APath,
    val type: Type,
    val label: String = path.path,
    val flags: Set<Flag> = emptySet(),
    /**
     * -1 location has no user seperation
     * 0 admin user/owner
     * X other users
     */
    val userHandle: UserHandle2,
) : Parcelable {

    enum class Flag {
        PRIMARY, SECONDARY, EMULATED
    }

    @Keep @Parcelize
    enum class Type(val raw: String) : Parcelable {
        @Json(name = "SDCARD") SDCARD("SDCARD"),
        @Json(name = "PUBLIC_MEDIA") PUBLIC_MEDIA("PUBLIC_MEDIA"),
        @Json(name = "PUBLIC_DATA") PUBLIC_DATA("PUBLIC_DATA"),
        @Json(name = "PUBLIC_OBB") PUBLIC_OBB("PUBLIC_OBB"),
        @Json(name = "PRIVATE_DATA") PRIVATE_DATA("PRIVATE_DATA"),
        @Json(name = "APP_LIB") APP_LIB("APP_LIB"),
        @Json(name = "APP_ASEC") APP_ASEC("APP_ASEC"),
        @Json(name = "APP_APP") APP_APP("APP_APP"),
        @Json(name = "APP_APP_PRIVATE") APP_APP_PRIVATE("APP_APP_PRIVATE"),
        @Json(name = "DALVIK_DEX") DALVIK_DEX("DALVIK_DEX"),
        @Json(name = "DALVIK_PROFILE") DALVIK_PROFILE("DALVIK_PROFILE"),
        @Json(name = "DOWNLOAD_CACHE") DOWNLOAD_CACHE("DOWNLOAD_CACHE"),
        @Json(name = "DATA") DATA("DATA"),
        @Json(name = "DATA_SYSTEM") DATA_SYSTEM("DATA_SYSTEM"),

        /**
         * Base directory for per-user system directory, credential encrypted.
         */
        @Json(name = "DATA_SYSTEM_CE") DATA_SYSTEM_CE("DATA_SYSTEM_CE"),

        /**
         * Base directory for per-user system directory, device encrypted.
         */
        @Json(name = "DATA_SYSTEM_DE") DATA_SYSTEM_DE("DATA_SYSTEM_DE"),

        @Json(name = "PORTABLE") PORTABLE("PORTABLE"),

        /**
         * Link2SD https://play.google.com/store/apps/details?id=com.buak.Link2SD
         * Apps2SD https://play.google.com/store/apps/details?id=com.a0soft.gphone.app2sd
         */
        @Json(name = "DATA_SDEXT2") DATA_SDEXT2("DATA_SDEXT2"),
        ;

        companion object {
            fun fromRaw(raw: String): Type =
                values().singleOrNull { it.raw == raw } ?: throw IllegalArgumentException("Unknown location TAG: $raw")

            @JvmField val PUBLIC_LOCATIONS = listOf(
                SDCARD, PUBLIC_DATA, PUBLIC_OBB, PUBLIC_MEDIA, PORTABLE
            )
        }
    }
}
