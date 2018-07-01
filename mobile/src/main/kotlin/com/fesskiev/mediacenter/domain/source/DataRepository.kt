package com.fesskiev.mediacenter.domain.source

import com.fesskiev.mediacenter.domain.source.local.LocalDataSource
import com.fesskiev.mediacenter.domain.source.remote.RemoteDataSource


class DataRepository(val remoteSource: RemoteDataSource, val localDataSource: LocalDataSource) {

}