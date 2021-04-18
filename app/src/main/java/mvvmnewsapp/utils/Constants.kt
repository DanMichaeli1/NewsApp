package mvvmnewsapp.utils

import com.example.newsapp.BuildConfig

class Constants {
    companion object{
        const val API_KEY = BuildConfig.ApiKey
        const val BASE_URL = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY = 500L
    }
}