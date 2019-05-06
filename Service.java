package com.elenakozachenko.githubtest.api;

import com.elenakozachenko.githubtest.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
@GET("/search/users?q=language;java+location:russia")
    Call<ItemResponse> getItems();
}
