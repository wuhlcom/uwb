package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Position;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPositionService {


    void positionCacheInit();

    abstract List<Position> getPositions();

    String getPositionsRs();

}
