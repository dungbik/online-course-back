package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;
import yoonleeverse.onlinecourseback.modules.course.repository.TechRepository;
import yoonleeverse.onlinecourseback.modules.course.service.TechService;
import yoonleeverse.onlinecourseback.modules.course.types.AddTechInput;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechServiceImpl implements TechService {

    private final TechRepository techRepository;

    @Override
    public List<TechType> getAllTech() {
        return techRepository.findAll().stream()
                .map(TechType::new)
                .collect(Collectors.toList());
    }

    @Override
    public ResultType addTech(AddTechInput input) {
        try {
            if (techRepository.existsByName(input.getName()))
                throw new RuntimeException("이미 존재하는 이름입니다.");

            TechEntity tech = TechEntity.makeTech(input);
            techRepository.save(tech);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }
}
