package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.config.AWSConfig;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.resolver.dataloaders.MainTechDataLoader;
import yoonleeverse.onlinecourseback.modules.course.service.TechService;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
@Slf4j
public class TechDataFetcher {

    private final TechService techService;

    @DgsData(parentType = "CourseType")
    public CompletableFuture<List<TechType>> mainTechs(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, List<TechType>> mainTechsDataLoader = dfe.getDataLoader(MainTechDataLoader.class);
        CourseType course = dfe.getSource();

        return mainTechsDataLoader.load(course.getSlug());
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public List<TechType> getAllTech() {
        return techService.getAllTech();
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType addTech(@InputArgument String name, DataFetchingEnvironment dfe) {
        return techService.addTech(dfe.getArgument("logo"), name);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType removeTech(@InputArgument Long id) {
        return techService.removeTech(id);
    }
}
