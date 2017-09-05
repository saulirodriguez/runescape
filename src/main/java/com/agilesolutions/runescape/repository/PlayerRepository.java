package com.agilesolutions.runescape.repository;

import com.agilesolutions.runescape.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> { }
