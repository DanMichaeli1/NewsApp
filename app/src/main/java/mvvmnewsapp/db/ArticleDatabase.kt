package mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mvvmnewsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao // Room implements this function

    companion object {
        @Volatile // Other threads immediately can see when a thread changes this instance
        private var instance: ArticleDatabase? = null
        private val LOCK = Any() // Used to synchronize setting this instance

        // If instance is not null, set the instance in the synchronized block
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: creatDatabase(context).also { instance = it }
        }

        private fun creatDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}