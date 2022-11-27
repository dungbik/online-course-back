package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import yoonleeverse.onlinecourseback.generated.client.*;
import yoonleeverse.onlinecourseback.generated.types.ResultType;
import yoonleeverse.onlinecourseback.generated.types.TechType;
import yoonleeverse.onlinecourseback.modules.course.repository.TechRepository;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;

import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@SpringBootTest
@WithUserDetails("test@test.com")
@Transactional
@Rollback(value = false)
class TechDataFetcherE2ETest {
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    TechRepository techRepository;

    @Test
    @Order(1)
    void addTech() throws Exception {
        FileInputStream inputFile = new FileInputStream("src/test/resources/test.jpeg");
        MockMultipartFile logo = new MockMultipartFile("file", "image.jpeg", MULTIPART_FORM_DATA_VALUE, inputFile);

        String query = "mutation addTech($logo: Upload!) { addTech(name: \"test\", logo: $logo) { success, error } }";

        ResultType result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query, "data.addTech", Maps.newHashMap("logo", logo), ResultType.class);

        assertThat(result.getSuccess()).isTrue();
        assertThat(result.getError()).isNullOrEmpty();
    }

    @Test
    @Order(2)
    void getAllTech() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                new GetAllTechGraphQLQuery.Builder().build(),
                new GetAllTechProjectionRoot().id().name().logo()
        );

        List<TechType> result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(), "data.getAllTech[*]", new TypeRef<List<TechType>>() { });
        System.out.println(result);
        assertThat(result.size()).isEqualTo(1);

        TechType firstElem = result.get(0);
        assertThat(firstElem.getId()).isNotNull();
        assertThat(firstElem.getName()).isEqualTo("test");
        assertThat(firstElem.getLogo()).isNotEmpty();
    }

    @Test
    @Order(3)
    void removeTech() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                new RemoveTechGraphQLQuery.Builder().id(techRepository.findAll().get(0).getId()).build(),
                new RemoveTechProjectionRoot().success().error()
        );

        ResultType result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(), "data.removeTech", ResultType.class);
        assertThat(result.getSuccess()).isTrue();
        assertThat(result.getError()).isNullOrEmpty();
    }
}