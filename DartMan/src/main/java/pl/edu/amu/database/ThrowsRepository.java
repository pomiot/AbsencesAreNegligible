package pl.edu.amu.database;

import pl.edu.amu.rest.dto.ThrowSet;

import java.util.List;

public interface ThrowsRepository {

    List<ThrowSet> getThrowsByMatchId(String matchId);

    ThrowSet getThrowSetById(String throwSetId);

    List<ThrowSet> getPlayerThrowsInMatch(String matchId, String login);

    ThrowSet addThrowSet(ThrowSet throwSet);

    List<ThrowSet> getThrowsFromRoundInMatch(String matchId, Integer roundNumber);

}
