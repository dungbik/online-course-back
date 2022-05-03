package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;
import yoonleeverse.onlinecourseback.modules.course.mapper.CourseMapper;
import yoonleeverse.onlinecourseback.modules.course.repository.TechRepository;
import yoonleeverse.onlinecourseback.modules.course.service.TechService;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;
import yoonleeverse.onlinecourseback.modules.file.mapper.FileMapper;
import yoonleeverse.onlinecourseback.modules.file.service.StorageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechServiceImpl implements TechService {

    private final TechRepository techRepository;
    private final StorageService storageService;
    private final CourseMapper courseMapper;
    private final FileMapper fileMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TechType> getAllTech() {
        return techRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResultType addTech(MultipartFile file, String name) {
        try {
            if (techRepository.existsByName(name))
                throw new RuntimeException("이미 존재하는 이름입니다.");

            String fileUrl = storageService.put(file, name, "public/tech");
            FileEntity logo = fileMapper.toEntity(fileUrl);

            techRepository.save(courseMapper.toEntity(name, logo));

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType removeTech(Long id) {
        try {
            TechEntity exTech = techRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 기술입니다."));

            FileEntity logo = exTech.getLogo();
            if (logo != null) {
                storageService.delete(logo.getFileUrl());
            }

            techRepository.delete(exTech);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }
}
