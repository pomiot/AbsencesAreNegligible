package pl.edu.amu.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;

import pl.edu.amu.rest.dto.Player;

public class PlayerUtility implements PlayerRepository {

	@Override
	public List<Player> getAllPlayers() {

		EntityManager entityManager = DatabaseManager.getEntityManager();

		return entityManager.createQuery(
			    "SELECT p FROM Player p")
			    .setMaxResults(10)
			    .getResultList();
	}

	@Override
	public Player getPlayerByLogin(String login) throws NotFoundException {
		EntityManager entityManager = DatabaseManager.getEntityManager();

		try
		{
			return (Player) entityManager.createQuery(
					"SELECT p FROM Player p WHERE p.login LIKE :login")
					.setParameter("login", login)
					.getSingleResult();
		}
		catch (Exception e)
		{
			throw new NotFoundException();
		}
	}

	@Override
	public boolean addPlayer(Player player) {

		boolean result = false;

		EntityManager entityManager = DatabaseManager.getEntityManager();
		try
		{
			entityManager.getTransaction().begin();
			entityManager.persist(player);
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
	public boolean deletePlayer(String login) {

		boolean result = false;

		EntityManager entityManager = DatabaseManager.getEntityManager();

		try
		{
			int deleted = entityManager.createQuery(
								"DELETE FROM Player p WHERE p.login LIKE :login")
								.setParameter("login", login).executeUpdate();

			result = deleted >= 1 ? true : false;
		}
		catch (Exception e)
		{
			throw new NotFoundException();
		}

		return result;
	}

	@Override
	public Player updatePlayerByLogin(String login, Player player) {
		return null;
	}

	@Override
	public Player getPlayerById(Long id) {
		return null;
	}

}
