package com.zhilutec.services;

import com.zhilutec.dbs.entities.Status;

import java.util.List;

public interface IStatusService {
    void batchSave(List<Status> statuses);
}
