import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    private const val DIRECTORY_NAME = "MahasiswaPintarPhotos"

    fun getPhotoDirectory(context: Context): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    fun saveBitmapToFile(bitmap: Bitmap, context: Context): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getPhotoDirectory(context)
        try {
            val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            return imageFile
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getBitmapFromFile(photoFile: File): Bitmap? {
        return BitmapFactory.decodeFile(photoFile.absolutePath)
    }

}
