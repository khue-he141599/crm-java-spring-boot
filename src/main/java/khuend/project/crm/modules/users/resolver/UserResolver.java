package khuend.project.crm.modules.users.resolver;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.UserResponse;
import khuend.project.crm.modules.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserResolver {

    private final UserService userService;

    @QueryMapping(name = "getListUsers")
    public List<UserResponse> users() {
        return userService.findAll();
    }

    @QueryMapping(name = "getUserById")
    public UserResponse userById(@Argument UUID id) {
        return userService.findById(id);
    }

    @MutationMapping(name = "createUser")
    public UserResponse createUser(@Argument @Valid CreateUserRequest input) {
        return userService.create(input);
    }
}
