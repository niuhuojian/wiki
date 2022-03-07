package demo.mapper;

import demo.resp.StatisticResp;

import java.util.List;

public interface MyEbookSnapshotMapper {
    public void genSnapshot();

    public List<StatisticResp> getData();

    public List<StatisticResp> get30Data();
}
