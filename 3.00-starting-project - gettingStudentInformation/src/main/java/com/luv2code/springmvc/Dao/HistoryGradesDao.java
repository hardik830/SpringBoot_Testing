package com.luv2code.springmvc.Dao;

import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradesDao extends CrudRepository<HistoryGrade,Integer> {

    public Iterable<HistoryGrade> findGradeByStudentId(int id);

   public void deleteByStudentId(int id);
}
