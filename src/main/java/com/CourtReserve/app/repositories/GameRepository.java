package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {
}