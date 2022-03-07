package demo.service;

import demo.mapper.MyEbookSnapshotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbookSnapshotService {
    @Autowired
    private MyEbookSnapshotMapper mapper;
    public void genSnapshot(){
        mapper.genSnapshot();
    }
}
