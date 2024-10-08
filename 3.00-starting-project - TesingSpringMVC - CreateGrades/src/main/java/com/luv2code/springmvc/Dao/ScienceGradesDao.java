package com.luv2code.springmvc.Dao;

import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradesDao extends CrudRepository<ScienceGrade,Integer> {
    public Iterable<ScienceGrade> findGradeByStudentId(int id);

   public void deleteByStudentId(int id);
}
