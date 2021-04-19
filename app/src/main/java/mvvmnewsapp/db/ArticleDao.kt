package mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import mvvmnewsapp.models.Article

// Each Dao needs to be annotated by @Dao to be recognized by Room
@Dao
interface ArticleDao {

    // Insert / update article
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long // Returns the ID of inserted article

    // Query to receive all articles from the db
    @Query("SELECT * FROM articles") // SQL query that selects all articles this function should return
    fun getAllArticles(): LiveData<List<Article>> // LiveData : enables fragments to subscribe to changes in a LiveData, useful for device rotations (View gets most updated data)

    // Delete an article
    @Delete
    suspend fun deleteArticle(article: Article)

}