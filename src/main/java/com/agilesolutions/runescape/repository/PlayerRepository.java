package com.agilesolutions.runescape.repository;

import com.agilesolutions.runescape.model.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> { }
