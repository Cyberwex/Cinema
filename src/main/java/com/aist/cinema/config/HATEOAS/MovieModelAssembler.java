package com.aist.cinema.config.HATEOAS;

import com.aist.cinema.controller.MovieController;
import com.aist.cinema.entity.Movie;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieModelAssembler implements RepresentationModelAssembler<Movie, EntityModel<Movie>> {
    @Override
    public EntityModel<Movie> toModel(Movie movie) {
        return EntityModel.of(movie,
                linkTo(methodOn(MovieController.class).getMovieById(movie.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).getAllMovies()).withRel(IanaLinkRelations.COLLECTION));
    }
}
