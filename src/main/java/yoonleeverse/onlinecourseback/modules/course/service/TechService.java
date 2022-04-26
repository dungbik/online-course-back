package yoonleeverse.onlinecourseback.modules.course.service;

import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;

public interface TechService {

    List<TechType> getAllTech();

    ResultType addTech(MultipartFile file, String name);

    ResultType removeTech(Long id);
}
