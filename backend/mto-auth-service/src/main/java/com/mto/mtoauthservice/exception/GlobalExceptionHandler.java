package com.mto.mtoauthservice.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected @Nullable GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        // ✅ Custom messages
        String message = "Something went wrong";

        if (ex instanceof RuntimeException) {
            message = ex.getMessage();
        }

        if (ex instanceof ConstraintViolationException) {
            message = "Invalid input provided";
        }

        return GraphqlErrorBuilder.newError(env)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .build();

    }
}
