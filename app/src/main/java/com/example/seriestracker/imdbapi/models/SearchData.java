package com.example.seriestracker.imdbapi.models;

import java.util.List;

public class SearchData
{
    private String QueryString;
    private List<SearchResult> results;
    private String errorMessage;

    public String getQueryString() {
        return QueryString;
    }

    public void setQueryString(String queryString) {
        QueryString = queryString;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
