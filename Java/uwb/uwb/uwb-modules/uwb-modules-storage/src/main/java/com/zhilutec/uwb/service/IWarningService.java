package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Warning;

import java.util.List;

public interface IWarningService {

    void batchSave(List<Warning> warnings);
}
