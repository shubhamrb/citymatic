package com.mamits.citymatic.ui.utils.rx;

import io.reactivex.Scheduler;

public interface ISchedulerProvider {
    Scheduler io();

    Scheduler ui();

}
