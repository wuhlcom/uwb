package com.zhilutec.services;

import com.zhilutec.dbs.entities.Position;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPositionService {


    void positionCacheInit();

    abstract List<Position> getPositions();

    String getPositionsRs();

}
