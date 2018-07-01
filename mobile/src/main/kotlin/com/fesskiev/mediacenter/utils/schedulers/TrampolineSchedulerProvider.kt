package com.fesskiev.mediacenter.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


class TrampolineSchedulerProvider : BaseSchedulerProvider {

    override fun  computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun multi(): Scheduler {
        return Schedulers.trampoline()
    }
}