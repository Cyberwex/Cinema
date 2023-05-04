package com.aist.cinema.controller;

import com.aist.cinema.config.HATEOAS.MovieModelAssembler;
import com.aist.cinema.entity.Movie;
import com.aist.cinema.service.MovieService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class MovieController {
    private final MovieService movieService;
    private final MovieModelAssembler assembler;

    public MovieController(MovieService movieService, MovieModelAssembler assembler) {
        this.movieService = movieService;
        this.assembler = assembler;
    }

    @GetMapping("/movies")
    public CollectionModel<EntityModel<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        List<EntityModel<Movie>> movieModels = movies.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(movieModels,
                linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel());
    }

    @GetMapping("/movies/{id}")
    public EntityModel<Movie> getMovieById(@PathVariable("id") Long id) {
        Movie movie = movieService.getMovieById(id);
        return assembler.toModel(movie);
    }

    @PostMapping("/movies")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Movie>> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.saveMovie(movie);
        EntityModel<Movie> movieModel = assembler.toModel(createdMovie);

        return ResponseEntity.created(movieModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(movieModel);
    }

    @PutMapping("/movies/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Movie>> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        EntityModel<Movie> movieModel = assembler.toModel(updatedMovie);

        return ResponseEntity.ok(movieModel);
    }

    @DeleteMapping("/movies/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteMovie(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }
}
