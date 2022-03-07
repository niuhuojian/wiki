package demo.mapper;

import demo.resp.DataResp;

import java.util.List;

public interface MyEbookSnapshotMapper {
    public void genSnapshot();

    public List<DataResp> getData();
}
