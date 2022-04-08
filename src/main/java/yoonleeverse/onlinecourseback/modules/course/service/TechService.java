package yoonleeverse.onlinecourseback.modules.course.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddTechInput;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;

public interface TechService {

    List<TechType> getAllTech();

    ResultType addTech(AddTechInput input);

    ResultType removeTech(Long id);
}
