package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Status;

import java.util.List;

public interface IStatusService {
    void batchSave(List<Status> statuses);
}
