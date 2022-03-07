package demo.service;

import demo.mapper.MyEbookSnapshotMapper;
import demo.resp.DataResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookSnapshotService {
    @Autowired
    private MyEbookSnapshotMapper mapper;
    public void genSnapshot(){
        mapper.genSnapshot();
    }

    public List<DataResp> getData(){
        return mapper.getData();
    }
}
