package mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mvvmnewsapp.models.Article
import mvvmnewsapp.models.NewsResponse
import mvvmnewsapp.repository.NewsRepository
import mvvmnewsapp.utils.Resource
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    // val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    // val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    private val _breakingNewsUiState = MutableStateFlow<Resource<NewsResponse>>(Resource.Empty())
    val breakingNewsUiState: StateFlow<Resource<NewsResponse>> = _breakingNewsUiState
    var breakingNewsPage = 1

    private val _searchNewsUiState = MutableStateFlow<Resource<NewsResponse>>(Resource.Empty())
    val searchNewsUiState: StateFlow<Resource<NewsResponse>> = _searchNewsUiState
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
//        breakingNews.postValue(Resource.Loading())
        _breakingNewsUiState.value = Resource.Loading()
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNewsUiState.value = handleBreakingNewsResponse(response)
//        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
//        searchNews.postValue(Resource.Loading())
        _searchNewsUiState.value = Resource.Loading()
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        _searchNewsUiState.value = handleSearchNewsResponse(response)
//        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}