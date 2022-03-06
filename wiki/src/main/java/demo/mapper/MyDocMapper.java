package demo.mapper;

import demo.domain.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyDocMapper {
    public void increaseViewCount(@Param("id") Long id);

    public void increaseVoteCount(@Param("id") Long id);

    public void updateEbookInfo();
}
