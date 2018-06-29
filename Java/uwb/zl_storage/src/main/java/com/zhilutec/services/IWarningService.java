package com.zhilutec.services;

import com.zhilutec.dbs.entities.Warning;

import java.util.List;

public interface IWarningService {

    void batchSave(List<Warning> warnings);
}
