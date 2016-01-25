package pl.edu.amu.repositoryImplementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;

import pl.edu.amu.database.DatabaseManager;
import pl.edu.amu.database.MatchRepository;
import pl.edu.amu.rest.dto.Match;
import pl.edu.amu.rest.dto.Player;
import pl.edu.amu.rest.dto.ThrowSet;

public class MatchRepositoryImpl implements MatchRepository {

	@Override
	public List<Match> getAllMatches() {
		
		EntityManager entityManager = DatabaseManager.getEntityManager();

		return entityManager.createQuery(
			    "SELECT m FROM Match m")
			    .getResultList();
	
	}

	@Override
	public Match getMatchById(Long matchId) throws NotFoundException {

		EntityManager entityManager = DatabaseManager.getEntityManager();

		try
		{
			return (Match) entityManager.createQuery(
					"SELECT m FROM Match m WHERE m.id = :matchId")
					.setParameter("matchId", matchId)
					.getSingleResult();
		}
		catch (Exception e)
		{
			throw new NotFoundException();
		}
	}

	@Override
	public boolean addMatch(Match match) {

		boolean result = false;

		EntityManager entityManager = DatabaseManager.getEntityManager();
		try
		{
			entityManager.getTransaction().begin();
			entityManager.persist(match);
			entityManager.getTransaction().commit();

			result = true;
		}
		catch (Exception e)
		{
			entityManager.getTransaction().rollback();

			result = false;
		}

		return result;
	}

	@Override
	public boolean deleteMatch(Long matchId) {
		
		EntityManager entityManager = DatabaseManager.getEntityManager();

		try
		{
			entityManager.getTransaction().begin();
			entityManager.remove(getMatchById(matchId));
			entityManager.getTransaction().commit();
			
			return true;
		}
		catch (Exception e)
		{	
			entityManager.getTransaction().rollback();
			
			throw new NotFoundException();
		}
	}

	@Override
	public Match updateMatch(Match match) {
		
		EntityManager entityManager = DatabaseManager.getEntityManager();
		
		try
		{
			entityManager.getTransaction().begin();
			
			entityManager.createQuery(
					"UPDATE Match m SET m.tournament_name = :newTournamentName WHERE m.id LIKE :id")
					.setParameter("newTournamentName", match.getTournamentName())
					.setParameter("id", match.getId())
					.executeUpdate();
			
			entityManager.getTransaction().commit();
			entityManager.refresh(getMatchById(match.getId()));
			return getMatchById(match.getId());
		}
		catch (Exception e)
		{	
			entityManager.getTransaction().rollback();
			
			throw new NotFoundException();
		}
	}

	@Override
	public List<Player> getPlayersInMatch(Long matchId) {
		
		EntityManager entityManager = DatabaseManager.getEntityManager();
		
		try
		{
			List<String> logins = entityManager.createQuery(
				    "SELECT DISTINCT t.player FROM ThrowSet t where t.matchId = :matchId")
				    .setParameter("matchId", matchId)
				    .getResultList();
			
			if (logins == null || logins.isEmpty()) 
			{
				throw new NotFoundException();
			}
			
			return entityManager.createQuery(
				    "SELECT DISTINCT p FROM Player p where p.login IN :logins")
				    .setParameter("logins", logins)
				    .getResultList();
		}
		catch (Exception e)
		{	
			throw new NotFoundException();
		}
	}

	@Override
	public List<ThrowSet> getAllThrowsInMatch(Long matchId) {
		
		EntityManager entityManager = DatabaseManager.getEntityManager();

		try
		{
			return entityManager.createQuery(
					"SELECT t FROM ThrowSet t WHERE t.matchId = :matchId")
					.setParameter("matchId", matchId)
					.getResultList();
		}
		catch (Exception e)
		{
			throw new NotFoundException();
		}
	}

	@Override
	public List<ThrowSet> getThrowsInRound(Long matchId, Integer roundNumber) {
		
		EntityManager entityManager = DatabaseManager.getEntityManager();
		
		try
		{
			return entityManager.createQuery(
					"SELECT t FROM ThrowSet t WHERE t.matchId = :matchId AND t.round = :roundNumber")
					.setParameter("matchId", matchId)
					.setParameter("roundNumber", roundNumber)
					.getResultList();
		}
		catch (Exception e)
		{
			throw new NotFoundException();
		}
	}

	@Override
	public boolean addThrowSetToRound(Long matchId, Integer roundNumber,
			ThrowSet throwSet) {
		
		boolean result = false;
		
		EntityManager entityManager = DatabaseManager.getEntityManager();
		try
		{
			throwSet.setMatchId(matchId);
			throwSet.setRound(roundNumber);
			
			entityManager.getTransaction().begin();
			entityManager.persist(throwSet);
			entityManager.getTransaction().commit();
			
			result = true;
		}
		catch (Exception e)
		{
			entityManager.getTransaction().rollback();
			
			result = false;
		}
		
		return result;
	}


}